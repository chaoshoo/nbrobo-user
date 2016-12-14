package com.ningkangyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ningkangyuan.R;

/**
 *  " + getResources().getString(R.string.MainActivity_java_50)
 * Created by xuchun on 2016/8/22.
 */
public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        init();
    }

    @Override
    protected View getView() {
        return mTextView;
    }

    @Override
    protected void init() {
        mTextView = (TextView) findViewById(R.id.universal_checkcard_num);
        mTextView.setText(" " + getResources().getString(R.string.DeptActivity_java_6) + " ：" + mVip.getCard_code());
        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.personal_center,  null));

        findViewById(R.id.personal_center_remote).setOnClickListener(this);
        findViewById(R.id.personal_center_registration).setOnClickListener(this);
        findViewById(R.id.personal_center_consult).setOnClickListener(this);
        findViewById(R.id.personal_center_back).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_center_remote:
                // " + getResources().getString(R.string.PersonalCenterActivity_java_4)
            {
                Intent intent = new Intent(this, RemoteHistoryActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.personal_center_registration:
                // " + getResources().getString(R.string.MainActivity_java_48)
            {
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.personal_center_consult:
                // " + getResources().getString(R.string.PersonalCenterActivity_java_8)
            {
                Intent intent = new Intent(this, QuestionHistoryActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.personal_center_back:
                // " + getResources().getString(R.string.DoctorActivity_java_40)
                finish();
                break;

        }
    }
}