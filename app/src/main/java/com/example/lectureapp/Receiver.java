package com.example.lectureapp;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the action of the intent (the event that triggered the broadcast either network or bluetooth)
        String action = intent.getAction();

        // used to handle network events
        receiveNetwork(context);

        // used to handle bluetooth events
        receiveBluetooth(intent, action, context);
    }


    private void receiveNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Register a callback to listen for network changes
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                NetworkCapabilities capabilities =
                        connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Toast.makeText(context, "Connected via WI-FI", Toast.LENGTH_SHORT).show();
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Toast.makeText(context, "Connected via Cellular", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onLost(Network network) {
                Toast.makeText(context, "Network disconnected", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void receiveBluetooth(Intent intent, String action, Context context) {
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
                Toast.makeText(context, "Bluetooth device connected", Toast.LENGTH_SHORT).show();
            }
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
                Toast.makeText(context, "Bluetooth device disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
