package com.spacesoftwares.spacecapture.key;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Objects;

public class PowerKeyObserver {
    private String TAG_POWER = "PowerKey";
    private Context mContext;
    private IntentFilter mIntentFilter;
    private OnPowerKeyListener mOnPowerKeyListener;
    private PowerKeyBroadcastReceiver mPowerKeyBroadcastReceiver;
    public PowerKeyObserver(Context context) {
        this.mContext = context;
    }

    //注册广播接收者
    public void startListen(){
        mIntentFilter=new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mPowerKeyBroadcastReceiver=new PowerKeyBroadcastReceiver();
        mContext.registerReceiver(mPowerKeyBroadcastReceiver, mIntentFilter);
        Log.i(TAG_POWER, "PowerKey----> 开始监听");
        System.out.println("PowerKey----> 开始监听");
    }

    //取消广播接收者
    public void stopListen(){
        if (mPowerKeyBroadcastReceiver!=null) {
            mContext.unregisterReceiver(mPowerKeyBroadcastReceiver);
            Log.i(TAG_POWER, "PowerKey----> 停止监听");
            System.out.println("PowerKey----> 停止监听");
        }
    }

    // 对外暴露接口
    public void setPowerKeyListener(OnPowerKeyListener powerKeyListener) {
        mOnPowerKeyListener = powerKeyListener;
    }

    // 回调接口
    public interface OnPowerKeyListener {
        void onPowerKeyPressed();
    }

    //广播接收者
    class PowerKeyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Objects.equals(action, Intent.ACTION_SCREEN_OFF)) {
                mOnPowerKeyListener.onPowerKeyPressed();
                //abortBroadcast();
            }
        }
    }
}

