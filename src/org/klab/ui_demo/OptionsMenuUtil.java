/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by iphoroid team
 */

package org.klab.ui_demo;

import org.klab.iphoroid.R;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;


/**
 * OptionsMenuUtil. 
 *
 * @author <a href="mailto:kodama-t@klab.jp">Takuya KODAMA</a> (kodamta-t)
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 */
public abstract class OptionsMenuUtil {

    public static interface OnReloadMenuItemCkickListener {
        void onReloadMenuItemCkick();
    }

    /** */
    public static void createOptionsMenu(final Context context, Menu menu) {
        OnMenuItemClickListener listener = new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case 0:
                    Intent i = new Intent(context, ListViewActivity.class);
                    context.startActivity(i);
                    break;
                case 1:
                    Intent j = new Intent(context, GridViewActivity.class);
                    context.startActivity(j);
                    break;
                case 2:
                    Intent k = new Intent(context, FlowViewAndCoverFlowActivity.class);
                    context.startActivity(k);
                    break;
                case 3:
                    Intent l = new Intent(context, FlowViewAndCoverFlowActivity.class);
                    context.startActivity(l);
                    break;
                }
                return false;
            }
        };
        menu.add(0, 0, 0, context.getResources().getString(R.string.activity_listview)).setIcon(R.drawable.menu_icon_list).setOnMenuItemClickListener(listener);
        menu.add(0, 1, 0, context.getResources().getString(R.string.activity_gridview)).setIcon(R.drawable.menu_icon_grid).setOnMenuItemClickListener(listener);
        menu.add(0, 2, 0, context.getResources().getString(R.string.activity_flowview)).setIcon(R.drawable.menu_icon_slaidshow).setOnMenuItemClickListener(listener);
        menu.add(0, 3, 0, context.getResources().getString(R.string.activity_coverflow)).setIcon(R.drawable.menu_icon_coverflow).setOnMenuItemClickListener(listener);
    }
}
