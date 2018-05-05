package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.adapter.BankCardListAdapter;
import com.nine.finance.app.AppGlobal;
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

public class MyApplyBankListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyApplyBankListActivity.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRececyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BankCardListAdapter mAdapter;
    private int pageNum = 20;
    private int lastId = 0;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyApplyBankListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_list);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData(0);
    }

    private void init() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );

        mRececyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRececyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new BankCardListAdapter(this);
        mRececyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mRececyclerView.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (mAdapter.getItemCount() % pageNum == 0) {
                    requestData(1);
                }
            }
        });

        mRececyclerView.setAdapter(mAdapter);
        findViewById(R.id.bt_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FaceScanActivity.startActivity(MyApplyBankListActivity.this);
                ChooseBankActivity.startActivity(MyApplyBankListActivity.this);
            }
        });
        requestData(0);
    }

    private void test() {
        List<BankInfo> list = new ArrayList<BankInfo>();
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("中国银行");
        bankInfo.setState("已申请");
        for (int i = 0; i < 15; i++) {
            list.add(bankInfo);
        }
        mAdapter = new BankCardListAdapter(MyApplyBankListActivity.this, list);
        mRececyclerView.setAdapter(mAdapter);
    }

    private List<BankInfo> getData() {
        List<BankInfo> list = new ArrayList<BankInfo>();
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("中国银行");
        bankInfo.setState("已申请");
        for (int i = 0; i < 15; i++) {
            list.add(bankInfo);
        }
        return list;
    }

    private void requestData(final int page) {
        if (!NetUtil.isNetworkConnectionActive(MyApplyBankListActivity.this)) {
            ToastUtils.showCenter(MyApplyBankListActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        if (AppGlobal.getUserInfo() != null) {
            para.put("userId", AppGlobal.getUserInfo().getUserId());
        }

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<BankInfo>>> call = api.getApplyBankList(AppGlobal.getUserInfo().getUserId());
        call.enqueue(new Callback<BaseModel<List<BankInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<BankInfo>>> call, Response<BaseModel<List<BankInfo>>> response) {
                if (response != null && response.code() == 200) {
                    List<BankInfo> list = response.body().content;
                    if (page == 0) {
                        if (list == null || list.size() == 0) {
                            startActivity(MyApplyBankListActivity.this, ChooseBankActivity.class);
                            MyApplyBankListActivity.this.finish();
                        } else {
                            mAdapter.resetData(list);
                        }
                    } else {
                        mAdapter.addData(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<BankInfo>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        requestData(0);
        mRefreshLayout.setRefreshing(false);
    }

}
