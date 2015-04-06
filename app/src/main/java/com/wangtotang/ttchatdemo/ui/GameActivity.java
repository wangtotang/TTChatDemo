package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.ui.view.GamePanel;

/**
 * Created by Wangto Tang on 2015/4/6.
 */
public class GameActivity extends CheckActivity {

    GamePanel gamePanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();

    }

    private void initView() {
        initTopBarForLeft("解锁游戏");
        RelativeLayout gameContainer = (RelativeLayout)this.findViewById(R.id.gamePanelContainer);
        gamePanel = new GamePanel(this,this);
        gameContainer.addView(gamePanel);
    }

}
