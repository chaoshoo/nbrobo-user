package com.ningkangyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.zxing.client.android.CaptureActivity;
import com.ningkangyuan.R;
import com.ningkangyuan.okhttp.OkHttpHelper;
import com.ningkangyuan.push.BDPushReceiver;
import com.ningkangyuan.utils.JsonUtil;
import com.ningkangyuan.utils.LogUtil;
import com.ningkangyuan.utils.ToastUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xuchun on 2016/8/15.
 */
public class IdScanLoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "IdScanLoginActivity";

    private EditText mIdCardNumET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        init();
    }

    @Override
    protected View getView() {
        return mIdCardNumET;
    }

    @Override
    protected void init() {
        findViewById(R.id.universal_checkcard_num).setVisibility(View.GONE);
        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.scan_login, null));
        mIdCardNumET = (EditText) findViewById(R.id.scan_login_id_card_number);
        findViewById(R.id.scan_login_start).setOnClickListener(this);
        findViewById(R.id.scan_login_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "view id: " + v.getId());
        switch (v.getId()) {
            case R.id.scan_login_submit:
                //登录
                login();
                break;
            case R.id.scan_login_start:
                Log.d(TAG, "start capture intent ");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode)
        {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String idNum = bundle.getString("idNum");
                mIdCardNumET.setText(idNum);
                break;
            default:
                break;
        }
    }

    private void login() {
        if (checkParams()) {
            showProgressDialog("正在登录..");
            OkHttpHelper.get(OkHttpHelper.makeJsonParams("useridcardlogin",
                    new String[]{"idcard", "android_tv_channel_id"},
                    new Object[]{mIdCardNumET.getText(), BDPushReceiver.android_tv_channel_id}), new LoginCallback());
        }
    }

    //校验登录参数
    private boolean checkParams() {
        if (TextUtils.isEmpty(mIdCardNumET.getText().toString().trim())) {
            ToastUtil.show(this,"请扫描身份证");
            return false;
        }
        return true;
    }

    class LoginCallback implements Callback {
        @Override
        public void onFailure(Call call, final IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    String msg = e.getMessage();
                    if (msg.startsWith("Failed"))  {
                        msg = "无法连接服务器，请检查网络";
                    }
                    ToastUtil.show(IdScanLoginActivity.this, msg);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String result = response.body().string();
            LogUtil.d(TAG, result);
            final String code = JsonUtil.getObjectByKey("code", result);
            final String message= JsonUtil.getObjectByKey("message", result);
            final String vip_info= JsonUtil.getObjectByKey("vip_info", result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    ToastUtil.show(IdScanLoginActivity.this, message);
                    if ("1".equals(code)) {
                        //将登陆数据存起来
                        IdScanLoginActivity.this.getSharedPreferences("login",0).edit().putString("vip",vip_info).commit();

                        //跳转至主页面
                        Intent intent = new Intent(IdScanLoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
