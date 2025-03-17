package com.example.lectureapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TestService testService;
    private boolean isBound = false;
    Receiver receiver;

    // Service connection for binding
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TestService.LocalBinder binder = (TestService.LocalBinder) service;
            testService = binder.getService();
            isBound = true;
            Log.d("TestService_m", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.d("TestService_m", "Service unbound");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Lifecycle", "onCreate() called!");

        //create an instance of the receiver to register and unregister it dynamically
        receiver = new Receiver();

        // Button to launch the dialog Activity
        findViewById(R.id.btn_open_dialog).setOnClickListener(v -> {
            Intent intent = new Intent(this, FloatingActivity.class);
            startActivity(intent);
        });

        // Button to launch the Second Activity
        findViewById(R.id.btn_next_act).setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        });

        // Start the service
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            startService(new Intent(this, TestService.class));
        });

        // Stop the service
        findViewById(R.id.btn_stop).setOnClickListener(v -> {
            stopService(new Intent(this, TestService.class));
        });

        // Bind to the service
        findViewById(R.id.btn_bind).setOnClickListener(v -> {
            bindService(new Intent(this, TestService.class), connection, BIND_AUTO_CREATE);
        });

        // Unbind from the service
        findViewById(R.id.btn_unbind).setOnClickListener(v -> {
            if (isBound) {
                unbindService(connection);
                isBound = false;
            }
        });

        // Call a service method when bound
        findViewById(R.id.btn_get_timestamp).setOnClickListener(v -> {
            if (isBound) {
                String timestamp = testService.getCurrentTimestamp();
                Toast.makeText(this, "Timestamp: " + timestamp, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Service not bound", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Lifecycle", "onStart() called!");

        // dynamically register the NetworkReceive
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle", "onResume() called!");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle", "onPause() called!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lifecycle", "onStop() called!");

        // dynamically Unregister the NetworkReceive
        unregisterReceiver(receiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Lifecycle", "onRestart() called!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lifecycle", "onDestroy() called!");
    }
}