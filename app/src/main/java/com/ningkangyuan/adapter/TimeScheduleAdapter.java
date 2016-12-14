package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Schedule;
import com.ningkangyuan.bean.TimeSchedule;
import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;

import java.util.List;

/**
 *  " + MyApplication.mContext.getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class TimeScheduleAdapter extends RecyclerView.Adapter<TimeScheduleAdapter.MyViewHolder> {

    private List<TimeSchedule> mTimeScheduleList;

    public TimeScheduleAdapter(List<TimeSchedule> timeScheduleList) {
        this.mTimeScheduleList = timeScheduleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.time_schedule_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TimeSchedule timeSchedule = mTimeScheduleList.get(position);
        holder.mBTimeTV.setText(" " + MyApplication.mContext.getResources().getString(R.string.TimeScheduleAdapter_java_6) + " ：" + timeSchedule.getBstp());
        holder.mETimeTV.setText(" " + MyApplication.mContext.getResources().getString(R.string.TimeScheduleAdapter_java_7) + " ：" + timeSchedule.getEstp());
        String regflag = timeSchedule.getRegflag();
        if ("1".equals(regflag)) {
            regflag = MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_9);
        } else {
            regflag = MyApplication.mContext.getResources().getString(R.string.ScheduleAdapter_java_6);
        }
        holder.mFlagTV.setText(regflag);
    }

    @Override
    public int getItemCount() {
        return mTimeScheduleList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mBTimeTV,mETimeTV,mFlagTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            mBTimeTV = (TextView) itemView.findViewById(R.id.time_schedule_item_btime);
            mETimeTV = (TextView) itemView.findViewById(R.id.time_schedule_item_etime);
            mFlagTV = (TextView) itemView.findViewById(R.id.time_schedule_item_flag);
        }
    }
}