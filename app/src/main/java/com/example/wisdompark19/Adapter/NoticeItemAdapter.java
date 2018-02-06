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
 * Created by 最美人间四月天 on 2018/1/25.
 */

public class NoticeItemAdapter extends RecyclerView.Adapter<NoticeItemAdapter.ViewHolder> {

    private List<Notice_item> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView card_message_tell;
        TextView card_message_content;
        TextView card_message_time;

        public ViewHolder(View itemView) {
            super(itemView);
            card_message_tell = (TextView)itemView.findViewById(R.id.card_message_tell);
            card_message_content = (TextView)itemView.findViewById(R.id.card_message_content);
            card_message_time = (TextView)itemView.findViewById(R.id.card_message_time);
        }
    }

    //构造器，接受数据集
    public NoticeItemAdapter(List<Notice_item> data){
        mDataSet = data;
    }

    //
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder)vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Notice_item mNotice_item = mDataSet.get(position);
        String card_message_tell = mNotice_item.getCard_message_tell();
        String card_message_content = mNotice_item.getCard_message_content();
        String card_message_time = mNotice_item.getCard_message_time();

        holder.card_message_tell.setText(card_message_tell);
        holder.card_message_content.setText(card_message_content);
        holder.card_message_time.setText(card_message_time);

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

    public class Notice_item{
        private String card_message_tell;
        private String card_message_content;
        private String card_message_time;

        public Notice_item(String card_message_tell,String card_message_content,String card_message_time){
            this.card_message_tell = card_message_tell;
            this.card_message_content = card_message_content;
            this.card_message_time = card_message_time;
        }

        public String getCard_message_tell() {
            return card_message_tell;
        }

        public void setCard_message_tell(String card_message_tell) {
            this.card_message_tell = card_message_tell;
        }

        public String getCard_message_content() {
            return card_message_content;
        }

        public void setCard_message_content(String card_message_content) {
            this.card_message_content = card_message_content;
        }

        public String getCard_message_time() {
            return card_message_time;
        }

        public void setCard_message_time(String card_message_time) {
            this.card_message_time = card_message_time;
        }

    }
}
