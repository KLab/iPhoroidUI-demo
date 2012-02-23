/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by iphoroid team
 */

package org.klab.ui_demo.model;

import java.io.Serializable;


/**
 * Item
 *
 * @author <a href="mailto:kodama-t@klab.jp">Takuya KODAMA</a> (kodamta-t)
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 */
public class Item implements Serializable {

    /** */
    private int id;
    /** */
    private String imageUrl;
    /** */
    private String thumbnailUrl;
    /** */
    private String title;
    /** */
    private String date;

    /** */
    public int getId() {
        return id;
    }

    /** */
    public void setId(int id) {
        this.id = id;
    }

    /** */
    public String getImageUrl() {
        return imageUrl;
    }

    /** */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /** */
    public String getTitle() {
        return title;
    }

    /** */
    public void setTitle(String title) {
        this.title = title;
    }

    /** */
    public String getDate() {
        return date;
    }

    /** */
    public void setDate(String date) {
        this.date = date;
    }
    
    /** */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /** */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Item)) {
            return false;
        }
        return id == ((Item) o).id;
    }
}
