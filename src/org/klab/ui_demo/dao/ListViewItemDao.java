/*
 * Copyright (c) 2011 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.ui_demo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.klab.ui_demo.model.Item;


/**
 * IF002.
 * PlayroomItem用DAO
 * カテゴリ毎にシングルトンインスタンスを保持します.
 * 
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (sano-n)
 * @version 0.00 2011/06/16 sano-n initial version <br>
 */
public class ListViewItemDao extends ItemDao {

    public static int PAGE_SIZE = 10;

    /** インスタンスを保持します */
    private static ListViewItemDao instance = new ListViewItemDao();
    
    /**
     * インスタンスを返します
     */
    public static ListViewItemDao getInstance() {
        return instance;
    }
    
    /**
     * 指定されたoffset以降のデータのリストを返します.
     * リスト内アイテム数は必ず Constants.MAX_SIZE_AT_ONCE 以下になります.
     * 
     * Gets 10 of items
     */
    public List<Item> getItems(int offset) throws IOException {
        return getItems(offset, PAGE_SIZE);
    }

    /**
     * 指定されたoffset以降のデータのリストを返します.
     * リスト内アイテムの最大個数は size 以下になります.
     * 
     * Gets size of items
     */
    public List<Item> getItems(int offset, int size) throws IOException {
        List<Item> result = new ArrayList<Item>();
        for (int i = offset; i < Math.min(offset + size, 99); i++) {
            result.add(newItem(i));
        }
        return result;
    }
}

/* */
