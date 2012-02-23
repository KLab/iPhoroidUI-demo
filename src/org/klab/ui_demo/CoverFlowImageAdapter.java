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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import org.klab.iphoroid.widget.coverflow.CoverFlowImageAdapterBase;
import org.klab.iphoroid.widget.support.HasImage;
import org.klab.iphoroid.widget.support.ImageCache;
import org.klab.iphoroid.widget.support.SimpleImageDownloadTask;
import org.klab.ui_demo.model.Item;
import org.klab.ui_demo.net.HTTPClient;


/**
 * CoverFlow と画像を関連付けるためのアダプターです。
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/06/23 sano-n initial version <br>
 */
public class CoverFlowImageAdapter extends CoverFlowImageAdapterBase<Item> {

    /**
     * アイテムの幅と高さを指定して、インスタンスを初期化します。
     *
     * @param context コンテキスト。
     * @param items 画像のリソース ID コレクション。
     * @param width アイテムの幅。
     * @param height アイテムの高さ。
     * @param isUserEffect 反射エフェクトを使用する場合は true。それ以外は false。
     */
    public CoverFlowImageAdapter(Context context, List<Item> items, int width, int height, boolean isUserEffect) {
        super(context, items, width, height, isUserEffect);
    }

    /** */
    private static final String CACHE_KEY_POSTFIX = "CF";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView view = new ImageView(this.mContext);

        final String url = items.get(position).getImageUrl();
        HasImage.Util.setImage(mContext, url, view, new SimpleImageDownloadTask(mContext) {
            @Override
            public void setResult(Bitmap bitmap) {
                if (bitmap != null) {
                    Bitmap image;
                    Bitmap resizedImage = makeResizedImage(bitmap, mLayoutParams.width, mLayoutParams.width);
                    if (mIsUserEffect) {
                        image = makeReflectedImage(resizedImage, REFLECTION_GAP);
                        if (!resizedImage.isRecycled()) {
                        	resizedImage.recycle();
                        }
                    } else {
                        image = resizedImage;
                    }
                    view.setImageBitmap(image);
                    ImageCache.setImage(url + CACHE_KEY_POSTFIX, image);
                } else {
                    view.setImageDrawable(ListViewActivity.noImageCoverflowIcon);
                }
            }
            @Override
            public Bitmap getBitmap(String url) throws IOException {
                return HTTPClient.getImage(url);
            }
            @Override
            public void showProgress() {
                view.setImageDrawable(ListViewActivity.loadingIcon);
                // don't show dialog
            }
            @Override
            public void dismissProgress() {
                // don't show dialog
            }
        }, CACHE_KEY_POSTFIX);

        view.setLayoutParams(mLayoutParams);
        view.setScaleType(ScaleType.CENTER_INSIDE);

//        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
//        drawable.setAntiAlias(true);

        return view;
    }
}
