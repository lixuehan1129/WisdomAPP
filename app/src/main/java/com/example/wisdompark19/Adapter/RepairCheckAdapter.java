package com.example.wisdompark19.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisdompark19.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/3/16.
 */

public class RepairCheckAdapter extends RecyclerView.Adapter<RepairCheckAdapter.ViewHolder> {

    private List<Repair_Check_item> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView repair_check_content;
        TextView repair_check_sta;
        TextView repair_check_ping;

        public ViewHolder(View itemView) {
            super(itemView);
            repair_check_content = (TextView)itemView.findViewById(R.id.repair_check_content);
            repair_check_sta = (TextView)itemView.findViewById(R.id.repair_check_sta);
            repair_check_ping = (TextView)itemView.findViewById(R.id.repair_check_ping);

        }
    }

    public RepairCheckAdapter(List<Repair_Check_item> data){
        mDataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_check_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder)vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Repair_Check_item mRepair_Check_item = mDataSet.get(position);
        String check_content = mRepair_Check_item.getRepair_check_content();
        String check_sta = mRepair_Check_item.getRepair_check_sta();
        String check_ping = mRepair_Check_item.getRepair_check_ping();
        //这里的图片来源需要修改
        holder.repair_check_content.setText(check_content);
        holder.repair_check_sta.setText(check_sta);
        holder.repair_check_ping.setText(check_ping);


        //判断是否设置了监听
        //为View设置监听
        if(mOnItemClickListener !=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    //Recycleview监听接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class Repair_Check_item{
        private String repair_check_content;
        private String repair_check_sta;
        private String repair_check_ping;

        public String getRepair_check_content() {
            return repair_check_content;
        }

        public void setRepair_check_content(String repair_check_content) {
            this.repair_check_content = repair_check_content;
        }

        public String getRepair_check_sta() {
            return repair_check_sta;
        }

        public void setRepair_check_sta(String repair_check_sta) {
            this.repair_check_sta = repair_check_sta;
        }

        public String getRepair_check_ping() {
            return repair_check_ping;
        }

        public void setRepair_check_ping(String repair_check_ping) {
            this.repair_check_ping = repair_check_ping;
        }

        public Repair_Check_item(String repair_check_content, String repair_check_sta,
                                 String repair_check_ping){
            this.repair_check_content = repair_check_content;
            this.repair_check_sta = repair_check_sta;
            this.repair_check_ping = repair_check_ping;
        }
    }
}
