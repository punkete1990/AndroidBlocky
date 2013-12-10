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
package com.app.blockydemo.content.actions;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import com.app.blockydemo.content.Sprite;
import com.app.blockydemo.content.bricks.LegoNxtMotorActionBrick.Motor;
import com.app.blockydemo.formulaeditor.Formula;

public class LegoNxtMotorActionAction extends TemporalAction {
	private static final int MIN_SPEED = -100;
	private static final int MAX_SPEED = 100;
	private static final int NO_DELAY = 0;

	private Motor motorEnum;
	private Formula speed;
	private Sprite sprite;

	@Override
	protected void update(float percent) {
		int speedValue = speed.interpretInteger(sprite);
		if (speedValue < MIN_SPEED) {
			speedValue = MIN_SPEED;
		} else if (speedValue > MAX_SPEED) {
			speedValue = MAX_SPEED;
		}

		//LegoNXT.sendBTCMotorMessage((int) (duration * 1000), motor, 0, 0);

	}

	public void setMotorEnum(Motor motorEnum) {
		this.motorEnum = motorEnum;
	}

	public void setSpeed(Formula speed) {
		this.speed = speed;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}