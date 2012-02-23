/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by iphoroid team
 */

package org.klab.ui_demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.klab.iphoroid.R;
import org.klab.iphoroid.widget.listview.PullToRefreshEndlessListView;
import org.klab.iphoroid.widget.listview.PullToRefreshEndlessListView.DefaultRefreshListener;
import org.klab.iphoroid.widget.support.DownloadTask;
import org.klab.iphoroid.widget.support.DownloadTask.DefaultDawnloadTask;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.ui_demo.dao.ListViewItemDao;
import org.klab.ui_demo.model.Item;


/**
 * ListView demo
 *
 * @author <a href="mailto:kodama-t@klab.jp">Takuya KODAMA</a> (kodamta-t)
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 */
public class ListViewActivity extends Activity implements HasImage {

    // for application
    public static Drawable loadingIcon;
    public static Drawable loadingCoverflowIcon;
    public static Drawable noImageIcon;
    public static Drawable noImageCoverflowIcon;

    /** for image management */
    private HasImage.ListViewOnScrollListener onScrollListener;

    /** for image management */
    public int getScrollState() {
        return onScrollListener.getScrollState();
    }

    /** */
    private ImageButton gridViewButton;

    /** */
    private ListView listView;

    /** for intent */
    public static final String INTENT_EXTRA_SELECTION = "selection";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        OptionsMenuUtil.createOptionsMenu(this, menu);
        return true;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        // application
        Resources r = getResources();
        loadingIcon = r.getDrawable(R.drawable.loading_thumbnail);
        noImageIcon = r.getDrawable(R.drawable.noimage_thumbnail);
        loadingCoverflowIcon = r.getDrawable(R.drawable.loading_coverflow);
        noImageCoverflowIcon = r.getDrawable(R.drawable.noimage_coverflow);
        
        // UI
        this.listView = (ListView) findViewById(R.id.listView);
        this.onScrollListener = new HasImage.ListViewOnScrollListener();
        listView.setOnScrollListener(onScrollListener);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, final int position, long id) {
                ListAdapter adapter = ((PullToRefreshEndlessListView<?>) av).getWrappedAdapter();
                toDetail(position, adapter.getCount());
            }
        });
        this.gridViewButton = (ImageButton) findViewById(R.id.gridViewButton);
        gridViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGridView(0);
            }
        });

        // data
        load();
    }

    /** */
    private void load() {
        DownloadTask<Integer, List<Item>> task = new DefaultDawnloadTask<Integer, List<Item>>(this, getResources().getString(R.string.dialog_message_loading)) {
            @Override
            public List<Item> download(Integer... params) throws Exception {
                int offset = params[0] * ListViewItemDao.PAGE_SIZE;
                return ListViewItemDao.getInstance().getItems(offset);
            }
            @Override
            public void setResult(List<Item> items) {
                listView.setAdapter(new ListViewItemAdapter(ListViewActivity.this, items) {
                    @Override
                    public List<Item> getItemsOnRefresh(int offset) throws IOException {
                        return ListViewItemDao.getInstance().getItems(offset == DefaultRefreshListener.PULL_TO_REFRESH ? 0 : offset);
                    }
                });
            }
        };
        try {
            task.execute(0);
        } catch (RejectedExecutionException e) {
            new AlertDialog.Builder(this).setTitle("Error").setMessage(e.getMessage()).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
Log.d("ListViewActivity", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        switch (requestCode) {
        case 100:
            if (resultCode == RESULT_OK) {
                int selection = data.getIntExtra(INTENT_EXTRA_SELECTION, 0);
Log.d("ListViewActivity", "position 0: " + selection);
                listView.setSelection(selection);
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for image management
        HasImage.Util.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // for image management
        HasImage.Util.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // for image management
        HasImage.Util.onDestroy(this);
    }

    /** */
    private void toGridView(int position) {
        Intent intent = new Intent(this, GridViewActivity.class);
        intent.putExtra(FlowViewAndCoverFlowActivity.INTENT_EXTRA_SELECTION, position);
        startActivity(intent);
    }


    /** */
    private void toDetail(int position, int count) {
        Intent intent = new Intent(this, FlowViewAndCoverFlowActivity.class);
Log.d("ListViewActivity", "position: " + position);
        intent.putExtra(FlowViewAndCoverFlowActivity.INTENT_EXTRA_SELECTION, position);
        startActivityForResult(intent, 100);
    }
}
