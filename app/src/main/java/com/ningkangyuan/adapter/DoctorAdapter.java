package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Dept;
import com.ningkangyuan.bean.Doctor;

import java.util.List;

/**
 *  " + getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    private List<Doctor> mDoctorList;

    public DoctorAdapter(List<Doctor> doctorList) {
        this.mDoctorList = doctorList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.doctor_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Doctor doctor = mDoctorList.get(position);
        holder.name.setText(doctor.getName());  // " + getResources().getString(R.string.DoctorAdapter_java_6)
        holder.job.setText(doctor.getTitle());  // " + getResources().getString(R.string.DoctorAdapter_java_7)
        holder.history.setText(doctor.getEdu());    // " + getResources().getString(R.string.DoctorAdapter_java_8)
        holder.intro.setText("  " + doctor.getInfo()); // " + getResources().getString(R.string.DoctorAdapter_java_9)
    }

    @Override
    public int getItemCount() {
        return mDoctorList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView protrait;
        public TextView name,job,history,intro;

        public MyViewHolder(View itemView) {
            super(itemView);
            protrait = (ImageView) itemView.findViewById(R.id.doctor_item_protrait);
            name = (TextView) itemView.findViewById(R.id.doctor_item_name);
            job = (TextView) itemView.findViewById(R.id.doctor_item_job);
            history = (TextView) itemView.findViewById(R.id.doctor_item_history);
            intro = (TextView) itemView.findViewById(R.id.doctor_item_intro);
        }
    }
}