package com.wangtotang.ttchatdemo.util;


import com.wangtotang.ttchatdemo.config.Config;

/**
 * Created by Wangto Tang on 2015/4/6.
 */
public class PuzzleGenerator {

    /**
     * 得到一个可解的问题数据
     * @return
     */
    public int[][] getPuzzleData() {

		int[][] data = getTarget();
		for(int i=0;i<data.length;i++) {
			for(int j=0;j<data.length;j++) {
				int index1 = (int)(Math.random() * data.length);
				int index2 = (int)(Math.random() * data.length);
				int temp = data[index1][index2];
				data[index1][index2] = data[i][j];
				data[i][j] = temp;
			}
		}


        //检查问题是否可解
        if(canSolve(data)) {
            return data;
        } else {
            return getPuzzleData();
        }

    }

    /**
     * 讨论问题的可解性
     * @param state 状态
     */
    private boolean canSolve(int[][] state) {

        int blank_row = 0; //"空格"所在的行数

        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state.length;j++) {
                if(state[i][j] == 0) {
                    blank_row = i;
                }
            }
        }
        if(state.length % 2 == 1) { //问题宽度为奇数
            return (getInversions(state) % 2 == 0);
        } else { //问题宽度为偶数
            if((state.length - blank_row) % 2 == 1) { //从底往上数,空格位于奇数行
                return (getInversions(state) % 2 == 0);
            } else { //从底往上数,空位位于偶数行
                return (getInversions(state) % 2 == 1);
            }
        }
    }

    /**
     * 计算问题的"倒置变量和"
     * @param state
     */
    private int getInversions(int[][] state) {
        int inversion = 0;
        int temp = 0;
        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state[i].length;j++) {
                int index = i* state.length + j + 1;
                while(index < (state.length * state.length)) {
                    if(state[index/state.length][index%state.length] != 0
                            && state[index/state.length]
                            [index%state.length] < state[i][j]) {
                        temp ++;
                    }
                    index ++;
                }
                inversion = temp + inversion;
                temp = 0;
            }
        }
        return inversion;
    }

    public int[][] getTarget(){
        int[][] data = new int[Config.SIZE][Config.SIZE];
        for(int i=0;i<data.length;i++) {
            for(int j=0;j<data.length;j++) {
                data[i][j] = i*data.length + j+1;
            }
        }
        data[data.length-1][data.length-1] = 0;
        return data;
    }

    public boolean isWin(int[][] state){

        int[][] tState = getTarget();
        //和目标矩阵比较，看是否相同，如果相同则表示问题已解
        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state.length;j++) {
                if(state[i][j] != tState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
