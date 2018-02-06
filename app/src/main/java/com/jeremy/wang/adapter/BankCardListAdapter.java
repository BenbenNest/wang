package com.jeremy.wang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.constant.Constant;
import com.jeremy.wang.model.BankInfo;

import java.util.List;

/**
 * Created by jeremy
 */
public class BankCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //定义一个集合，接收从Activity中传递过来的数据和上下文
    private List<BankInfo> mList;
    private Context mContext;

    public BankCardListAdapter(Context context, List<BankInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.bank_list_item_layout, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            final String itemText = mList.get(position).getBankName();
            ((MyHolder) holder).tv.setText(position + "." + itemText);
            switch (mList.get(position).getState()) {
                case Constant.BANK_STATUS_NO:
                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_NO_ACTION);
                    break;
                case Constant.BANK_STATUS_OK:
                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_OK_ACTION);
                    break;
                case Constant.BANK_STATUS_APPLYING:
                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_APPLYING_ACTION);
                    break;
                case Constant.BANK_STATUS_REJECT:
                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_REJECT_ACTION);
                    break;
            }
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView action;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.bank_name);
            action = (TextView) itemView.findViewById(R.id.bt_action);
        }
    }
}
