package com.nbrobo.scan.comparison;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.nbrobo.scan.common.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.renderscript.Type;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import io.github.silvaren.easyrs.tools.Nv21Image;

public class ComparisionExecutor {
	
	private static final String TAG = "ComparisionExecutor";

	private static ComparisionExecutor instance;

	private TessBaseAPI baseApi;
    private RenderScript rs;
	
	private ComparisionExecutor(Context context) {
        rs = RenderScript.create(context);
	}
	
	public static synchronized ComparisionExecutor getInstance(Context context) {
		if (null == instance) {
			instance = new ComparisionExecutor(context);
		}
		return instance;
	}
	
	public synchronized String execute(byte[] data, int width, int height) {
		baseApi=new TessBaseAPI();
		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		baseApi.init(sdPath, "eng");
        Bitmap bitmap = Nv21Image.nv21ToBitmap(rs, data, width, height);
		int destWidth = width > 1200 ? 1200 : width;
		int destHeight = height > destWidth / 5 ? destWidth / 5 : height;
		Bitmap  resultMap = Bitmap.createBitmap(bitmap, (width  - destWidth) / 2, (height - destHeight) / 2, destWidth, destHeight);
        //设置要ocr的图片bitmap
        baseApi.setImage(resultMap);
        //根据Init的语言，获得ocr后的字符串
		Log.d(TAG, "start tess ocr. width: " + width + " height: " + height + " data length: " + data.length);
        String text= baseApi.getUTF8Text();
		Log.d(TAG, "----------end tess ocr, result: "+ text);
        //释放bitmap
        baseApi.clear();

        //如果连续ocr多张图片，这个end可以不调用，但每次ocr之后，必须调用clear来对bitmap进行释放
        //释放native内存
        baseApi.end();

        return text;
	}

}
