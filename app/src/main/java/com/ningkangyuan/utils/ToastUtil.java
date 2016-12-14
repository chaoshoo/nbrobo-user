package com.ningkangyuan.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xuchun on 2016/8/15.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void show(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

    /**
     *  " + getResources().getString(R.string.ToastUtil_java_4)Toast " + getResources().getString(R.string.ToastUtil_java_5)Context
     */
    public static void destory() {
        if (mToast != null) {
            mToast = null;
        }
    }
}