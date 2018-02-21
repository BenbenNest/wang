package com.nine.finance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.BankInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremy
 */
public class BankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BankInfo> mList = new ArrayList<BankInfo>();
    private Context mContext;

    public BankListAdapter(Context context) {
        this.mContext = context;
    }

    public BankListAdapter(Context context, List<BankInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void resetData(List<BankInfo> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<BankInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            final String itemText = mList.get(position).getBankName();
            MyHolder myHolder = (MyHolder) holder;
            ((MyHolder) holder).tv.setText(itemText);
//            switch (mList.get(position).getState()) {
//                case Constant.BANK_STATUS_NO:
//                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_NO_ACTION);
//                    break;
//                case Constant.BANK_STATUS_OK:
//                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_OK_ACTION);
//                    break;
//                case Constant.BANK_STATUS_APPLYING:
//                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_APPLYING_ACTION);
//                    break;
//                case Constant.BANK_STATUS_REJECT:
//                    ((MyHolder) holder).action.setText(Constant.BANK_STATUS_REJECT_ACTION);
//                    break;
//            }
            myHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AppGlobal.getApplyModel().mBank = mList.get(position);
                    AppGlobal.getApplyModel().setBankId(mList.get(position).getBankId());
                    Intent intent = new Intent();
                    intent.putExtra("bank", mList.get(position));
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
            });

        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        View root;
        TextView tv;
        TextView action;

        public MyHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tv = (TextView) itemView.findViewById(R.id.bank_name);
            action = (TextView) itemView.findViewById(R.id.bt_action);
        }
    }
}
