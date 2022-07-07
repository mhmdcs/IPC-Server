package com.example.ipcserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Broadcasts can be sent by the system, or by applications.
// And other applications can register to listen to those specific broadcasts with BroadcastReceiver.
// Our client application will send the broadcasts with intents, and our server application will receive them with intents usingg BroadcastReceiver.
// In other words, we're initiating one-way communication channel.
class IPCBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        RecentClient.client = Client(
            clientPackageName = intent?.getStringExtra(PACKAGE_NAME),
            clientProcessId = intent?.getStringExtra(PID),
            clientData = intent?.getStringExtra(DATA),
            ipcMethod = "Broadcast"
        )
    }
}