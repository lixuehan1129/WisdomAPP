package com.example.wisdompark19.Society;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.SocietyTuCaoAdapter;
import com.example.wisdompark19.Adapter.TabLayoutAdapter;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 最美人间四月天 on 2018/1/9.
 */

public class SocietyFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private SocietyTuCaoAdapter societyTuCaoAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabLayoutAdapter mTabLayoutAdapter;
    private String[] titles=new String[]{"消息通知", "失物招领", "社区吐槽", "成员列表"};
    private List<Fragment> fragments=new ArrayList<>();

    public static SocietyFragment newInstance(String info) {
        Bundle args = new Bundle();
        args.putString("info", info);
        SocietyFragment societyFragment = new SocietyFragment();
        societyFragment.setArguments(args);
        return societyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.societyfragment, null);
        Toolbar mToolbar = (Toolbar)view.findViewById(R.id.mainTool);
        mToolbar.setTitle("社区");
        initView(view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setMenu(mToolbar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_society_select,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setMenu(Toolbar toolbar){
        //返回按钮监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //menu item点击事件监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_message:
                        Toast.makeText(getActivity(),"消息通知",Toast.LENGTH_LONG).show();
                        Intent intent_mes = new Intent(getActivity(), SocietyNewMessagePage.class);
                        intent_mes.putExtra("put_data_mes","消息通知");
                        startActivity(intent_mes);
                        break;
                    case R.id.menu_find:
                        Toast.makeText(getActivity(),"失物招领",Toast.LENGTH_LONG).show();
                        Intent intent_find = new Intent(getActivity(), SocietyFindPageActivity.class);
                        intent_find.putExtra("put_data_find","失物招领");
                        startActivity(intent_find);
                        break;
                    case R.id.menu_tu_cao:
                        Toast.makeText(getActivity(),"社区吐槽",Toast.LENGTH_LONG).show();
                        showEditDialog();
                        break;
                }
                return false;
            }
        });

    }

    private void initView(View view){
        mTabLayout = (TabLayout)view.findViewById(R.id.society_layout);
        mViewPager = (ViewPager)view.findViewById(R.id.society_viewpager);
        //设置TabLayout标签的显示方式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            mTabLayout.addTab(mTabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        mTabLayout.setOnTabSelectedListener(this);

        fragments.add(new SocietyNewMessage());
        fragments.add(new SocietyFindThing());
        fragments.add(new SocietyMakeComplaint());
        fragments.add(new SocietyMemberCheck());

        mTabLayoutAdapter=new TabLayoutAdapter(getChildFragmentManager(),titles,fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void showEditDialog(){
        societyTuCaoAdapter = new SocietyTuCaoAdapter(getActivity());
        societyTuCaoAdapter.setView(new EditText(getContext()));
        societyTuCaoAdapter.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                societyTuCaoAdapter.showKeyboard();
            }
        }, 200);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
