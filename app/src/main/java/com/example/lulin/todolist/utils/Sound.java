package com.example.lulin.todolist.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.preference.PreferenceManager;

import com.example.lulin.todolist.R;


/**
 * 4.4 出现 MediaPlayer: Should have subtitle controller already set 错误
 * Don't care about this "Exception".
 *
 * @link https://stackoverflow.com/questions/20087804/should-have-subtitle-controller-already-set-mediaplayer-error-android
 */
public class Sound {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mNextPlayer;
    private int music_id;

    private OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mMediaPlayer = mNextPlayer;
            setNextMediaPlayer();
        }
    };

    public Sound(Context context) {
        mContext = context;
        music_id = (int) SPUtils.get(mContext,"music_id",1);
        build();
    }

//    public void setSound(int music){
//        mMediaPlayer = MediaPlayer.create(mContext, music);
//        build();
//    }

    /**
     * setLooping(true) 虽然能循环播放，但不能做到无缝播放处理，会出现短暂的暂停
     * 这里采用 setNextMediaPlayer 来解决这问题
     *
     * @link https://stackoverflow.com/questions/13409657/how-to-loop-a-sound-without-gaps-in-android
     * @link https://developer.android.com/reference/android/media/MediaPlayer.html#setNextMediaPlayer(android.media.MediaPlayer)
     */
    private void build() {
        mMediaPlayer = MediaPlayer.create(mContext, music_id);
        setNextMediaPlayer();
    }

    private void setNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(mContext, music_id);
        mMediaPlayer.setNextMediaPlayer(mNextPlayer);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
    }

    public void play() {
        if (isSoundOn()) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void resume() {
        if (isSoundOn()) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    /**
     * 不要调用 MediaPlayer.stop 停止播放音频，调用这个方法后的 MediaPlayer 对象无法再播放音频
     */
    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            release();
            build();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;

            mNextPlayer.stop();
            mNextPlayer.release();
            mNextPlayer = null;
        }
    }

    private boolean isSoundOn() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean("pref_key_tick_sound", true);
    }
}
