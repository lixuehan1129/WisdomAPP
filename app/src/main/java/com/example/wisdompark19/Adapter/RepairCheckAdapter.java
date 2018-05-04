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
        TextView repair_check_name;
        TextView repair_check_phone;
        TextView repair_check_fenlei;
        TextView repair_check_shijian;
        TextView repair_check_jindu;
        TextView repair_check_pingjia;

        public ViewHolder(View itemView) {
            super(itemView);
            repair_check_name = (TextView)itemView.findViewById(R.id.repair_check_name);
            repair_check_phone = (TextView)itemView.findViewById(R.id.repair_check_phone);
            repair_check_fenlei = (TextView)itemView.findViewById(R.id.repair_check_leixing);
            repair_check_shijian = (TextView)itemView.findViewById(R.id.repair_check_shijian);
            repair_check_jindu = (TextView)itemView.findViewById(R.id.repair_check_jindu);
            repair_check_pingjia = (TextView)itemView.findViewById(R.id.repair_check_pingjia);
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
        String check_name = mRepair_Check_item.getRepair_check_name();
        String check_phone = mRepair_Check_item.getRepair_check_phone();
        String check_fenlei = mRepair_Check_item.getRepair_check_fenlei();
        String check_shijian = mRepair_Check_item.getRepair_check_shijian();
        //这里的图片来源需要修改
        holder.repair_check_name.setText(check_name);
        holder.repair_check_phone.setText(check_phone);
        holder.repair_check_fenlei.setText(check_fenlei);
        holder.repair_check_shijian.setText(check_shijian);

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
        private String repair_check_name;
        private String repair_check_phone;
        private String repair_check_fenlei;
        private String repair_check_shijian;

        public String getRepair_check_name() {
            return repair_check_name;
        }

        public void setRepair_check_name(String repair_check_name) {
            this.repair_check_name = repair_check_name;
        }

        public String getRepair_check_phone() {
            return repair_check_phone;
        }

        public void setRepair_check_phone(String repair_check_phone) {
            this.repair_check_phone = repair_check_phone;
        }

        public String getRepair_check_fenlei() {
            return repair_check_fenlei;
        }

        public void setRepair_check_fenlei(String repair_check_fenlei) {
            this.repair_check_fenlei = repair_check_fenlei;
        }

        public String getRepair_check_shijian() {
            return repair_check_shijian;
        }

        public void setRepair_check_shijian(String repair_check_shijian) {
            this.repair_check_shijian = repair_check_shijian;
        }

        public Repair_Check_item(String repair_check_name, String repair_check_phone,
                                 String repair_check_fenlei, String repair_check_shijian){
            this.repair_check_name = repair_check_name;
            this.repair_check_phone = repair_check_phone;
            this.repair_check_fenlei = repair_check_fenlei;
            this.repair_check_shijian = repair_check_shijian;
        }
    }
}
