package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeremy.wang.R;
import com.jeremy.wang.adapter.BankCardListAdapter;
import com.jeremy.wang.recyclerview.EndLessOnScrollListener;
import com.jeremy.wang.recyclerview.MyDecoration;

public class BankCardListActivity extends AppCompatActivity {
    RecyclerView mRececyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BankCardListAdapter mAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BankCardListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_list);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_card_list);
        mRececyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRececyclerView.setLayoutManager(mLinearLayoutManager);

        mRececyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mRececyclerView.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });

        findViewById(R.id.bt_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceScanActivity.startActivity(BankCardListActivity.this);
            }
        });

    }

    private void loadMoreData() {

        mAdapter.notifyDataSetChanged();

    }


}
