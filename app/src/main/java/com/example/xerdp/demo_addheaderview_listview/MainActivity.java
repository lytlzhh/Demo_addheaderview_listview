package com.example.xerdp.demo_addheaderview_listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import adapter.List_adapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Mylistview.IRefreshlistener {

    private Mylistview listview_id;
    private ScrollView scrollview_id;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] arg = {"美国", "俄罗斯", "中国", "印度"};
    private List<Bean> list = null;

    private List_adapter list_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //  fun();

    }

    private void initView() {
        listview_id = (Mylistview) findViewById(R.id.listview_id);
        // scrollview_id = (ScrollView) findViewById(R.id.scrollview_id);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        listview_id.setInterface(this);
        list = new ArrayList<>();
    }


    public void fun() {

        for (int i = 0; i < 2; i++) {
            list.add(0, new Bean("hello"));
        }
    }


    public void showlist(List<Bean> list) {
        if (list_adapter == null) {
            list_adapter = new List_adapter(list, this);
            listview_id.setAdapter(list_adapter);
        } else {
            list_adapter.onDatachange(list);
        }
    }


    @Override
    public void onRefresh() {
        onReflash();
    }

    @Override
    public void onReflash() {
        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fun();
                showlist(list);
                listview_id.RefreshComplete();
            }
        }, 400);
    }
}
