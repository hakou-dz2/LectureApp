package com.example.lectureapp;

import android.app.Service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class TestService extends Service {
    private int counter = 0;
    private boolean isRunning = false;

    // Binder given to clients (give the activities a way to communicate with the service)
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        TestService getService() {
            return TestService.this;
        }
    }
    // Create the service
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TestService", "Service onCreate()");
    }
    // UnBind the service
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("TestService", "Service onUnbind()");
        return super.onUnbind(intent);
    }
 // Stop the service
    @Override
    public void onDestroy() {
        Log.d("TestService", "Service onDestroy()");
        super.onDestroy();
        isRunning = false;
    }
    // Return the binder when the clients bind to the service (to communicate with the service)
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TestService", "Service onBind()");
        return binder;
    }
    // start the service and run the background task
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TestService", "Service onStartCommand()");
        if (!isRunning) {
            isRunning = true;
            startCounter();
        }
        return START_STICKY; // Restart if killed by the system
    }
    // Start a background task
    private void startCounter() {
        new Thread(() -> {
            while (isRunning) {
                try {
                    Log.d("TestService", "Counter: " + counter++);
                    Thread.sleep(5000); // Log every 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* Method for clients to get the current timestamp from the service
    * the clients can call this method only when they are bound to the service
     */
    public String getCurrentTimestamp() {
        return "Timestamp: " + System.currentTimeMillis();
    }

}
