package com.ningkangyuan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

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
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mIdentityET,mPhoneET,mPasswordET;
    private String mIdentityStr,mPhoneStr,mPasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isLoadVip = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        init();
    }

    @Override
    protected View getView() {
        return mIdentityET;
    }

    @Override
    protected void init() {

        findViewById(R.id.universal_checkcard_num).setVisibility(View.GONE);

        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.login, null));

        mIdentityET = (EditText) findViewById(R.id.login_identity);
//        mPhoneET = (EditText) findViewById(R.id.login_phone);
        mPasswordET = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.login_submit).setOnClickListener(this);
        findViewById(R.id.regist_idcard_scan).setOnClickListener(this);
        TextView registerBtn = (TextView) findViewById(R.id.login_register);
        registerBtn.setOnClickListener(this);
        registerBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        registerBtn.getPaint().setAntiAlias(true);

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "view id: " + v.getId());
        switch (v.getId()) {
            case R.id.login_submit:
                //登录
                login();
                break;
            case R.id.login_register:
                Log.d(TAG, "start regist intent ");
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.regist_idcard_scan:
                Log.d(TAG, "start capture intent ");
                startActivity(new Intent(this, IdScanLoginActivity.class));
                break;
        }
    }

    private void login() {
        if (checkParams()) {
            showProgressDialog("正在登录..");

            OkHttpHelper.get(OkHttpHelper.makeJsonParams("userlogin",
                    new String[]{"num","password","android_tv_channel_id"},
                    new Object[]{mIdentityStr,mPasswordStr, BDPushReceiver.android_tv_channel_id}), new LoginCallback());
        }
    }

    //校验登录参数
    private boolean checkParams() {
        mIdentityStr = mIdentityET.getText().toString().trim();
        if (TextUtils.isEmpty(mIdentityStr)) {
            ToastUtil.show(this,"请输入身份证或者手机号");
            return false;
        }
        mPasswordStr = mPasswordET.getText().toString().trim();
        if (TextUtils.isEmpty(mPasswordStr)) {
            mPasswordET.setHintTextColor(Color.RED);
            return false;
        }
        return true;
    }

    class LoginCallback implements  Callback {
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
                    ToastUtil.show(LoginActivity.this, msg);
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
                    ToastUtil.show(LoginActivity.this, message);
                    if ("1".equals(code)) {
                        //将登陆数据存起来
                        LoginActivity.this.getSharedPreferences("login",0).edit().putString("vip",vip_info).commit();

                        //保存当前登录的账号和密码
                        LoginActivity.this.getSharedPreferences("login",0).edit().putString("identityStr",mIdentityStr).commit();
                        LoginActivity.this.getSharedPreferences("login",0).edit().putString("passwordStr", mPasswordStr).commit();

                        //跳转至主页面
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
