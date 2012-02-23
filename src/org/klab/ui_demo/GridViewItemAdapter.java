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
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.klab.iphoroid.widget.support.HasImage;
import org.klab.iphoroid.widget.support.SimpleImageDownloadTask;
import org.klab.ui_demo.model.Item;
import org.klab.ui_demo.net.HTTPClient;


/**
 * GridViewItemAdapter. 
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/07/08 sano-n initial version <br>
 */
class GridViewItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater inflater;

    public GridViewItemAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /** */
    @SuppressWarnings("unused")
    private static final String CACHE_KEY_POSTFIX = "GR";
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_view_item, null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Item item = getItem(position);
        if (item != null) {
            viewHolder.imageView.setTag(position);
            final String url = item.getImageUrl();
            viewHolder.imageView.setTag(position); // view 使いまわされ対策
            HasImage.Util.setImage(getContext(), url, viewHolder.imageView, new SimpleImageDownloadTask(getContext()) {
                @Override
                public void setResult(Bitmap bitmap) {
                    if (bitmap != null) {
                        if (viewHolder.imageView.getTag() != null && (Integer) viewHolder.imageView.getTag() == position) { // view 使いまわされ対策
                            viewHolder.imageView.setImageBitmap(bitmap);
                        }
                    } else {
                        if (viewHolder.imageView.getTag() != null && (Integer) viewHolder.imageView.getTag() == position) { // view 使いまわされ対策
                            viewHolder.imageView.setImageDrawable(ListViewActivity.noImageIcon);
                        }
                    }
                }
                @Override
                public Bitmap getBitmap(String url) throws IOException {
                    return HTTPClient.getImage(url);
                }
                @Override
                public void showProgress() {
                    viewHolder.imageView.setImageDrawable(ListViewActivity.loadingIcon);
                    // don't show dialog
                }
                @Override
                public void dismissProgress() {
                    // don't show dialog
                }
//              }, CACHE_KEY_POSTFIX);
              }, null);
        }

        return view;
    }

    /** */
    protected Bitmap makeSquareImage(Bitmap src, int width, int height) {
        Bitmap dest = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        Canvas canvas = new Canvas(dest);
        int l = (src.getWidth() - width) / 2;
        int t = (src.getHeight() - height) / 2;
        int r = l + width;
        int b = t + height;
        canvas.drawBitmap(src, new Rect(l, t, r, b), new Rect(0, 0, width, height), null);
Log.d("GalleryItemAdapter", "crop: " + src.getWidth() + "x" + src.getHeight() + " -> " + dest.getWidth() + "x" + dest.getHeight());
        return dest;
    }
}
