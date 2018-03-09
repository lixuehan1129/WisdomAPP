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
import com.example.wisdompark19.Adapter.SocietyMemberAdapter;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyMemberCheck extends Fragment {

    private List<SocietyMemberAdapter.Item_member> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private SocietyMemberAdapter mSocietyMemberAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> member_image = new ArrayList<String>();
    ArrayList<String> member_name = new ArrayList<String>();
    ArrayList<String> member_id = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_member_check, container, false);

        findView(view);
        findData();
        initData();
        setAdapter();
        setItemClick();

        return view;
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_member_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void findData(){
        String member_content;
        for(int i=0; i<15; i++){
            member_content = "社区成员"+i;
            member_image.add(null);
            member_name.add(member_content);
            member_id.add(member_content);

        }
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<15; i++){
            SocietyMemberAdapter newData = new SocietyMemberAdapter(Data);
            SocietyMemberAdapter.Item_member item_member = newData.new Item_member(
                    member_name.get(i),member_image.get(i),member_id.get(i)
            );
            Data.add(item_member);
        }
    }

    private void setAdapter(){
        mSocietyMemberAdapter = new SocietyMemberAdapter(Data);
        mRecyclerView.setAdapter(mSocietyMemberAdapter);
    }

    private void setItemClick(){
        mSocietyMemberAdapter.setOnItemClickListener(new SocietyMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(getActivity(), member_name.get(position), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
