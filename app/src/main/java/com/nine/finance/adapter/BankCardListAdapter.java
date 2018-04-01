package com.nine.finance.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nine.finance.R;
import com.nine.finance.model.BankInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremy
 */
public class BankCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //定义一个集合，接收从Activity中传递过来的数据和上下文
    private List<BankInfo> mList = new ArrayList<>();
    private Context mContext;

    public BankCardListAdapter(Context context) {
        this.mContext = context;
    }

    public BankCardListAdapter(Context context, List<BankInfo> list) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            BankInfo bankInfo = mList.get(position);
            String itemText = bankInfo.getBankName();
            if (TextUtils.isEmpty(itemText)) {
                itemText = "test";
            }
            ((MyHolder) holder).tv.setText(itemText);
            if (!TextUtils.isEmpty(bankInfo.getLogo())) {
                Glide.with(mContext).load(bankInfo.getLogo()).into(((MyHolder) holder).iv);
            }
            if (!TextUtils.isEmpty(bankInfo.getBackground())) {
                Glide.with(mContext)
                        .load(bankInfo.getBackground())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(180, 180) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(resource);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ((MyHolder) holder).root.setBackground(drawable);
//                                }
                            }
                        });
            }
            ((MyHolder) holder).tv.setText(itemText);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView iv;
        TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            iv = (ImageView) itemView.findViewById(R.id.logo);
            tv = (TextView) itemView.findViewById(R.id.bank_name);
        }
    }
}
