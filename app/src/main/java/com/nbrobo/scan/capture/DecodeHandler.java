/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nbrobo.scan.capture;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ningkangyuan.R;
import com.nbrobo.scan.comparison.ComparisionExecutor;
import com.nbrobo.scan.comparison.Result;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CaptureActivity activity;
	private ComparisionExecutor comparisionExecutor;
	private boolean running = true;

	DecodeHandler(CaptureActivity activity) {
		this.activity = activity;
		try {
			this.comparisionExecutor = ComparisionExecutor.getInstance(activity);
		} catch (Exception e) {
			this.comparisionExecutor = null;
			Log.e(TAG, e.getLocalizedMessage());
		}
	}

	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}
		switch (message.what) {
		case R.id.decode:
			decode((byte[]) message.obj, message.arg1, message.arg2);
			break;
		case R.id.quit:
			running = false;
			Looper.myLooper().quit();
			break;
		}
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 *
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		Handler handler = activity.getHandler();
		if (null == this.comparisionExecutor) {
			Message message = Message.obtain(handler, R.id.decode_failed);
			message.sendToTarget();
		}
		String result = this.comparisionExecutor.execute(data, width, height);
		if (result != null) {
			if (handler != null) {
				String reg = "^(\\d{14}|\\d{17})([0-9]|X|x)$";
				Pattern pattern = Pattern.compile(reg);
				Matcher m = pattern.matcher(result);
				while (m.find()) {
					Message message = Message.obtain(handler, R.id.decode_succeeded, result);
					Bundle bundle = new Bundle();
					message.setData(bundle);
					message.sendToTarget();
					return;
				}
				Message message = Message.obtain(handler, R.id.decode_failed);
				message.sendToTarget();
				return;
			}
		} else {
			if (handler != null) {
				Message message = Message.obtain(handler, R.id.decode_failed);
				message.sendToTarget();
			}
		}
	}

}
