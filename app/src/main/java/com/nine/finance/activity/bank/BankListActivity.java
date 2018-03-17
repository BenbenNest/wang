package com.nine.finance.activity.bank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.activity.BaseActivity;
import com.nine.finance.activity.MyApplyBankListActivity;
import com.nine.finance.adapter.BankListAdapter;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BankInfo;
import com.nine.finance.model.BaseModel;
import com.nine.finance.recyclerview.EndLessOnScrollListener;
import com.nine.finance.recyclerview.MyDecoration;
import com.nine.finance.sortedview.CharacterParser;
import com.nine.finance.sortedview.PinyinComparator;
import com.nine.finance.sortedview.SideBar;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BankListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = MyApplyBankListActivity.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView mSearchView;
    private LinearLayoutManager mLinearLayoutManager;
    private BankListAdapter mAdapter;
    private String mKey = "";
    private int lastId = 0;
    private int firstPage = 0;
    private SideBar sideBar;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        init();
    }


    private void init() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                ToastUtils.showCenter(BankListActivity.this, s);
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
//                    recyclerView.scrollToPosition(position);
                    smoothMoveToPosition(recyclerView, position);
                }
            }
        });

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
        mAdapter = new BankListAdapter(this);
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

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        position = position + mLinearLayoutManager.findLastVisibleItemPosition() - mLinearLayoutManager.findFirstVisibleItemPosition() - 1;
        if (position > mLinearLayoutManager.getItemCount()) {
            position = mLinearLayoutManager.getItemCount();
        }
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
//            mToPosition = position;
//            mShouldScroll = true;
        }
    }

    private List<BankInfo> getData() {
        List<BankInfo> list = new ArrayList<BankInfo>();
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("中国银行");
//        bankInfo.setState(Constant.BANK_STATUS_OK);
        bankInfo.setState("已申请");
        for (int i = 0; i < 15; i++) {
            list.add(bankInfo);
        }
        return list;
    }

    private void requestData(final int page) {
        if (!NetUtil.isNetworkConnectionActive(BankListActivity.this)) {
            ToastUtils.showCenter(BankListActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<BankInfo>>> call = api.getBankList();
        call.enqueue(new Callback<BaseModel<List<BankInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<BankInfo>>> call, Response<BaseModel<List<BankInfo>>> response) {
                if (response != null && response.code() == 200) {
                    List<BankInfo> list = response.body().content;
//                    if (page == 0) {
//                        mAdapter.resetData(list);
//                    } else {
//                        mAdapter.addData(list);
//                    }
//                    for (int i = 0; i < 5; i++) {
//                        list.addAll(list);
//                    }
                    Collections.sort(list, pinyinComparator);
                    mAdapter.resetData(list);
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<BankInfo>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        requestData(firstPage);
    }
}
