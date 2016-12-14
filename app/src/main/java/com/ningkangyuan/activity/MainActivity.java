package com.ningkangyuan.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;
import com.ningkangyuan.bean.DetectionKPI;
import com.ningkangyuan.bean.Order;
import com.ningkangyuan.bioland.DeviceControlActivity;
import com.ningkangyuan.kpi.Measure;
import com.ningkangyuan.okhttp.OkHttpHelper;
import com.ningkangyuan.storage.Shared;
import com.ningkangyuan.utils.JsonUtil;
import com.ningkangyuan.utils.LogUtil;
import com.ningkangyuan.utils.ToastUtil;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xuchun on 2016/8/15.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "MainActivity";

    private TextView mCheckCardTV;

    private ImageView mStatusIV;

//    private ImageView mUserPortraitIV;
    private TextView mCheckUserTV, mCheckTimeTV, mCheckTypeTV, mCheckValueTV;

    private TextView mRegistrationInfoTV, mReferenceTV;
    private ImageView mErWeiMaIV;

    private FrameLayout mDetectionKpiContentFL;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private MainUpView mMainUpView;

    private Timer mTimer = new Timer(true);
    private TimerTask mGetInfoTimerTask;
    private TimerTask mScanBluetoothTask;

    private int mRequestFailure = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        init();
    }

    @Override
    protected View getView() {
        return mCheckCardTV;
    }

    @Override
    protected void init() {
        mCheckCardTV = (TextView) findViewById(R.id.universal_checkcard_num);


        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.main,  null));
        mMainUpView = (MainUpView) findViewById(R.id.main_upview);
        mMainUpView.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge effectNoDrawBridge = (EffectNoDrawBridge) mMainUpView.getEffectBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mMainUpView.setUpRectResource(R.drawable.test_rectangle); //  " + getResources().getString(R.string.DeptActivity_java_8).
        mMainUpView.setShadowResource(R.drawable.item_shadow); //  " + getResources().getString(R.string.DeptActivity_java_9).

        mStatusIV = (ImageView) findViewById(R.id.main_status);

//        mUserPortraitIV = (ImageView) findViewById(R.id.main_user_protrait);
        mCheckUserTV = (TextView) findViewById(R.id.main_check_user);
        mCheckTimeTV = (TextView) findViewById(R.id.main_check_time);
        mCheckTypeTV = (TextView) findViewById(R.id.main_check_type);
        mCheckValueTV = (TextView) findViewById(R.id.main_check_value);

        mReferenceTV = (TextView) findViewById(R.id.main_reference);
        mRegistrationInfoTV = (TextView) findViewById(R.id.main_registration_info);
        findViewById(R.id.main_registration_info_layout).setOnClickListener(this);
        findViewById(R.id.main_registration_info_layout).setOnFocusChangeListener(this);
        mErWeiMaIV = (ImageView) findViewById(R.id.main_erweima);

        mDetectionKpiContentFL = (FrameLayout) findViewById(R.id.main_detection_kpi_content);

        findViewById(R.id.main_remote).setOnClickListener(this);
        findViewById(R.id.main_registration).setOnClickListener(this);
        findViewById(R.id.main_my).setOnClickListener(this);
        findViewById(R.id.main_children).setOnClickListener(this);
        findViewById(R.id.main_message).setOnClickListener(this);
        findViewById(R.id.main_history).setOnClickListener(this);
        findViewById(R.id.main_setting).setOnClickListener(this);
        findViewById(R.id.main_guide).setOnClickListener(this);

        findViewById(R.id.main_registration).setOnFocusChangeListener(this);
        findViewById(R.id.main_remote).setOnFocusChangeListener(this);
        findViewById(R.id.main_my).setOnFocusChangeListener(this);
        findViewById(R.id.main_children).setOnFocusChangeListener(this);
        findViewById(R.id.main_message).setOnFocusChangeListener(this);
        findViewById(R.id.main_history).setOnFocusChangeListener(this);
        findViewById(R.id.main_setting).setOnFocusChangeListener(this);
        findViewById(R.id.main_guide).setOnFocusChangeListener(this);

        findViewById(R.id.main_my).requestFocus();

        mCheckCardTV.setText(getResources().getString(R.string.DeptActivity_java_6) + mVip.getCard_code());
        String userName = mVip.getReal_name();
        if (TextUtils.isEmpty(userName)) {
            userName = mVip.getMobile();
        }
        mCheckUserTV.setText(userName);
//        ImageLoaderHelper.getInstance().loader(mVip.getHeard_img_url(),  mUserPortraitIV,  ImageLoaderHelper.makeImageOptions());

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            ToastUtil.show(this, getResources().getString(R.string.DeviceSelectAcitivity_java_10));
        }

        showProgressDialog(" " + getResources().getString(R.string.MainActivity_java_14) + " ..");
        qryOrder();
        getLastInfo();
    }

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device,  int rssi,  byte[] scanRecord) {
            String macName = device.getName();
            LogUtil.d(TAG, "macName：" + macName);
            if (macName != null && macName.startsWith("Bioland")) {
                mBluetoothAdapter.stopLeScan(leScanCallback);
//                showBluetoothScanDialog(device);
                Intent intent = new Intent(MainActivity.this,  DeviceControlActivity.class);
                intent.putExtra("DEVICE_NAME",  device.getName());
                intent.putExtra("DEVICE_ADDRESS",  device.getAddress());
                startActivityForResult(intent, 0);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode,  int resultCode,  Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                List<DetectionKPI> tempList = (List<DetectionKPI>) data.getSerializableExtra("data");
                showData(tempList);
            }
        }
    }

    private void excuteGetInfoTimer() {
        if (mGetInfoTimerTask == null) {
            mGetInfoTimerTask = new TimerTask() {
                @Override
                public void run() {
                    qryOrder();
                    getLastInfo();
                }
            };
        }
        mTimer.schedule(mGetInfoTimerTask, 30 * 60 * 1000, 38 * 60 * 1000);

        if (mScanBluetoothTask == null) {
            mScanBluetoothTask = new TimerTask() {
                @Override
                public void run() {
                    mBluetoothAdapter.startLeScan(leScanCallback);
                }
            };
        }
        mTimer.schedule(mScanBluetoothTask, 8 * 1000);
    }

    private void stopGetInfoTimer() {
        if (mGetInfoTimerTask != null) {
            mGetInfoTimerTask.cancel();
            mGetInfoTimerTask = null;
        }
        if (mScanBluetoothTask != null) {
            mScanBluetoothTask.cancel();
            mScanBluetoothTask = null;
        }
    }

//    private AlertDialog.Builder mBluetoothScanDialog;
//    private void showBluetoothScanDialog(final BluetoothDevice device) {
//        if (mBluetoothScanDialog == null) {
//            mBluetoothScanDialog = new AlertDialog.Builder(this);
//            mBluetoothScanDialog.setTitle(getResources().getString(R.string.FamilyActivity_java_91));
//            mBluetoothScanDialog.setPositiveButton(getResources().getString(R.string.MainActivity_java_28),  new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog,  int which) {
//                    Intent intent = new Intent(MainActivity.this,  DeviceControlActivity.class);
//                    intent.putExtra("DEVICE_NAME",  device.getName());
//                    intent.putExtra("DEVICE_ADDRESS",  device.getAddress());
//                    startActivity(intent);
//                }
//            });
//            mBluetoothScanDialog.setNegativeButton(getResources().getString(R.string.MainActivity_java_34),  new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog,  int which) {
//                    // " + getResources().getString(R.string.MainActivity_java_37)
//                    mBluetoothAdapter.startLeScan(leScanCallback);
//                }
//            });
//        }
//        String message = device.getName();
//        if (message.endsWith("BPM")) {
//            message = getResources().getString(R.string.DeviceSelectAcitivity_java_3);
//        } if (message.endsWith("BGM")) {
//            message = getResources().getString(R.string.DeviceSelectAcitivity_java_6);
//        }
//        mBluetoothScanDialog.setMessage(getResources().getString(R.string.MainActivity_java_40) + message + " " + getResources().getString(R.string.MainActivity_java_41)？");
//        mBluetoothScanDialog.show();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        excuteGetInfoTimer();
        if (MyApplication.isRefreshMain) {
            showProgressDialog(" " + getResources().getString(R.string.MainActivity_java_14) + " ..");
            mVip = Shared.getInstance().getLocalVip(this);
            mCheckCardTV.setText(getResources().getString(R.string.DeptActivity_java_6) + mVip.getCard_code());
            String userName = mVip.getReal_name();
            if (TextUtils.isEmpty(userName)) {
                userName = mVip.getMobile();
            }
            mCheckUserTV.setText(userName);
            qryOrder();
            getLastInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestFailure = 0;
        mBluetoothAdapter.stopLeScan(leScanCallback);
        stopGetInfoTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View v,  boolean hasFocus) {
        if (hasFocus) {
            mMainUpView.setFocusView(v, 1.0f);
        } else {
            mMainUpView.setUnFocusView(v);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_remote:
                // " + getResources().getString(R.string.MainActivity_java_46)
            {
                Intent intent = new Intent(this, RemoteActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_registration:
                // " + getResources().getString(R.string.MainActivity_java_48)
            {
                Intent intent = new Intent(this, HosActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_my:
                // " + getResources().getString(R.string.MainActivity_java_50)
            {
                Intent intent = new Intent(this, PersonalCenterActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_children:
                // " + getResources().getString(R.string.FamilyActivity_java_1)
            {
                Intent intent = new Intent(this, FamilyActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_message:
                // " + getResources().getString(R.string.MainActivity_java_54)
            {
                Intent intent = new Intent(this, MessageListActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_history:
                // " + getResources().getString(R.string.MainActivity_java_56)
            {
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_setting:
                // " + getResources().getString(R.string.MainActivity_java_58)
            {
//                ToastUtil.show(this, getResources().getString(R.string.MainActivity_java_60));
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_guide:
                // " + getResources().getString(R.string.GuideActivity_java_1)
//                ToastUtil.show(this, getResources().getString(R.string.MainActivity_java_60));
            {
                Intent intent = new Intent(this, GuideActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.main_registration_info_layout:
            {
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    // " + getResources().getString(R.string.MainActivity_java_67)
    private void qryOrder() {
        mCallList.add(OkHttpHelper.get(OkHttpHelper.makeJsonParams("getghorderlst",
                new String[]{"orderId", "status", "hosid", "vipcode", "docid", "deptid", "patientname", "pageIndex", "pageSize"},
                new Object[]{"", "", "", mVip.getVip_code(), "", "", "", 1,  1}),  new Callback() {
            @Override
            public void onFailure(Call call,  final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = e.getMessage();
                        if (msg.startsWith("Failed"))  {
                            msg = getResources().getString(R.string.BaseActivity_java_23);
                        }
                        ToastUtil.show(MainActivity.this,  msg);
                    }
                });
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                String result = response.body().string();
                LogUtil.d(TAG,  "onResponse：" + result);
                if ("1".equals(JsonUtil.getObjectByKey("code",  result))) {
                    String orders = JsonUtil.getObjectByKey("orders",  result);
                    final List<Order> tempList = JsonUtil.mGson.fromJson(orders,  new TypeToken<List<Order>>() {}.getType());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!tempList.isEmpty()) {
                                findViewById(R.id.main_no_reg).setVisibility(View.GONE);
                                Order order = tempList.get(0);
                                mRegistrationInfoTV.setText(" " + getResources().getString(R.string.MainActivity_java_95) + " ：" + order.getOutpdate() + "\n " + getResources().getString(R.string.MainActivity_java_96) + " ：" + order.getOrderid() + "\n " + getResources().getString(R.string.MainActivity_java_97) + " ：" + order.getOrderfee() + " " + getResources().getString(R.string.MainActivity_java_98) + "\n " + getResources().getString(R.string.MainActivity_java_99) + " ："
                                 + order.getHosname() + "\n " + getResources().getString(R.string.MainActivity_java_100) + " ：" + order.getDeptname() + "\n " + getResources().getString(R.string.MainActivity_java_101) + " ：" + order.getDocname());
                            } else {
                                findViewById(R.id.main_no_reg).setVisibility(View.VISIBLE);
                                mRegistrationInfoTV.setText(getResources().getString(R.string.MainActivity_java_102));
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.main_no_reg).setVisibility(View.VISIBLE);
                            mRegistrationInfoTV.setText(getResources().getString(R.string.MainActivity_java_102));
                        }
                    });
                }
            }
        }));
    }

    private void getLastInfo() {
        mCallList.add(OkHttpHelper.get(OkHttpHelper.makeJsonParams("recentmeasuredata",
                new String[]{"card_code"},
                new Object[]{mVip.getCard_code()}),  new Callback() {
            @Override
            public void onFailure(Call call,  final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRequestFailure ++;
                        if (mRequestFailure < 3) {
                            getLastInfo();
                            return;
                        }
                        dismissProgressDialog();
//                        showEmptyView();
//                        String msg = e.getMessage();
//                        if (msg.startsWith("Failed"))  {
//                            msg = getResources().getString(R.string.BaseActivity_java_23);
//                        }
                        ToastUtil.show(MainActivity.this,  getResources().getString(R.string.BaseActivity_java_23));
                    }
                });
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                String result = response.body().string();
                LogUtil.d(TAG, "onResponse：" + result);
                String flag = JsonUtil.getObjectByKey("flag", result);
                if ("success".equals(flag)) {
                    String array = JsonUtil.getObjectByKey("array", result);
                    final List<DetectionKPI> tempList = JsonUtil.mGson.fromJson(array, new TypeToken<List<DetectionKPI>>() {}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.isRefreshMain = false;
                            dismissProgressDialog();
                            showData(tempList);
                        }
                    });
                } else {
                    final String remark = JsonUtil.getObjectByKey("remark", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            showEmptyView();
                            ToastUtil.show(MainActivity.this, remark);
                        }
                    });
                }
            }
        }));
    }

    private void showData(List<DetectionKPI> detectionKPIs) {
        if (detectionKPIs == null || detectionKPIs.isEmpty()) {
            showEmptyView();
            return;
        }
        DetectionKPI detectionKPI = detectionKPIs.get(0);
        String inspect_code = detectionKPI.getInspect_code();
        if (Measure.XueTang.INSPECT_CODE.equals(inspect_code)) {
            showXueTang(detectionKPIs);
        } else if (Measure.XueYa.INSPECT_CODE.equals(inspect_code)) {
            showXueYa(detectionKPIs);
        } else if (Measure.XingTi.INSPECT_CODE.equals(inspect_code)) {
            showXingTi(detectionKPIs);
        } else {
            showEmptyView();
        }
    }

    private TextView mHighPressTV, mLowPressTV, mPulseRateTV;
    private View mXueYaView;
    private void showXueYa(List<DetectionKPI> detectionKPIList) {

        if (mXueYaView == null) {
            mXueYaView = LayoutInflater.from(this).inflate(R.layout.main_xueya, null);
            mHighPressTV = (TextView) mXueYaView.findViewById(R.id.main_xueya_highpress);
            mLowPressTV = (TextView) mXueYaView.findViewById(R.id.main_xueya_lowpress);
            mPulseRateTV = (TextView) mXueYaView.findViewById(R.id.main_xueya_pulserate);
        }
        mDetectionKpiContentFL.removeAllViews();
        mDetectionKpiContentFL.addView(mXueYaView);

        for(DetectionKPI detectionKPI : detectionKPIList) {
            if (Measure.XueYa.CODE_PR.equals(detectionKPI.getKpi_code())) {
                mPulseRateTV.setText(detectionKPI.getInspect_value());
            } else if (Measure.XueYa.CODE_SYS.equals(detectionKPI.getKpi_code())) {
                mHighPressTV.setText(detectionKPI.getInspect_value());
            } else if (Measure.XueYa.CODE_DIA.equals(detectionKPI.getKpi_code())) {
                mLowPressTV.setText(detectionKPI.getInspect_value());
            }
        }
        showCheckInfo(detectionKPIList);
    }

    private TextView mValueTV, mTypeTV;
    private View mXueTangView;
    private void showXueTang(List<DetectionKPI> detectionKPIList) {
        if (mXueTangView == null) {
            mXueTangView = LayoutInflater.from(this).inflate(R.layout.main_xuetang, null);
            mTypeTV = (TextView) mXueTangView.findViewById(R.id.main_xuetang_type);;
            mValueTV = (TextView) mXueTangView.findViewById(R.id.main_xuetang_value);
        }
        mDetectionKpiContentFL.removeAllViews();
        mDetectionKpiContentFL.addView(mXueTangView);

        for(DetectionKPI detectionKPI : detectionKPIList) {
            String str = "";
            if (Measure.XueTang.CODE_GLU0.equals(detectionKPI.getKpi_code())) {
                str = getResources().getString(R.string.MainActivity_java_123);
            } else if (Measure.XueTang.CODE_GLU1.equals(detectionKPI.getKpi_code())) {
                str = getResources().getString(R.string.MainActivity_java_124);
            } else if (Measure.XueTang.CODE_GLU2.equals(detectionKPI.getKpi_code())) {
                str = getResources().getString(R.string.MainActivity_java_125);
            }
            mTypeTV.setText(str);
            mValueTV.setText(detectionKPI.getInspect_value());
        }
        showCheckInfo(detectionKPIList);
    }

    private TextView mHeightTV, mWeightTV, mBMITV;
    private View mXingTiView;
    private void showXingTi(List<DetectionKPI> detectionKPIList) {

        if (mXingTiView == null) {
            mXingTiView = LayoutInflater.from(this).inflate(R.layout.main_xingti, null);
            mHeightTV = (TextView) mXingTiView.findViewById(R.id.main_xingti_height);
            mWeightTV = (TextView) mXingTiView.findViewById(R.id.main_xingti_weight);
            mBMITV = (TextView) mXingTiView.findViewById(R.id.main_xingti_bmi);
        }
        mDetectionKpiContentFL.removeAllViews();
        mDetectionKpiContentFL.addView(mXingTiView);

        for(DetectionKPI detectionKPI : detectionKPIList) {
            if (Measure.XingTi.HEIGHT.equals(detectionKPI.getKpi_code())) {
                mHeightTV.setText(detectionKPI.getInspect_value());
            } else if (Measure.XingTi.WEIGHT.equals(detectionKPI.getKpi_code())) {
                mWeightTV.setText(detectionKPI.getInspect_value());
            } else if (Measure.XingTi.BMI.equals(detectionKPI.getKpi_code())) {
                mBMITV.setText(detectionKPI.getInspect_value());
            }
        }
        showCheckInfo(detectionKPIList);
    }

    private void showCheckInfo(List<DetectionKPI> detectionKPIList) {
        int status = 0;
        for (int i = 0; i < detectionKPIList.size();i ++) {
            if (!"0".equals(detectionKPIList.get(i).getInspect_is_normal())) {
                status = Integer.valueOf(detectionKPIList.get(i).getInspect_is_normal());
                break;
            }
        }
        if (status == -1) {
            if (getResources().getConfiguration().locale.getCountry().equals("CN") ) {
                status = R.drawable.main_check_status_low_cn;
            } else {
                status = R.drawable.main_check_status_low;
            }
        } else if (status == 0) {
            if (getResources().getConfiguration().locale.getCountry().equals("CN") ) {
                status = R.drawable.main_check_status_normal_cn;
            } else {
                status = R.drawable.main_check_status_normal;
            }
        } else if (status == 1) {
            if (getResources().getConfiguration().locale.getCountry().equals("CN") ) {
                status = R.drawable.main_check_status_high_cn;
            } else {
                status = R.drawable.main_check_status_high;
            }
        }
        mStatusIV.setImageResource(status);

        String desc = " " + getResources().getString(R.string.MainActivity_java_129) + "  ";
        for (int i = 0; i < detectionKPIList.size();i ++) {
            desc += detectionKPIList.get(i).getInspect_desc();
        }
        mReferenceTV.setText(desc);

        DetectionKPI detectionKPI = detectionKPIList.get(0);
        String inspect_time = detectionKPI.getInspect_time();
        mCheckTimeTV.setText(" " + getResources().getString(R.string.MainActivity_java_130) + " ：" + inspect_time);
        String type = "";
        String value = "";
        if (Measure.XueTang.INSPECT_CODE.equals(detectionKPI.getInspect_code())) {
            type = getResources().getString(R.string.DeviceSelectAcitivity_java_6);
            value = detectionKPI.getInspect_value();
        } else if (Measure.XueYa.INSPECT_CODE.equals(detectionKPI.getInspect_code())) {
            type = getResources().getString(R.string.DeviceSelectAcitivity_java_3);
            value = mHighPressTV.getText() + "/" + mLowPressTV.getText();
        } else if (Measure.XingTi.INSPECT_CODE.equals(detectionKPI.getInspect_code())) {
            type = getResources().getString(R.string.MainActivity_java_133);
            value = mHeightTV.getText() + "/" + mWeightTV.getText();
        }
        mCheckTypeTV.setText(type);
        mCheckValueTV.setText(value);
    }

    private View mEmptyView;
    private void showEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.main_xueya, null);
        }
        mDetectionKpiContentFL.removeAllViews();
        mDetectionKpiContentFL.addView(mEmptyView);

        mCheckTimeTV.setText(" " + getResources().getString(R.string.MainActivity_java_135) + "：--");
        mCheckTypeTV.setText(getResources().getString(R.string.DeviceSelectAcitivity_java_3));
        mCheckValueTV.setText("--");
        mReferenceTV.setText("");
    }

}