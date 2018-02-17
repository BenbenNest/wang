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
import com.nine.finance.model.BankInfo;

import java.util.List;

/**
 * Created by jeremy
 */
public class BranchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BankInfo> mList;
    private Context mContext;

    public BranchListAdapter(Context context, List<BankInfo> list) {
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
//            ((MyHolder) holder).tv.setText(position + "." + itemText);
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
            myHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("binkinfo", mList.get(position));
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
            });

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
