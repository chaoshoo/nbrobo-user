package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Hos;

import java.util.List;

/**
 *  " + getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class HosAdapter extends RecyclerView.Adapter<HosAdapter.MyViewHolder> {

    private List<Hos> mHosList;

    public HosAdapter(List<Hos> hosList) {
        this.mHosList = hosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.universal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hos hos = mHosList.get(position);
        holder.mContentTV.setText("" + hos.getHosname());
    }

    @Override
    public int getItemCount() {
        return mHosList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mContentTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            mContentTV = (TextView) itemView.findViewById(R.id.universal_content);
        }
    }
}