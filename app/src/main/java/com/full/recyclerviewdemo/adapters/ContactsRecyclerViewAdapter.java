package com.full.recyclerviewdemo.adapters;

import android.animation.Animator;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.full.recyclerviewdemo.R;

import java.util.ArrayList;

/**
 * Created by Shangeeth Sivan on 25/05/17.
 */

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> implements SectionIndexer {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mDataList;

    public ContactsRecyclerViewAdapter(Context mContext, ArrayList<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View lView = mLayoutInflater.inflate(R.layout.list_item_rec_view, viewGroup, false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mDataTV.setText(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public Object[] getSections() {
        return mDataList.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDataTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mDataTV = (TextView) itemView.findViewById(R.id.data_tv);
        }
    }
}
