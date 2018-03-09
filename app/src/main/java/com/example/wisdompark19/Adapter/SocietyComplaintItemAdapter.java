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
 * Created by 最美人间四月天 on 2018/3/9.
 */

public class SocietyComplaintItemAdapter extends RecyclerView.Adapter<SocietyComplaintItemAdapter.ViewHolder> {

    private List<Society_Com_Item> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView society_com_item;

        public ViewHolder(View itemView) {
            super(itemView);
             society_com_item = (TextView)itemView.findViewById(R.id.society_complaint_text);

        }
    }

    public SocietyComplaintItemAdapter(List<Society_Com_Item> data){
        mDataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.society_complaint_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder)vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Society_Com_Item mSociety_Com_Item = mDataSet.get(position);
        String society_com_item = mSociety_Com_Item.getSociety_com_item();

        holder.society_com_item.setText(society_com_item);


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

    public class Society_Com_Item{
        public String getSociety_com_item() {
            return society_com_item;
        }

        public void setSociety_com_item(String society_com_item) {
            this.society_com_item = society_com_item;
        }

        private String society_com_item;


        public Society_Com_Item(String society_com_item){
            this.society_com_item = society_com_item;
        }
    }
}
