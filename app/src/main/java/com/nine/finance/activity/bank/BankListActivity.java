package com.nine.finance.activity.bank;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.nine.finance.R;
import com.nine.finance.activity.BaseActivity;
import com.nine.finance.adapter.BankListAdapter;

public class BankListActivity extends BaseActivity {

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
