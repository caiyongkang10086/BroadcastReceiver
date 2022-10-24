package com.cvte.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StandardBroadcast extends BroadcastReceiver {
    private static final String TAG = "StandardBroadcastReceiv";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"StandardBroadcastReceiver onReceive...");
        Toast.makeText(context, "Receiver a standard Broadcast...", Toast.LENGTH_SHORT).show();
    }
}