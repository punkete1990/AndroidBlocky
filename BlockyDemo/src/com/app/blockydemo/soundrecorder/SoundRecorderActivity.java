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
package com.app.blockydemo.soundrecorder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.blockydemo.R;
import com.app.blockydemo.common.Constants;
import com.app.blockydemo.ui.BaseActivity;
import com.app.blockydemo.utils.Utils;

import java.io.IOException;

public class SoundRecorderActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = SoundRecorderActivity.class.getSimpleName();
	private SoundRecorder soundRecorder;
	private Chronometer timeRecorderChronometer;
	private ImageButton recordButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View view) {
	}

	@Override
	public void onBackPressed() {
		stopRecording();
		super.onBackPressed();
	}

	private synchronized void startRecording() {
		if (soundRecorder != null && soundRecorder.isRecording()) {
			return;
		}
	}

	private void setViewsToRecordingState() {
	}

	private synchronized void stopRecording() {
		if (soundRecorder == null || !soundRecorder.isRecording()) {
			return;
		}
		setViewsToNotRecordingState();
		try {
			soundRecorder.stop();
			Uri uri = soundRecorder.getPath();
			setResult(Activity.RESULT_OK, new Intent(Intent.ACTION_PICK, uri));
		} catch (IOException e) {
			Log.e("CATROID", "Error recording sound.", e);
			setResult(Activity.RESULT_CANCELED);
		}
	}

	private void setViewsToNotRecordingState() {
	}

}
