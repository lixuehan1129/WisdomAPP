package com.example.wisdompark19.Society;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisdompark19.R;


/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyFindThing extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_find_things, container, false);
        TextView textView = (TextView)view.findViewById(R.id.textView3);
        textView.setText("失物招领1");
        return view;
    }
}
