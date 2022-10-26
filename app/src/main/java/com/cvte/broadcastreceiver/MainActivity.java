package com.cvte.broadcastreceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IntentFilter intentFilter;
    private NetWorkChangeCallback netWorkChangeCallback;
    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastReceiver localBroadcastReceiver;
    //        <-- dynamic register BroadcastReceiver>
    //    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        netWorkChangeCallback = NetWorkChangeCallback.getInstance(this);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.build();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            connMgr.registerNetworkCallback(request, netWorkChangeCallback);
        }
        /*<-- dynamic register BroadcastReceiver>
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);*/

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cvte.broadcastreceiver.localbroadcast");
        localBroadcastReceiver = new LocalBroadcastReceiver();
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter);

        Button sendBroadcastReceiver = findViewById(R.id.sendBroadcastReceiver);
        sendBroadcastReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "MainActivity Button sendBroadcastReceiver onClick");
                Intent intent = new Intent("com.cvte.broadcastreceiver.localbroadcast");
                localBroadcastManager.sendBroadcast(intent);
                /*//发送有序广播
                Intent intent = new Intent("com.cvte.broadcasttest.OrderedBroadcast");
                //新版android标准广播需要设置包名（相较于《第一行代码》里的android版本）
                intent.setPackage(getPackageName());
                sendOrderedBroadcast(intent,null);*/

                /*//发送标准广播
                Intent intent = new Intent("com.cvte.broadcastreceiver.Standard_Broadcast");
                //新版android标准广播需要设置包名（相较于《第一行代码》里的android版本）
                intent.setPackage(getPackageName());
                sendBroadcast(intent);*/

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localBroadcastReceiver);
        /*<-- dynamic register BroadcastReceiver>
        unregisterReceiver(networkChangeReceiver);*/
    }

    static class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Receiver a Local Broadcast...",Toast.LENGTH_SHORT).show();
        }
    }

    /*<-- dynamic register BroadcastReceiver>
    static class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Network Changed", Toast.LENGTH_SHORT).show();
        }
    }*/

    static class NetWorkChangeCallback extends ConnectivityManager.NetworkCallback {
        private Context context;
        private static NetWorkChangeCallback netWorkChangeCallback;

        public static NetWorkChangeCallback getInstance(Context context) {
            return netWorkChangeCallback = new NetWorkChangeCallback(context);
        }
        public NetWorkChangeCallback (Context context) {
            this.context = context;
        }
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Toast.makeText(context,"Network Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Toast.makeText(context,"Network Disconnected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Toast.makeText(context,"Wifi Connected", Toast.LENGTH_SHORT).show();
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Toast.makeText(context,"Data traffic is connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Other Network", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}