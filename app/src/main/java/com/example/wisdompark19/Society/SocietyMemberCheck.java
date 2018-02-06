package com.example.wisdompark19.Society;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyMemberCheck extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_member_check, container, false);
        TextView textView = (TextView)view.findViewById(R.id.textView5);
        textView.setText("成员列表");
        return view;
    }
}
