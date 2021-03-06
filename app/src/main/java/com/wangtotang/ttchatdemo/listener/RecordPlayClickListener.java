package com.wangtotang.ttchatdemo.listener;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.util.BmobUtils;

/**
 * Created by Wangto Tang on 2015/3/31.
 */
public class RecordPlayClickListener implements View.OnClickListener {
    BmobMsg message;
    ImageView iv_voice;
    private AnimationDrawable anim = null;
    Context context;
    String currentObjectId = "";
    MediaPlayer mediaPlayer = null;
    public static boolean isPlaying = false;
    public static RecordPlayClickListener currentPlayListener = null;
    static BmobMsg currentMsg = null;// 用于区分两个不同语音的播放
    BmobUserManager userManager;

    public RecordPlayClickListener(Context context, BmobMsg msg, ImageView voice) {
        this.iv_voice = voice;
        this.message = msg;
        this.context = context;
        currentMsg = msg;
        currentPlayListener = this;
        currentObjectId = BmobUserManager.getInstance(context).getCurrentUserObjectId();
        userManager = BmobUserManager.getInstance(context);
    }

    /**
     * 播放语音
     *
     * @Title: playVoice
     * @Description: TODO
     * @param @param filePath
     * @return void
     * @throws
     */
    public void startPlayRecord(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        audioManager.setSpeakerphoneOn(false);// 关闭扬声器
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);

        try {
            mediaPlayer.reset();
            FileInputStream fis = new FileInputStream(new File(filePath));
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer arg0) {
                    isPlaying = true;
                    currentMsg = message;
                    arg0.start();
                    startRecordAnimation();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlayRecord();
                        }

                    });
            currentPlayListener = this;
        } catch (Exception e) {
        }
    }

    /**
     * 停止播放
     *
     * @Title: stopPlayRecord
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void stopPlayRecord() {
        stopRecordAnimation();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }

    /**
     * 开启播放动画
     *
     * @Title: startRecordAnimation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void startRecordAnimation() {
        if (message.getBelongId().equals(currentObjectId)) {
            iv_voice.setImageResource(R.drawable.anim_chat_voice_right);
        } else {
            iv_voice.setImageResource(R.drawable.anim_chat_voice_left);
        }
        anim = (AnimationDrawable) iv_voice.getDrawable();
        anim.start();
    }

    /**
     * 停止播放动画
     *
     * @Title: stopRecordAnimation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void stopRecordAnimation() {
        if (message.getBelongId().equals(currentObjectId)) {
            iv_voice.setImageResource(R.drawable.voice_right3);
        } else {
            iv_voice.setImageResource(R.drawable.voice_left3);
        }
        if (anim != null) {
            anim.stop();
        }
    }

    @Override
    public void onClick(View arg0) {
        if (isPlaying) {
            currentPlayListener.stopPlayRecord();
            if (currentMsg != null && currentMsg.hashCode() == message.hashCode()) {
                currentMsg = null;
                return;
            }
        }
        if (message.getBelongId().equals(currentObjectId)) {// 如果是自己发送的语音消息，则播放本地地址
            String localPath = message.getContent().split("&")[0];
            startPlayRecord(localPath);
        } else {// 如果是收到的消息，则需要先下载后播放
            String localPath = getDownLoadFilePath(message);
            startPlayRecord(localPath);
        }
    }

    public String getDownLoadFilePath(BmobMsg msg) {
        String accountDir = BmobUtils.string2MD5(userManager.getCurrentUserObjectId());
        File dir = new File(Config.MyVoiceDir + File.separator + accountDir + File.separator + msg.getBelongId());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 在当前用户的目录下面存放录音文件
        File audioFile = new File(dir.getAbsolutePath() + File.separator + msg.getMsgTime() + ".amr");
        try {
            if (!audioFile.exists()) {
                audioFile.createNewFile();
            }
        } catch (IOException e) {
        }
        return audioFile.getAbsolutePath();
    }
}
