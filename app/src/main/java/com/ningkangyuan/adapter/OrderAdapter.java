package com.ningkangyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningkangyuan.R;
import com.ningkangyuan.bean.Hos;
import com.ningkangyuan.bean.Order;
import com.ningkangyuan.utils.LogUtil;
import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  " + MyApplication.mContext.getResources().getString(R.string.HosActivity_java_1)
 * Created by xuchun on 2016/9/21.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private static final String TAG = "OrderAdapter";

    private List<Order> mOrderList;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OrderAdapter(List<Order> orderList) {
        this.mOrderList = orderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.mOrderNumTV.setText(" " + MyApplication.mContext.getResources().getString(R.string.MainActivity_java_95) + " ：" + order.getOutpdate() + "\n " + MyApplication.mContext.getResources().getString(R.string.OrderActivity_java_77) + " ：" + order.getOrderid());
        String content = " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_8) + " ：" + order.getOrderfee() + MyApplication.mContext.getResources().getString(R.string.MainActivity_java_98);
        String status = "";
        String orderTime = order.getCreate_time();
        String payTime = order.getPayrtime();
        String cancelTime = order.getCanceltime();
        try {
//            if (!TextUtils.isEmpty(order.getOrdertime())) {
//                orderTime = mSimpleDateFormat.format(new Date(Long.valueOf(order.getOrdertime())));
//            }
//            if (!TextUtils.isEmpty(order.getPayrtime())) {
//                payTime = mSimpleDateFormat.format(new Date(Long.valueOf(order.getPayrtime())));
//            }
//            if (!TextUtils.isEmpty(order.getCanceltime())) {
//                cancelTime = mSimpleDateFormat.format(new Date(Long.valueOf(order.getCanceltime())));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("1".equals(order.getStatus())) {
            holder.mOrderStatusTV.setText(MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_10));
        } else if ("2".equals(order.getStatus())) {
            content += "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_11) + " ：" + orderTime;
            status = MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_12);
        } else if ("3".equals(order.getStatus())) {
            content += "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_11) + " ：" + orderTime + "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_14) + " ：" + payTime;
            status = MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_15);
        } else if ("4".equals(order.getStatus())) {
            content += "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_11) + " ：" + orderTime;
            status = MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_17);
        } else if ("5".equals(order.getStatus())) {
            content += "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_18) + " ：" + cancelTime + "\n" + " " + MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_19) + " ：" + order.getCancelreason();
            status = MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_20);
        } else if ("6".equals(order.getStatus())) {
            status = MyApplication.mContext.getResources().getString(R.string.OrderAdapter_java_21);
        }
        holder.mOrderContentTV.setText(content);
        holder.mOrderStatusTV.setText(status);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mOrderNumTV;
        private TextView mOrderContentTV;
        private TextView mOrderStatusTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            mOrderNumTV = (TextView) itemView.findViewById(R.id.order_item_num);
            mOrderContentTV = (TextView) itemView.findViewById(R.id.order_item_content);
            mOrderStatusTV = (TextView) itemView.findViewById(R.id.order_item_status);
        }
    }
}