/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.ui_demo;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import org.klab.iphoroid.R;
import org.klab.iphoroid.widget.support.DownloadTask;
import org.klab.iphoroid.widget.support.DownloadTask.DefaultDawnloadTask;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.ui_demo.dao.ItemDao;
import org.klab.ui_demo.model.Item;


/**
 * GridView demo
 * 
 * @intent.extra {@link #INTENT_EXTRA_SELECTION} int initial selected position
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/07/14 sano-n initial version <br>
 */
public class GridViewActivity extends Activity implements HasImage {

    /** for image management */
    private HasImage.ListViewOnScrollListener onScrollListener;

    /** for image management */
    public int getScrollState() {
        return onScrollListener.getScrollState();
    }

    /** */
    private ImageButton listViewButton;

    /** */
    private GridView gridView;

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
        
        setContentView(R.layout.grid_view);
        
        // UI
        this.gridView = (GridView) findViewById(R.id.gridView);  
        this.onScrollListener = new ListViewOnScrollListener();
        gridView.setOnScrollListener(onScrollListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//Log.d("GridViewActivity", "position: " + position);
                 toDetail(position);
            }
        });
        this.listViewButton = (ImageButton) findViewById(R.id.listViewButton);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toListView(0);
            }
        });

        // data
        load();
    }

    /** */
    private void load() {
        DownloadTask<Object, List<Item>> task = new DefaultDawnloadTask<Object, List<Item>>(this, getResources().getString(R.string.dialog_message_loading)) {
            @Override
            public List<Item> download(Object... params) throws Exception {
                return ItemDao.getInstance().getImages();
            }
            @Override
            public void setResult(List<Item> items) {
                gridView.setAdapter(new GridViewItemAdapter(GridViewActivity.this, items));
            }
        };
        try {
            task.execute();
        } catch (RejectedExecutionException e) {
            new AlertDialog.Builder(this).setTitle("Error").setMessage(e.getMessage()).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
Log.d("GridViewActivity", "requestCode: " + requestCode + ", resultCode: " + resultCode);

        switch (requestCode) {
        case 100:
            if (resultCode == RESULT_OK) {
                int selection = data.getIntExtra(INTENT_EXTRA_SELECTION, 0);
Log.d("GridViewActivity", "position 0: " + selection);
                gridView.setSelection(selection);
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
    private void toDetail(int position) {
        Intent intent = new Intent(this, FlowViewAndCoverFlowActivity.class);
        intent.putExtra(FlowViewAndCoverFlowActivity.INTENT_EXTRA_SELECTION, position);
        startActivityForResult(intent, 100);
    }

    /** */
    private void toListView(int position) {
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra(ListViewActivity.INTENT_EXTRA_SELECTION, position);
        startActivity(intent);
    }
}