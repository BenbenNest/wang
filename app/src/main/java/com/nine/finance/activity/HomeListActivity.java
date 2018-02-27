package com.nine.finance.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.adapter.HomeListAdapter;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.HomeInfo;
import com.nine.finance.recyclerview.EndLessOnScrollListener;
import com.nine.finance.recyclerview.MyDecoration;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.SearchView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyApplyBankListActivity.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView mSearchView;
    private LinearLayoutManager mLinearLayoutManager;
    private HomeListAdapter mAdapter;
    private String mKey = "";
    private int lastId = 0;
    private int firstPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        init();
    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mAdapter = new HomeListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
//                requestData(1);
            }
        });
        mSearchView.setSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKey = mSearchView.getKey();
                requestData(firstPage);
            }
        });
        requestData(0);
    }

    private void requestData(final int page) {
        if (!NetUtil.isNetworkConnectionActive(HomeListActivity.this)) {
            ToastUtils.showCenter(HomeListActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<HomeInfo>>> call = api.getHome();
        call.enqueue(new Callback<BaseModel<List<HomeInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<HomeInfo>>> call, Response<BaseModel<List<HomeInfo>>> response) {
                if (response != null && response.code() == 200) {
                    List<HomeInfo> list = response.body().content;
                    if (page == 0) {
                        mAdapter.resetData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<HomeInfo>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        requestData(firstPage);
    }
}