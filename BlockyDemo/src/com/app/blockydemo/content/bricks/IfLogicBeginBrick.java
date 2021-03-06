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
package com.app.blockydemo.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


import com.app.blockydemo.R;
import com.app.blockydemo.common.BrickValues;
import com.app.blockydemo.content.Script;
import com.app.blockydemo.content.Sprite;
import com.app.blockydemo.formulaeditor.Formula;
import com.app.blockydemo.ui.fragment.FormulaEditorFragment;

import java.util.ArrayList;
import java.util.List;

public class IfLogicBeginBrick extends NestingBrick implements OnClickListener, FormulaBrick {
	private static final long serialVersionUID = 1L;
	private static final String TAG = IfLogicBeginBrick.class.getSimpleName();
	public static final int EXECUTE_ELSE_PART = -1;
	private Formula ifCondition;
	protected IfLogicElseBrick ifElseBrick;
	protected IfLogicEndBrick ifEndBrick;
	private transient IfLogicBeginBrick copy;

	public IfLogicBeginBrick(Sprite sprite, int condition) {
		this.sprite = sprite;
		ifCondition = new Formula(condition);
	}

	public IfLogicBeginBrick(Sprite sprite, Formula condition) {
		this.sprite = sprite;
		ifCondition = condition;
	}

	@Override
	public Formula getFormula() {
		return ifCondition;
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	public IfLogicElseBrick getIfElseBrick() {
		return ifElseBrick;
	}

	public IfLogicEndBrick getIfEndBrick() {
		return ifEndBrick;
	}

	public IfLogicBeginBrick getCopy() {
		return copy;
	}

	public void setIfElseBrick(IfLogicElseBrick elseBrick) {
		this.ifElseBrick = elseBrick;
	}

	public void setIfEndBrick(IfLogicEndBrick ifEndBrick) {
		this.ifEndBrick = ifEndBrick;
	}

	@Override
	public Brick clone() {
		return new IfLogicBeginBrick(sprite, ifCondition.clone());
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}

		view = View.inflate(context, R.layout.brick_if_begin_if, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_if_begin_checkbox);
		final Brick brickInstance = this;

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView prototypeTextView = (TextView) view.findViewById(R.id.brick_if_begin_prototype_text_view);
		TextView ifBeginTextView = (TextView) view.findViewById(R.id.brick_if_begin_edit_text);

		ifCondition.setTextFieldId(R.id.brick_if_begin_edit_text);
		ifCondition.refreshTextField(view);

		prototypeTextView.setVisibility(View.GONE);
		ifBeginTextView.setVisibility(View.VISIBLE);

		ifBeginTextView.setOnClickListener(this);

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_if_begin_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView ifLabel = (TextView) view.findViewById(R.id.if_label);
			//TextView ifLabelEnd = (TextView) view.findViewById(R.id.if_label_second_part);
			TextView editX = (TextView) view.findViewById(R.id.brick_if_begin_edit_text);
			ifLabel.setTextColor(ifLabel.getTextColors().withAlpha(alphaValue));
			//ifLabelEnd.setTextColor(ifLabelEnd.getTextColors().withAlpha(alphaValue));
			editX.setTextColor(editX.getTextColors().withAlpha(alphaValue));
			editX.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_if_begin_if, null);
		TextView textIfBegin = (TextView) prototypeView.findViewById(R.id.brick_if_begin_prototype_text_view);
		textIfBegin.setText(String.valueOf(BrickValues.IF_CONDITION));
		return prototypeView;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, ifCondition);
	}

	@Override
	public boolean isInitialized() {
		if (ifElseBrick == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void initialize() {
		ifElseBrick = new IfLogicElseBrick(sprite, this);
		ifEndBrick = new IfLogicEndBrick(sprite, ifElseBrick, this);
		Log.w(TAG, "Creating if logic stuff");
	}

	@Override
	public List<NestingBrick> getAllNestingBrickParts(boolean sorted) {
		//TODO: handle sorting
		List<NestingBrick> nestingBrickList = new ArrayList<NestingBrick>();
		if (sorted) {
			nestingBrickList.add(this);
			nestingBrickList.add(ifElseBrick);
			nestingBrickList.add(ifEndBrick);
		} else {
			nestingBrickList.add(this);
			nestingBrickList.add(ifEndBrick);
		}

		return nestingBrickList;
	}

	@Override
	public boolean isDraggableOver(Brick brick) {
		if (brick == ifElseBrick) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite, Script script) {
		//ifEndBrick and ifElseBrick will be set in the copyBrickForSprite method of IfLogicEndBrick
		IfLogicBeginBrick copyBrick = (IfLogicBeginBrick) clone(); //Using the clone method because of its flexibility if new fields are added  
		copyBrick.ifElseBrick = null; //if the Formula gets a field sprite, a separate copy method will be needed
		copyBrick.ifEndBrick = null;
		copyBrick.sprite = sprite;
		this.copy = copyBrick;
		return copyBrick;
	}
	
	@Override
	public String getScript() {
		return "if ("+ifCondition.getDisplayString(null).replace("=", "==")+") {\n";
	}
}
