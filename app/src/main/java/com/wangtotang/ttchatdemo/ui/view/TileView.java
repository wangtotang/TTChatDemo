package com.wangtotang.ttchatdemo.ui.view;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Wangto Tang on 2015/4/6.
 */
public class TileView extends ImageView {

    private int row; //当前对象的行信息
    private int column; //当前对象的列信息

    private int data;

    public TileView(Context context,int data,int row,int column) {
        super(context);
        this.data = data;
        this.row = row;
        this.column = column;
    }

    public void setLocation(int row,int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
