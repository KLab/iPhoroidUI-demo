/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.ui_demo;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.klab.iphoroid.R;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.iphoroid.widget.support.ImageDownloadTask.DefaultImageDownloadHelper;
import org.klab.ui_demo.model.Item;
import org.klab.ui_demo.net.HTTPClient;


/**
 * FlowViewItemAdapter. 
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/07/08 sano-n initial version <br>
 */
class FlowViewItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater inflater;

    public FlowViewItemAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.flow_view_item, null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Item item = getItem(position);
        if (item != null) {
            viewHolder.imageView.setTag(position);
            HasImage.Util.setImage(getContext(), item.getImageUrl(), viewHolder.imageView, new DefaultImageDownloadHelper(null, ListViewActivity.noImageIcon) {
                @Override
                public void onPreDownload(ImageView imageView) {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
                @Override
                public void onDownloadSuccess(ImageView imageView) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                @Override
                public Bitmap doDownload(String param) throws IOException {
                    return HTTPClient.getImage(param);
                }
            }); 
        }

        return view;
    }
}
