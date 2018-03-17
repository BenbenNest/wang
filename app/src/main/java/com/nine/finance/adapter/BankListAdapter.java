package com.nine.finance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.BankInfo;
import com.nine.finance.sortedview.CharacterParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremy
 */
public class BankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {

    private List<BankInfo> mList = new ArrayList<BankInfo>();
    private Context mContext;
    CharacterParser characterParser;


    public BankListAdapter(Context context) {
        this(context, new ArrayList<BankInfo>());
    }

    public BankListAdapter(Context context, List<BankInfo> list) {
        this.mContext = context;
        this.mList = list;
        characterParser = CharacterParser.getInstance();
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
            final MyHolder myHolder = (MyHolder) holder;
            final BankInfo bankInfo = mList.get(position);
            myHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AppGlobal.getApplyModel().mBank = mList.get(position);
//                    AppGlobal.getApplyModel().setBankId(mList.get(position).getBankId());
                    Intent intent = new Intent();
                    intent.putExtra("bank", bankInfo);
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
            });

            String itemText = bankInfo.getBankName();
            if (TextUtils.isEmpty(itemText)) {
                itemText = "test";
            }
            myHolder.tv.setText(itemText);
            if (!TextUtils.isEmpty(bankInfo.getLogo())) {
                Glide.with(mContext).load(AppGlobal.getUserInfo().getHead()).into(((BankListAdapter.MyHolder) holder).iv);
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
                                myHolder.root.setBackground(drawable);
//                                }
                            }
                        });
            }
            ((BankListAdapter.MyHolder) holder).tv.setText(itemText);

        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < mList.size(); i++) {
            String sortStr = getSortLetters(mList.get(i).getBankName());
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    public String getSortLetters(String name) {
        String pinyin = characterParser.getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        return getSortLetters(mList.get(position).getBankName()).charAt(0);
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
