package com.full.recyclerviewdemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;

import com.full.recyclerviewdemo.R;
import com.full.recyclerviewdemo.adapters.ContactsRecyclerViewAdapter;
import com.full.recyclerviewdemo.util.FastScroller;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView mListView;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ArrayList<String> lTestDataList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            lTestDataList.add((char)('a'+i)+" test " + i);
        }
        Animation lAnimation = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        final ContactsRecyclerViewAdapter mAdapter = new ContactsRecyclerViewAdapter(this, lTestDataList);
        mRecyclerView.setAdapter(mAdapter);
        FastScroller lFastScroller = (FastScroller) findViewById(R.id.fast_scroller);
        lFastScroller.setRecyclerView(mRecyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test:
                startActivity(new Intent(this,ListActivity.class));
                break;
        }
        return true;
    }
}
