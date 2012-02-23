/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by iphoroid team
 */

package org.klab.ui_demo.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


/**
 * HTTPClient. 
 *
 * @author <a href="mailto:kodama-t@klab.jp">Takuya KODAMA</a> (kodamta-t)
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 */
public abstract class HTTPClient {

    /**
     * {@link android.os.AsyncTask} で使用されるのを前提で、
     * {@link Thread#interrupt()} されると通信を中断します。
     * 
     * @return null when thread interrupted
     * @throws NullPointerException l.61 why? 
     */
    private static byte[] getByteArrayFromURL(String urlString) throws IOException {
        URLConnection uc = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            URL url = new URL(urlString);
            uc = url.openConnection();
            if (uc instanceof HttpURLConnection) {
                ((HttpURLConnection) uc).setRequestMethod("GET");
            }
            uc.connect();

            is = uc.getInputStream();
            os = new ByteArrayOutputStream();

            byte[] byteArray = new byte[1024];
            int size = 0;
            while ((size = is.read(byteArray)) != -1) {
                os.write(byteArray, 0, size);
                if (Thread.currentThread().isInterrupted()) {
Log.w("HTTPClient", "thread interrupted: " + Thread.currentThread().getName());
                    return null;
                }
            }

            byte[] result = os.toByteArray();
            return result;
        } finally {
            if (uc instanceof HttpURLConnection) {
                ((HttpURLConnection) uc).disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * {@link android.os.AsyncTask} で使用されるのを前提で、
     * {@link Thread#interrupt()} されると通信を中断します。
     * 
     * @return null when thread interrupted 
     */
    public static Bitmap getImage(String url) throws IOException {
        byte[] byteArray = getByteArrayFromURL(url);
        if (byteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
Log.d("HTTPClient", "bitmap: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            return bitmap;
        } else {
            return null;
        }
    }
}
