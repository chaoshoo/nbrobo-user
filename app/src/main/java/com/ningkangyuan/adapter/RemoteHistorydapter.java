package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Dept;
import com.ningkangyuan.bean.RemoteHistory;

import java.util.List;

import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;
/**
 *  " + MyApplication.mContext.getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class RemoteHistorydapter extends RecyclerView.Adapter<RemoteHistorydapter.MyViewHolder> {

    private List<RemoteHistory> mRemoteHistoryList;

    public RemoteHistorydapter(List<RemoteHistory> remoteHistoryList) {
        this.mRemoteHistoryList = remoteHistoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.remote_history_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RemoteHistory remoteHistory = mRemoteHistoryList.get(position);
        holder.doctor.setText(" " + MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_6) + " ：" + remoteHistory.getName() + "\n " + MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_7) + " ：" + remoteHistory.getOrder_time());
        holder.remark.setText(" " + MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_8) + "：\n" + remoteHistory.getRemark());
        String isZd = remoteHistory.getIszd();
        if ("1".equals(isZd)) {
            isZd = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_9);
        } else if ("2".equals(isZd)) {
            isZd = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_10);
        } else {
            isZd = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_11);
        }
        String isDeal = remoteHistory.getIsdeal();
        if ("1".equals(isDeal)) {
            isDeal = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_12);
        } else if ("2".equals(isDeal)) {
            isDeal = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_10);
        } else {
            isDeal = MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_11);
        }
        holder.status.setText(" " + MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_15) + " ：" + isZd + "\n " + MyApplication.mContext.getResources().getString(R.string.RemoteHistorydapter_java_16) + " ：" + isDeal);
    }

    @Override
    public int getItemCount() {
        return mRemoteHistoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView doctor,remark,status;

        public MyViewHolder(View itemView) {
            super(itemView);
            doctor = (TextView) itemView.findViewById(R.id.remote_item_doctor);
            remark = (TextView) itemView.findViewById(R.id.remote_item_remark);
            status = (TextView) itemView.findViewById(R.id.remote_item_status);
        }
    }
}