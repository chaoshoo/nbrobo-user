package com.ningkangyuan.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.ningkangyuan.MyApplication;
import com.ningkangyuan.bean.Vip;
import com.ningkangyuan.utils.JsonUtil;

/**
 * Created by xuchun on 2016/8/23.
 */
public class Shared {

    private static Shared mShared;

    public static synchronized Shared getInstance() {
        if (mShared == null) {
            mShared = new Shared();
        }
        return mShared;
    }

    /**
     *  " + getResources().getString(R.string.Shared_java_1)vip
     * @param context
     * @return
     */
    public Vip getLocalVip(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        String vip = sp.getString("vip",null);
        if (vip == null) {
            return null;
        }
        return JsonUtil.mGson.fromJson(vip,Vip.class);
    }

    /**
     *  " + getResources().getString(R.string.Shared_java_5)
     * @param context
     * @return
     */
    public String getIdentityStr(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        return sp.getString("identityStr", null);
    }

    /**
     *  " + getResources().getString(R.string.Shared_java_8)
     * @param context
     * @return
     */
    public String getPasswordStr(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        return sp.getString("passwordStr",null);
    }

    /**
     * 得到登录的密码
     * @param context
     * @return
     */
    public String getPushMsg(Context context) {
        SharedPreferences sp = context.getSharedPreferences("push", 0);
        return sp.getString("msg",null);
    }

}