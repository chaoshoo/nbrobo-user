package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Doc;
import com.ningkangyuan.bean.Schedule;

import java.util.List;

import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;
/**
 *  " + MyApplication.mContext.getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private List<Schedule> mScheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.mScheduleList = scheduleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.schedule_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Schedule schedule = mScheduleList.get(position);
        holder.mTimeTV.setText(schedule.getOutpdate() + "(" + schedule.getTimeinterval() + ")");
        holder.mOrderFeeTV.setText("¥ " + schedule.getOrderfee());
        String validFlag = schedule.getValidflag();
        String hitStr = "";
        String flagStr = MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_6);
        if ("1".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_7) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_8) + "）";
            flagStr = MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_9);
        } else if ("2".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_10) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_11) + "）";
        } else if ("3".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_12) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_13) + "）";
        } else if ("4".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_14) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_15) + " ";
        } else if ("5".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_16) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_17) + "）";
        } else if ("6".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_18) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_19) + "）";
        } else if ("7".equals(validFlag)) {
            hitStr = " " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_20) + "（ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_7) + "/ " + MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_22) + " ";
            flagStr = MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_9);
        }
        holder.mHintTV.setText(hitStr);
        holder.mFlagTV.setText(flagStr);
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTimeTV,mOrderFeeTV,mHintTV,mFlagTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTimeTV = (TextView) itemView.findViewById(R.id.schedule_item_time);
            mOrderFeeTV = (TextView) itemView.findViewById(R.id.schedule_item_orderfee);
            mHintTV = (TextView) itemView.findViewById(R.id.schedule_item_hint);
            mFlagTV = (TextView) itemView.findViewById(R.id.schedule_item_flag);
        }
    }
}