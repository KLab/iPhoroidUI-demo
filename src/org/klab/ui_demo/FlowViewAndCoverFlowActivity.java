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
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import org.klab.iphoroid.R;
import org.klab.iphoroid.util.ActivityUtil;
import org.klab.iphoroid.widget.coverflow.CoverFlowGallery;
import org.klab.iphoroid.widget.flowview.FlowView;
import org.klab.iphoroid.widget.support.DownloadTask;
import org.klab.iphoroid.widget.support.DownloadTask.DefaultDawnloadTask;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.ui_demo.dao.ItemDao;
import org.klab.ui_demo.model.Item;


/**
 * FlowView (portrait) / Coverflow (landscape) demo
 * 
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/07/14 sano-n initial version <br>
 */
public class FlowViewAndCoverFlowActivity extends Activity implements HasImage, OnItemClickListener, OnItemSelectedListener {

    /** for image management */
    private HasImage.AdapterViewOnScrollListener flowViewOnScrollListener;

    /** for image management */
    private HasImage.AdapterViewOnScrollListener galleryOnScrollListener;

    /** for image management */
    public int getScrollState() {
        if (flowViewOnScrollListener != null) {
            return flowViewOnScrollListener.getScrollState();
        } else if (galleryOnScrollListener != null) {
            return galleryOnScrollListener.getScrollState();
        } else {
            throw new IllegalStateException("flowViewOnScrollListener and galleryOnScrollListener are both null");
        }
    }

    /** landscape */
    private CoverFlowGallery coverFlowGallery;

    /** portrait */
    private FlowView flowView;
    /** portrait */
    private TextView textView;

    /** for intent */
    public static final String INTENT_EXTRA_SELECTION = "selection";

    /** @see #onSaveInstanceState(Bundle) */
    public static final String SAVED_INSTANCE_STATE_SELECTION = "selection";
   
    /** */
    private int selection;

    /** */
    private Intent resultIntent;

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

        if (ActivityUtil.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.flow_view_coverflow);

        int defaultSelection;
        if (savedInstanceState != null) {
            defaultSelection = savedInstanceState.getInt(SAVED_INSTANCE_STATE_SELECTION);
        } else {
            defaultSelection = getIntent().getIntExtra(INTENT_EXTRA_SELECTION, 0);
        }
Log.d("FlowViewActivity", "selection: " + defaultSelection);

Log.w("FlowViewActivity", "orientation: " + ActivityUtil.getScreenOrientation(this));
        if (ActivityUtil.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT) {
            // UI
            this.flowView = (FlowView) findViewById(R.id.flowView);
            this.flowViewOnScrollListener = new HasImage.AdapterViewOnScrollListener();
            flowView.setOnScrollListener(flowViewOnScrollListener);
            flowView.setOnViewSwitchListener(new FlowView.ViewSwitchListener() {
                @Override
                public void onSwitched(View view, int position) {
Log.d("FlowViewActivity", "select 1: " + position);
                    setSelection(position);
                }
            });
        } else {
            // UI
            this.coverFlowGallery = (CoverFlowGallery) this.findViewById(R.id.content_list);
            this.galleryOnScrollListener = new AdapterViewOnScrollListener();
            coverFlowGallery.setOnScrollListener(galleryOnScrollListener);
            coverFlowGallery.setOnItemClickListener(this);
            coverFlowGallery.setOnItemSelectedListener(this);
            coverFlowGallery.setSpacing(-75);
            this.textView = (TextView) findViewById(R.id.content_title);
        }

        // data
        load(defaultSelection);

        // result
        resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
    }

    /** */
    private void load(final int defaultSelection) {
        DownloadTask<Void, List<Item>> task = null;
        if (ActivityUtil.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT) {
            task = new DefaultDawnloadTask<Void, List<Item>>(this, getResources().getString(R.string.dialog_message_loading)) {
                @Override
                public List<Item> download(Void... params) throws Exception {
                    return ItemDao.getInstance().getImages();
                }
                @Override
                public void setResult(List<Item> items) {
                    flowView.setAdapter(new FlowViewItemAdapter(FlowViewAndCoverFlowActivity.this, items));
    
                    flowView.setSelection(defaultSelection);
Log.d("FlowViewActivity", "selection 2: " + defaultSelection);
                }
            };
        } else {
            task = new DefaultDawnloadTask<Void, List<Item>>(this, getResources().getString(R.string.dialog_message_loading)) {
                @Override
                public List<Item> download(Void... args) throws Exception {
                    return ItemDao.getInstance().getImages();
                }
                @Override
                public void setResult(List<Item> items) {
                    coverFlowGallery.setAdapter(new CoverFlowImageAdapter(FlowViewAndCoverFlowActivity.this, items, 300, 450, true));

                    coverFlowGallery.setSelection(defaultSelection, true);
//Log.d("FlowViewActivity", "selection 3: " + defaultSelection);
                }
            };
        }
        try {
            task.execute();
        } catch (RejectedExecutionException e) {
            new AlertDialog.Builder(this).setTitle("Error").setMessage(e.getMessage()).show();
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);

       if (flowView != null) {
           outState.putInt(SAVED_INSTANCE_STATE_SELECTION, selection);
Log.d("FlowViewActivity", "here 1: " + selection);
       } else if (coverFlowGallery != null) {
           outState.putInt(SAVED_INSTANCE_STATE_SELECTION, selection);
Log.d("FlowViewActivity", "here 2: " + selection);
       } else {
           throw new IllegalStateException("listView and coverFlowGallary are both null");
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
    private void setSelection(int selection) {
        this.selection = selection;
        resultIntent.putExtra(ListViewActivity.INTENT_EXTRA_SELECTION, selection);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
Log.d("FlowViewActivity", "select 2: " + position);
        setSelection(position);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
Log.d("FlowViewActivity", "select 3: " + position);
        setSelection(position);
        
        Item item = (Item) parent.getItemAtPosition(position);
        textView.setText(item.getTitle());
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
