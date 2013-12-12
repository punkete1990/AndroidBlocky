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

import java.util.List;

import com.app.blockydemo.content.Script;
import com.app.blockydemo.content.Sprite;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;


public abstract class MarketplaceBrick extends BrickBaseType implements AllowedAfterDeadEndBrick {

	private static final long serialVersionUID = 1L;

	public abstract Script initScript(Sprite sprite);

	@Override
	public View getNoPuzzleView(Context context, int brickId, BaseAdapter adapter) {
		return getView(context, brickId, adapter);
	}

	@Override
	public abstract Brick clone();
	
	public boolean containsDeadEnd() {
		for (Brick brick : getAllNestingBrickParts(false)) {
			if (brick instanceof DeadEndBrick) {
				return true;
			}
		}

		return false;
	}
	
	public abstract boolean isInitialized();

	public abstract void initialize();

	public abstract boolean isDraggableOver(Brick brick);
	
	/**
	 * 
	 * @return List of NestingBricks in order of their appearance
	 */
	public abstract List<Brick> getAllNestingBrickParts(boolean sorted);
}
