package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeremy.wang.R;
import com.jeremy.wang.adapter.BankCardListAdapter;
import com.jeremy.wang.model.BankInfo;
import com.jeremy.wang.recyclerview.EndLessOnScrollListener;
import com.jeremy.wang.recyclerview.MyDecoration;
import com.jeremy.wang.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class BankCardListActivity extends BaseActivity {
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
//        setTitle(R.string.title_card_list);
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

        findViewById(R.id.bt_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FaceScanActivity.startActivity(BankCardListActivity.this);
                ChooseBankActivity.startActivity(BankCardListActivity.this);
            }
        });
        test();
    }

    private void test() {
        List<BankInfo> list = new ArrayList<BankInfo>();
        BankInfo bankInfo = new BankInfo();
        bankInfo.setName("中国银行");
        bankInfo.setStatus(Constant.BANK_STATUS_OK);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setName("工商银行");
        bankInfo.setStatus(Constant.BANK_STATUS_OK);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setName("建设银行");
        bankInfo.setStatus(Constant.BANK_STATUS_APPLYING);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setName("农业银行");
        bankInfo.setStatus(Constant.BANK_STATUS_APPLYING);
        list.add(bankInfo);
        mAdapter = new BankCardListAdapter(BankCardListActivity.this, list);
        mRececyclerView.setAdapter(mAdapter);
    }

    private void loadMoreData() {

        mAdapter.notifyDataSetChanged();

    }


}
