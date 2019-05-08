package com.spacesoftwares.spacecapture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spacesoftwares.spacecapture.service.WhiteService;


public class MainActivity extends AppCompatActivity {

    private Button mBtnWhite, mBtnStopWhite, mBtnExit;
    private SeekBar seekBar;
    private TextView tv_sound;
    private AudioManager mAudioManager;
    private int maxVolume,currentVolume;

    private final static String TAG = MainActivity.class.getSimpleName();
    private Intent whiteIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnWhite = findViewById(R.id.btn_white);
        mBtnStopWhite = findViewById(R.id.btn_stop_white);
        seekBar = findViewById(R.id.seekBar);
        tv_sound = findViewById(R.id.textView);

        mBtnExit = findViewById(R.id.btn_exit);

        //获取系统的Audio管理者
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        //当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        //seekbar设置最大值为最大音量，这样设置当前进度时不用换算百分比了
        seekBar.setMax(maxVolume);
        //seekbar设置当前进度为当前音量
        setView();

        //seekbar设置拖动监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar arg0,int progress,boolean fromUser)
            {
                //设置媒体音量为当前seekbar进度
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                setView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub


            }
        });

        setListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(--currentVolume>=0){
                    setView();
                }else {
                    currentVolume = 0;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(++currentVolume<=maxVolume){
                    setView();
                }else{
                    currentVolume = maxVolume;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                setView();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setView(){
        tv_sound.setText(currentVolume+"");
        seekBar.setProgress(currentVolume);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    }

    private void setListener(){
        OnClick onClick = new OnClick();
        mBtnWhite.setOnClickListener(onClick);
        mBtnStopWhite.setOnClickListener(onClick);
        mBtnExit.setOnClickListener(onClick);
    }

    private class  OnClick implements  View.OnClickListener{
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            switch(viewId){
                case R.id.btn_white:
                    Log.i(TAG, "MAIN: btn_white");
                    if(null == whiteIntent)
                        whiteIntent = new Intent(MainActivity.this, WhiteService.class);
                    startService(whiteIntent);
                    break;
                case R.id.btn_stop_white:
                    Log.i(TAG, "MAIN: btn_stop_white");
                    if(null != whiteIntent)
                       stopService(whiteIntent);
                break;
                case R.id.btn_exit:
                    Log.i(TAG, "MAIN: btn_exit");
                    if(null != whiteIntent)
                        stopService(whiteIntent);
                    finish();
                    break;
            }
        }
    }

}