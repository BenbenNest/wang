package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.adapter.BankCardListAdapter;
import com.nine.finance.constant.Constant;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BankInfo;
import com.nine.finance.model.BaseModel;
import com.nine.finance.recyclerview.EndLessOnScrollListener;
import com.nine.finance.recyclerview.MyDecoration;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        requestData(0);
    }

    private void test() {
        List<BankInfo> list = new ArrayList<BankInfo>();
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("中国银行");
        bankInfo.setState(Constant.BANK_STATUS_OK);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setBankName("工商银行");
        bankInfo.setState(Constant.BANK_STATUS_OK);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setBankName("建设银行");
        bankInfo.setState(Constant.BANK_STATUS_APPLYING);
        list.add(bankInfo);
        bankInfo = new BankInfo();
        bankInfo.setBankName("农业银行");
        bankInfo.setState(Constant.BANK_STATUS_APPLYING);
        list.add(bankInfo);
        mAdapter = new BankCardListAdapter(BankCardListActivity.this, list);
        mRececyclerView.setAdapter(mAdapter);
    }

    private void loadMoreData() {

        mAdapter.notifyDataSetChanged();

    }

    private void requestData(int page) {
        if (!NetUtil.isNetworkConnectionActive(BankCardListActivity.this)) {
            ToastUtils.showCenter(BankCardListActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<BankInfo>>> call = api.getApplyBankList(body);
        call.enqueue(new Callback<BaseModel<List<BankInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<BankInfo>>> call, Response<BaseModel<List<BankInfo>>> response) {

            }

            @Override
            public void onFailure(Call<BaseModel<List<BankInfo>>> call, Throwable t) {

            }
        });


    }


}
