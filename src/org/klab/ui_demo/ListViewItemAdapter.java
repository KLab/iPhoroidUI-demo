/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by iphoroid team
 */

package org.klab.ui_demo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.klab.iphoroid.R;
import org.klab.iphoroid.widget.listview.RefreshableArrayAdapter;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.ui_demo.dao.ListViewItemDao;
import org.klab.ui_demo.model.Item;


/**
 * ListViewItemAdapter.
 *
 * @author <a href="mailto:kodama-t@klab.jp">Takuya KODAMA</a> (kodamta-t)
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 */
abstract class ListViewItemAdapter extends RefreshableArrayAdapter<Item> {

    /** */
    private LayoutInflater inflater;
    
    public ListViewItemAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView dateTextView;
        TextView titleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.play_image);
            viewHolder.dateTextView = (TextView) view.findViewById(R.id.date);
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Item item = this.getItem(position);
        if (item != null) {
            // 画像
            viewHolder.imageView.setTag(position);
            HasImage.Util.setImage(getContext(), item.getThumbnailUrl(), viewHolder.imageView, ListViewActivity.loadingIcon, ListViewActivity.noImageIcon); 
            // テキスト
            viewHolder.dateTextView.setText(item.getDate());
            viewHolder.titleTextView.setText(item.getTitle());
        }

        return view;
    }

    @Override
    public int getPagingSize() {
        return ListViewItemDao.PAGE_SIZE;
    }
}
