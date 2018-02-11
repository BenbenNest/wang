package com.jeremy.wang.activity.bank;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jeremy.wang.R;
import com.jeremy.wang.activity.BaseActivity;
import com.jeremy.wang.adapter.BankListAdapter;

public class BranchListActivity extends BaseActivity {

    RecyclerView recyclerView;
    BankListAdapter bankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        init();
    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        
    }




}
