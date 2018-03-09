package com.example.wisdompark19.Society;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.SocietyComplaintItemAdapter;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyMakeComplaint extends Fragment {

    private List<SocietyComplaintItemAdapter.Society_Com_Item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private SocietyComplaintItemAdapter mSocietyComplaintItemAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> society_com_content = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_make_complaint, container, false);

        findView(view);
        findData();
        initData();
        setAdapter();
        setItemClick();

        return view;
    }


    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_complaint_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void findData(){
        String society_content;
        for(int i=0; i<15; i++){
            society_content = "社区吐槽信息,社区吐槽信息界面,在此查看成员吐槽信息"+i;
            society_com_content.add(society_content);
        }
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<15; i++){
            SocietyComplaintItemAdapter newData = new SocietyComplaintItemAdapter(Data);
            SocietyComplaintItemAdapter.Society_Com_Item society_com_item = newData.new Society_Com_Item(
                    society_com_content.get(i)
            );
            Data.add(society_com_item);
        }
    }

    private void setAdapter(){
        mSocietyComplaintItemAdapter = new SocietyComplaintItemAdapter(Data);
        mRecyclerView.setAdapter(mSocietyComplaintItemAdapter);
    }

    private void setItemClick(){
        mSocietyComplaintItemAdapter.setmOnItemClickListener(new SocietyComplaintItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(getActivity(), society_com_content.get(position), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
