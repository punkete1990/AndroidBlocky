/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.app.blockydemo.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;


import com.app.blockydemo.ProjectManager;
import com.app.blockydemo.R;
import com.app.blockydemo.content.Project;
import com.app.blockydemo.content.Script;
import com.app.blockydemo.content.Sprite;
import com.app.blockydemo.ui.BottomBar;
import com.app.blockydemo.ui.ScriptActivity;
import com.app.blockydemo.ui.adapter.ScriptAdapter;
import com.app.blockydemo.ui.adapter.ScriptAdapter;
import com.app.blockydemo.ui.dialogs.NewVariableDialog;
import com.app.blockydemo.ui.dialogs.NewVariableDialog.NewVariableDialogListener;
import com.app.blockydemo.utils.Utils;

public class FormulaEditorScriptListFragment extends ListFragment implements Dialog.OnKeyListener,
ScriptAdapter.OnCheckedChangeListener, ScriptAdapter.OnListItemClickListener {

	public static final String SCRIPT_TAG = "scriptFragment";
	public static final String EDIT_TEXT_BUNDLE_ARGUMENT = "formulaEditorEditText";
	public static final String ACTION_BAR_TITLE_BUNDLE_ARGUMENT = "actionBarTitle";
	public static final String FRAGMENT_TAG_BUNDLE_ARGUMENT = "fragmentTag";

	private String actionBarTitle;
	private ActionMode contextActionMode;
	private View selectAllActionModeButton;
	private boolean inContextMode;
	private int deleteIndex;
	private ScriptAdapter adapter;

	public FormulaEditorScriptListFragment() {
		contextActionMode = null;
		deleteIndex = -1;
		inContextMode = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("CatroidFragmentTag", "FormulaEditorVariableList onresume()");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		initializeScriptAdapter();
		getActivity().findViewById(R.id.bottom_bar).setVisibility(View.GONE);
		this.actionBarTitle = getArguments().getString(ACTION_BAR_TITLE_BUNDLE_ARGUMENT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_formula_editor_variablelist, container, false);
		return fragmentView;
	}



	@Override
	public void onListItemClick(int position) {
		Log.d("catroid", "onListItemClick");
		if (!inContextMode) {
			FormulaEditorFragment formulaEditor = (FormulaEditorFragment) getActivity()
					.getSupportFragmentManager().findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
			if (formulaEditor != null) {
				formulaEditor.addUserVariableToActiveFormula(adapter.getItem(position).getName());
				formulaEditor.updateButtonViewOnKeyboard();
			}
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
			onKey(null, keyEvent.getKeyCode(), keyEvent);
		}

	}

	@Override
	public void onCheckedChange() {
		if (!inContextMode) {
			return;
		}

		updateActionModeTitle();
		Utils.setSelectAllActionModeButtonVisibility(selectAllActionModeButton,
				adapter.getCount() > 0 && adapter.getAmountOfCheckedItems() != adapter.getCount());
	}

	private void updateActionModeTitle() {
		String title = adapter.getAmountOfCheckedItems()
				+ " "
				+ getActivity().getResources().getQuantityString(
						R.plurals.formula_editor_variable_context_action_item_selected,
						adapter.getAmountOfCheckedItems());

		contextActionMode.setTitle(title);
	}

	@Override
	public void onStart() {

		this.registerForContextMenu(getListView());
		getListView().setItemsCanFocus(true);
		getListView().setLongClickable(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
				if (!inContextMode) {
					deleteIndex = position;
					getActivity().openContextMenu(getListView());
					return true;
				}
				return false;
			}
		});

		adapter.notifyDataSetChanged();

		super.onStart();
	}





	public void showFragment(Context context) {
		FragmentActivity activity = (FragmentActivity) context;
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragmentManager.beginTransaction();

		Fragment formulaEditorFragment = fragmentManager
				.findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
		fragTransaction.hide(formulaEditorFragment);

		BottomBar.showBottomBar(activity);
		BottomBar.hidePlayButton(activity);

		fragTransaction.show(this);
		fragTransaction.commit();

		if (adapter != null) {
			initializeScriptAdapter();
		}
	}

	private void initializeScriptAdapter() {
		Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
		adapter = new ScriptAdapter(getActivity(),currentSprite.getScriptList() );
		setListAdapter(adapter);
		adapter.setOnCheckedChangeListener(this);
		adapter.setOnListItemClickListener(this);
	}

	@Override
	public boolean onKey(DialogInterface d, int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			getActivity().findViewById(R.id.bottom_bar).setVisibility(View.GONE);
			((ScriptActivity) getActivity()).updateHandleAddButtonClickListener();

			FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.hide(this);
			FormulaEditorFragment formulaEditorFragment = (FormulaEditorFragment) getActivity()
					.getSupportFragmentManager().findFragmentByTag(
							FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
			formulaEditorFragment.updateBrickView();
			fragmentTransaction.show(formulaEditorFragment);
			fragmentTransaction.commit();
			return true;
		default:
			break;
		}
		return false;
	}

	private void addSelectAllActionModeButton(ActionMode mode, Menu menu) {
		selectAllActionModeButton = Utils.addSelectAllActionModeButton(getLayoutInflater(null), mode, menu);
		selectAllActionModeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				for (int position = 0; position < adapter.getCount(); position++) {
					adapter.addCheckedItem(position);
				}
				adapter.notifyDataSetChanged();
				onCheckedChange();
			}
		});
	}

}
