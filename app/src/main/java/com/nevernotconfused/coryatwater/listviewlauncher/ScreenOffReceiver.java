package com.nevernotconfused.coryatwater.listviewlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by coryatwater on 3/23/16.
 */
public class ScreenOffReceiver extends BroadcastReceiver{
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
            Log.e("TAG", "onReceive: screen turnedoof" );
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            wasScreenOn = true;
            Log.e("TAG", "onReceive: Screen turned on");
        }
    }
}
