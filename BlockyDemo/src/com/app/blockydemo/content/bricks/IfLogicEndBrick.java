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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.app.blockydemo.R;
import com.app.blockydemo.content.Script;
import com.app.blockydemo.content.Sprite;

import java.util.ArrayList;
import java.util.List;

public class IfLogicEndBrick extends NestingBrick implements AllowedAfterDeadEndBrick {

	static final int FOREVER = -1;
	private static final long serialVersionUID = 1L;
	private static final String TAG = IfLogicEndBrick.class.getSimpleName();
	private IfLogicElseBrick ifElseBrick;

	private IfLogicBeginBrick ifBeginBrick;

	public IfLogicEndBrick(Sprite sprite, IfLogicElseBrick elseBrick, IfLogicBeginBrick beginBrick) {
		this.sprite = sprite;
		this.ifElseBrick = elseBrick;
		this.ifBeginBrick = beginBrick;
		beginBrick.setIfEndBrick(this);
		elseBrick.setIfEndBrick(this);
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	public IfLogicElseBrick getIfElseBrick() {
		return ifElseBrick;
	}

	public IfLogicBeginBrick getIfBeginBrick() {
		return ifBeginBrick;
	}

	public void setIfElseBrick(IfLogicElseBrick ifElseBrick) {
		this.ifElseBrick = ifElseBrick;
	}

	public void setIfBeginBrick(IfLogicBeginBrick ifBeginBrick) {
		this.ifBeginBrick = ifBeginBrick;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.brick_if_end_if, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_if_end_if_checkbox);
		final Brick brickInstance = this;

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});
		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_if_end_if_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView ifEndLabel = (TextView) view.findViewById(R.id.brick_if_end_if_label);
			ifEndLabel.setTextColor(ifEndLabel.getTextColors().withAlpha(alphaValue));

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public Brick clone() {
		return new IfLogicEndBrick(getSprite(), ifElseBrick, ifBeginBrick);
	}

	@Override
	public View getPrototypeView(Context context) {
		return View.inflate(context, R.layout.brick_if_end_if, null);
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
	public boolean isInitialized() {
		if (ifElseBrick == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void initialize() {
		//ifElseBrick = new IfLogicElseBrick(sprite);
		Log.w(TAG, "Cannot create the IfLogic Bricks from here!");
	}

	@Override
	public List<NestingBrick> getAllNestingBrickParts(boolean sorted) {
		//TODO: handle sorting
		List<NestingBrick> nestingBrickList = new ArrayList<NestingBrick>();
		if (sorted) {
			nestingBrickList.add(ifBeginBrick);
			nestingBrickList.add(ifElseBrick);
			nestingBrickList.add(this);
		} else {
			nestingBrickList.add(this);
			nestingBrickList.add(ifBeginBrick);
			//nestingBrickList.add(ifElseBrick);
		}

		return nestingBrickList;
	}

	@Override
	public View getNoPuzzleView(Context context, int brickId, BaseAdapter adapter) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.brick_if_end_if, null);
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite, Script script) {
		IfLogicEndBrick copyBrick = (IfLogicEndBrick) clone(); //Using the clone method because of its flexibility if new fields are added
		ifBeginBrick.setIfEndBrick(this);
		ifElseBrick.setIfEndBrick(this);

		copyBrick.ifBeginBrick = null;
		copyBrick.ifElseBrick = null;
		copyBrick.sprite = sprite;
		return copyBrick;
	}
	@Override
	public String getScript() {
		return "}\n";
	}
}
