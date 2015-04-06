package com.wangtotang.ttchatdemo.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.ui.SetMyInfoActivity;
import com.wangtotang.ttchatdemo.util.PuzzleGenerator;


/**
 * Created by Wangto Tang on 2015/4/6.
 */
public class GamePanel extends RelativeLayout implements View.OnClickListener {


    //当前游戏数据
    private int[][] mData;
    //游戏中大图片被切割后的小图片数组
    private Bitmap[] bitmaps;
    //当前"空格"的位置
    private int blank_row;
    private int blank_column;
    private Context mContext;
    TileView[][] tiles;
    private PuzzleGenerator generator;
    private int tileWidth;
    private int tileHeight;
    int direction = Config.UNMOVABLE;
    private Activity mActivity;
    public GamePanel(Context context,Activity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
        init(context);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(tileWidth * Config.SIZE, tileHeight * Config.SIZE);
    }


     //重新随机生成问题数据
    public void init(Context context) {

        this.removeAllViews();
        generator = new PuzzleGenerator();
        this.mData = generator.getPuzzleData();
        for(int i=0;i<mData.length;i++) {
            for(int j=0;j<mData.length;j++) {
                if(mData[i][j] == 0) {
                    blank_row = i;
                    blank_column = j;
                }
            }
        }
        this.setBackgroundColor(Config.panelBG);


        bitmaps = new Bitmap[Config.SIZE * Config.SIZE];

        tiles = new TileView[Config.SIZE][Config.SIZE];


        bitmaps[0]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_0);
        bitmaps[1]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_1);
        bitmaps[2]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_2);
        bitmaps[3]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_3);
        bitmaps[4]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_4);
        bitmaps[5]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_5);
        bitmaps[6]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_6);
        bitmaps[7]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_7);
        bitmaps[8]=BitmapFactory.decodeResource(context.getResources(), R.drawable.num_8);

        tileWidth = bitmaps[0].getWidth();
        tileHeight = bitmaps[0].getHeight();

        LayoutParams containerParams =
                new LayoutParams(Config.SIZE * tileWidth,Config.SIZE * tileHeight);
        this.setLayoutParams(containerParams);

        for(int i=0;i<Config.SIZE;i++) {
            for(int j=0;j<Config.SIZE;j++) {
                tiles[i][j] = new TileView(context,mData[i][j],i,j);
                tiles[i][j].setImageBitmap(bitmaps[mData[i][j]]);
                tiles[i][j].setOnClickListener(this);
                LayoutParams layoutParams =
                        new LayoutParams(tileWidth, tileHeight);
                layoutParams.leftMargin = tileWidth * j;
                layoutParams.topMargin = tileHeight * i;
                tiles[i][j].setLayoutParams(layoutParams);

                if(mData[i][j] != 0) { //不添加"空格"
                    this.addView(tiles[i][j]);
                }
            }
        }
    }


     // 得到当前单击单元的可以移动的方向
    public int getMoveDirection(int row,int column) {
        if(row > 0 && tiles[row-1][column].getData() == 0) {
            return Config.UP;
        } else if(row < (mData.length-1) && tiles[row+1][column].getData() == 0) {
            return Config.DOWN;
        } else if(column > 0 && tiles[row][column-1].getData() == 0) {
            return Config.LEFT;
        } else if(column < (mData.length-1)&& tiles[row][column+1].getData() == 0) {
            return Config.RIGHT;
        } else {
            return Config.UNMOVABLE;
        }
    }


     //移动数据，交换数据元素
    private void moveData(int[][] array,TileView[][] tiles,int row,int column,int direction) {
        int temp = 0;
        TileView tempView;

        switch(direction) {
            case Config.UP:
                temp = array[row][column];
                array[row][column] = array[row-1][column];
                array[row-1][column] = temp;

                //设置TileView的位置标记
                tempView = tiles[row][column];
                tiles[row][column] = tiles[row-1][column];
                tiles[row][column].setRow(row+1);
                tiles[row-1][column] = tempView;
                tiles[row-1][column].setRow(row-1);
                blank_row ++;
                break;
            case Config.DOWN:
                temp = array[row][column];
                array[row][column] = array[row+1][column];
                array[row+1][column] = temp;

                //设置TileView的位置标记
                tempView = tiles[row][column];
                tiles[row][column] = tiles[row+1][column];
                tiles[row][column].setRow(row-1);
                tiles[row+1][column] = tempView;
                tiles[row+1][column].setRow(row+1);
                blank_row --;
                break;
            case Config.LEFT:
                temp = array[row][column];
                array[row][column] = array[row][column-1];
                array[row][column-1] = temp;

                tempView = tiles[row][column];
                tiles[row][column] = tiles[row][column-1];
                tiles[row][column].setColumn(column+1);
                tiles[row][column-1] = tempView;
                tiles[row][column-1].setColumn(column-1);
                blank_column ++;
                break;
            case Config.RIGHT:
                temp = array[row][column];
                array[row][column] = array[row][column+1];
                array[row][column+1] = temp;

                //设置TileView的位置标记
                tempView = tiles[row][column];
                tiles[row][column] = tiles[row][column+1];
                tiles[row][column].setColumn(column-1);
                tiles[row][column+1] = tempView;
                tiles[row][column+1].setColumn(column+1);
                blank_column --;
                break;
            case Config.UNMOVABLE:
                break;
        }
        if(generator.isWin(array)){
            Toast.makeText(mContext,"解锁成功",Toast.LENGTH_LONG).show();
            String from = mActivity.getIntent().getStringExtra("from");//me add other
            String username = mActivity.getIntent().getStringExtra("username");
             Intent intent =new Intent(mContext,SetMyInfoActivity.class);
             intent.putExtra("from", from);
             intent.putExtra("username",username);
             mContext.startActivity(intent);
             mActivity.finish();
        }
    }


    @Override
    public void onClick(View v) {
            if (v instanceof TileView) {
                int row = ((TileView) v).getRow();
                int column = ((TileView) v).getColumn();
                direction = getMoveDirection(row,column);
                move(direction, row, column);
            }
    }



    // 自动机解题调用方法
    public void move(int direction,int row,int column) {
        TileView v = tiles[row][column];
        LayoutParams layoutParams =
                new LayoutParams(tileWidth,tileHeight);
        moveData(mData, tiles, row, column, direction);
        switch(direction) {
            case Config.UP:
                layoutParams.leftMargin = v.getLeft();
                layoutParams.topMargin = v.getTop() - tileHeight;
                v.setLayoutParams(layoutParams);
                break;
            case Config.DOWN:
                layoutParams.leftMargin = v.getLeft();
                layoutParams.topMargin = v.getTop() + tileHeight;
                v.setLayoutParams(layoutParams);
                break;
            case Config.LEFT:
                layoutParams.leftMargin = v.getLeft() - tileWidth;
                layoutParams.topMargin = v.getTop();
                v.setLayoutParams(layoutParams);
                break;
            case Config.RIGHT:
                layoutParams.leftMargin = v.getLeft() + tileWidth;
                layoutParams.topMargin = v.getTop();
                v.setLayoutParams(layoutParams);
                break;
            case Config.UNMOVABLE:
                break;
        }
        GamePanel.this.invalidate();
    }
}
