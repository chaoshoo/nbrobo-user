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

package com.google.zxing.client.android;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;


import com.googlecode.tesseract.android.TessBaseAPI;
import com.ningkangyuan.R;
import com.ningkangyuan.activity.InitActivity;
import com.ningkangyuan.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.silvaren.easyrs.tools.Nv21Image;


final class DecodeHandler extends Handler {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private boolean running = true;

  private TessBaseAPI baseApi;
  private RenderScript rs;
 // private ImageProcess imageProcess;

  private List<String> resultList;

  DecodeHandler(CaptureActivity activity) {
    this.activity = activity;
    rs = RenderScript.create(activity);
    this.resultList = new ArrayList<>();
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

  private String checkResult(String result) {
    if (this.resultList.isEmpty()) {
      this.resultList.add(result);
    } else if (result.length() == this.resultList.get(0).length()) {
      this.resultList.add(result);
    } else {
      return null;
    }
    if (this.resultList.size() < 5) {
      return null;
    }
    int length = result.length();
    StringBuilder sb = new StringBuilder();
    for (int index = 0; index < length; index++) {
      Map<String, Integer> map = new HashMap<>();
      for (int resultIndex = 0; resultIndex < this.resultList.size(); resultIndex++) {
        char c = this.resultList.get(resultIndex).charAt(index);
        String s = String.valueOf(c);
        Integer count = map.get(s);
        if (count == null) {
          count = 0;
        } else {
          count ++;
        }
        map.put(s, count);
      }
      Integer count = 0;
      String s = "";
      Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry<String, Integer> entry = iter.next();
        String key = entry.getKey();
        Integer value = entry.getValue();
        if (value.equals(count)) {
          this.resultList.clear();
          return null;
        }
        if (value > count) {
          s = key;
          count = value;
        }
      }
      sb.append(s);
    }
    this.resultList.clear();
    return sb.toString();
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   *
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
  private void decode(byte[] data, int width, int height) {
    Handler handler = activity.getHandler();

    String result = this.execute(data, width, height);
    if (result != null) {
      if (handler != null) {
        String reg = "^(\\d{14}|\\d{17})([0-9]|X|x)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(result);
        while (m.find()) {
          result = this.checkResult(result);
          if (null == result) {
            Message message = Message.obtain(handler, R.id.decode_failed);
            message.sendToTarget();
            return;
          }
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

  private String execute(byte[] data, int width, int height) {
    String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    baseApi=new TessBaseAPI();
    baseApi.init(sdPath, InitActivity.TRAINED_DATA_NAME_PREFIX);
    Bitmap bitmap = Nv21Image.nv21ToBitmap(rs, data, width, height);

    int destWidth = width / 2;
    int destHeight = height / 2;
    if (destHeight > destWidth / 5) {
      destHeight = destWidth / 5;
    }

    Bitmap  resultMap = Bitmap.createBitmap(bitmap, (width  - destWidth) / 2, (height - destHeight) / 2, destWidth, destHeight);
    resultMap = scaleBitmap(resultMap, 4f, 4f);
    resultMap = binarization(resultMap);
//    Bitmap laplaceBitmap = imageProcess.LaplaceGradient(resultMap);
    //设置要ocr的图片bitmap
//    Bitmap convertedBitmap = convertBmp(resultMap);
//    String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/Bitmap["+
//            android.os.SystemClock.uptimeMillis() + "].png";
//    String lpath = Environment.getExternalStorageDirectory().toString()+"/DCIM/Bitmap["+
//            android.os.SystemClock.uptimeMillis() + "]l.png";
//    dumpBitmap(resultMap, path);
//    ToastUtil.show(this.activity, String.valueOf(new File(path).getParentFile().listFiles().length));
//    dumpBitmap(laplaceBitmap, lpath);

    baseApi.setImage(resultMap);
    //根据Init的语言，获得ocr后的字符串
    //Log.d(TAG, "start tess ocr. width: " + destWidth + " height: " + destHeight + " data length: " + data.length);
    String text= baseApi.getUTF8Text();
    //ToastUtil.show(this.activity, text);
    //Log.d(TAG, "----------end tess ocr, result: "+ text);
    //释放bitmap
    baseApi.clear();

    //如果连续ocr多张图片，这个end可以不调用，但每次ocr之后，必须调用clear来对bitmap进行释放
    //释放native内存
    baseApi.end();
    return text;
  }

  private Bitmap convertBmp(Bitmap bmp){
    int w = bmp.getWidth();
    int h = bmp.getHeight();

    Bitmap convertBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
    Canvas cv = new Canvas(convertBmp);
    Matrix matrix = new Matrix();
//  matrix.postScale(1, -1); //镜像垂直翻转
    matrix.postScale(-1, 1); //镜像水平翻转
//  matrix.postRotate(-90); //旋转-90度

    Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
    cv.drawBitmap(newBmp, new Rect(0, 0,newBmp.getWidth(), newBmp.getHeight()),new Rect(0, 0, w, h), null);
    return convertBmp;
  }

  private void dumpBitmap(Bitmap bitmap, String path) {
    File file = new File(path);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
      FileOutputStream fos = null;
      try {
        fos = new java.io.FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
      } catch (java.io.IOException ex) {
        // MINI_THUMBNAIL not exists, ignore the exception and generate one.
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (java.io.IOException ex) {
            ex.printStackTrace();;
          }
        }
      }
  }

  private Bitmap scaleBitmap(Bitmap bitmap, float widthScale, float heithtScale) {
    Matrix matrix = new Matrix();
    matrix.postScale(widthScale, heithtScale); //长和宽放大缩小的比例
    Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    return resizeBmp;
  }

  public Bitmap binarization(Bitmap img) {
    int width = img.getWidth();
    int height = img.getHeight();
    int area = width * height;
    int gray[][] = new int[width][height];
    int average = 0;// 灰度平均值
    int graysum = 0;
    int graymean = 0;
    int grayfrontmean = 0;
    int graybackmean = 0;
    int pixelGray;
    int front = 0;
    int back = 0;
    int[] pix = new int[width * height];
    img.getPixels(pix, 0, width, 0, 0, width, height);
    for (int i = 1; i < width; i++) { // 不算边界行和列，为避免越界
      for (int j = 1; j < height; j++) {
        int x = j * width + i;
        int r = (pix[x] >> 16) & 0xff;
        int g = (pix[x] >> 8) & 0xff;
        int b = pix[x] & 0xff;
        pixelGray = (int) (0.3 * r + 0.59 * g + 0.11 * b);// 计算每个坐标点的灰度
        gray[i][j] = (pixelGray << 16) + (pixelGray << 8) + (pixelGray);
        graysum += pixelGray;
      }
    }
    graymean = (int) (graysum / area);// 整个图的灰度平均值
    average = graymean;
    Log.i(TAG,"Average:"+average);
    for (int i = 0; i < width; i++) // 计算整个图的二值化阈值
    {
      for (int j = 0; j < height; j++) {
        if (((gray[i][j]) & (0x0000ff)) < graymean) {
          graybackmean += ((gray[i][j]) & (0x0000ff));
          back++;
        } else {
          grayfrontmean += ((gray[i][j]) & (0x0000ff));
          front++;
        }
      }
    }
    int frontvalue = (int) (grayfrontmean / front);// 前景中心
    int backvalue = (int) (graybackmean / back);// 背景中心
    float G[] = new float[frontvalue - backvalue + 1];// 方差数组
    int s = 0;
    Log.i(TAG,"Front:"+front+"**Frontvalue:"+frontvalue+"**Backvalue:"+backvalue);
    for (int i1 = backvalue; i1 < frontvalue + 1; i1++)// 以前景中心和背景中心为区间采用大津法算法（OTSU算法）
    {
      back = 0;
      front = 0;
      grayfrontmean = 0;
      graybackmean = 0;
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (((gray[i][j]) & (0x0000ff)) < (i1 + 1)) {
            graybackmean += ((gray[i][j]) & (0x0000ff));
            back++;
          } else {
            grayfrontmean += ((gray[i][j]) & (0x0000ff));
            front++;
          }
        }
      }
      grayfrontmean = (int) (grayfrontmean / front);
      graybackmean = (int) (graybackmean / back);
      G[s] = (((float) back / area) * (graybackmean - average)
              * (graybackmean - average) + ((float) front / area)
              * (grayfrontmean - average) * (grayfrontmean - average));
      s++;
    }
    float max = G[0];
    int index = 0;
    for (int i = 1; i < frontvalue - backvalue + 1; i++) {
      if (max < G[i]) {
        max = G[i];
        index = i;
      }
    }
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int in = j * width + i;
        if (((gray[i][j]) & (0x0000ff)) < (index + backvalue)) {
          pix[in] = Color.rgb(0, 0, 0);
        } else {
          pix[in] = Color.rgb(255, 255, 255);
        }
      }
    }

    Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    temp.setPixels(pix, 0, width, 0, 0, width, height);
    return temp;
  }

}
