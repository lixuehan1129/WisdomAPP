package com.example.wisdompark19.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisdompark19.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/4/9.
 */

public class SocietyFindAdapter extends RecyclerView.Adapter<SocietyFindAdapter.ViewHolder> {

    private List<Society_Find_item> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shop_item_image1;
        ImageView shop_item_image2;
        ImageView shop_item_image3;
        TextView shop_item_content;

        public ViewHolder(View itemView) {
            super(itemView);
            shop_item_image1 = (ImageView)itemView.findViewById(R.id.society_find_item_image1);
            shop_item_image2 = (ImageView)itemView.findViewById(R.id.society_find_item_image2);
            shop_item_image3 = (ImageView)itemView.findViewById(R.id.society_find_item_image3);
            shop_item_content = (TextView)itemView.findViewById(R.id.society_find_item_content);

        }
    }

    public SocietyFindAdapter(List<Society_Find_item> data){
        mDataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.society_find_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder)vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

//        Shop_Trade_item mShop_Trade_item = mDataSet.get(position);
//        String shop_image = mShop_Trade_item.getShop_trade_image();
//        String shop_content = mShop_Trade_item.getShop_trade_content();
        //这里的图片来源需要修改
//        holder.shop_item_image.setImageResource(R.mipmap.ic_image_load);
//        holder.shop_item_content.setText(shop_content);

        Society_Find_item society_find_item = mDataSet.get(position);
        holder.shop_item_content.setText(society_find_item.getShop_trade_content());

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

    public class Society_Find_item{
        private String shop_trade_image1;
        private String shop_trade_image2;
        private String shop_trade_image3;
        private String shop_trade_content;

        public String getShop_trade_image1() {
            return shop_trade_image1;
        }

        public void setShop_trade_image1(String shop_trade_image1) {
            this.shop_trade_image1 = shop_trade_image1;
        }

        public String getShop_trade_image2() {
            return shop_trade_image2;
        }

        public void setShop_trade_image2(String shop_trade_image2) {
            this.shop_trade_image2 = shop_trade_image2;
        }

        public String getShop_trade_image3() {
            return shop_trade_image3;
        }

        public void setShop_trade_image3(String shop_trade_image3) {
            this.shop_trade_image3 = shop_trade_image3;
        }

        public String getShop_trade_content() {
            return shop_trade_content;
        }

        public void setShop_trade_content(String shop_trade_content) {
            this.shop_trade_content = shop_trade_content;
        }
        public Society_Find_item( String shop_trade_content){
            this.shop_trade_content = shop_trade_content;
        }
//        public Society_Find_item(String shop_trade_image1, String shop_trade_image2,
//                                 String shop_trade_image3, String shop_trade_content){
//            this.shop_trade_image1 = shop_trade_image1;
//            this.shop_trade_image2 = shop_trade_image2;
//            this.shop_trade_image3 = shop_trade_image3;
//            this.shop_trade_content = shop_trade_content;
//        }
    }
}
