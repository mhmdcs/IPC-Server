package com.example.ipcserver

import android.app.Service
import android.content.Intent
import android.os.*
import android.text.TextUtils

// Do not forget to declare the service to our Manifest file, mark it as exported so it's accessible to other processes, and add an intent filter.
class IPCServerService : Service() {

    companion object {
        var connectionCount: Int = 0
        const val NOT_SENT = "No Data Sent"
    }

    // ------------------------------------------------------------------------------------
    // Aidl method

    // Create the binder object by extending from our generated Stub class, which itself extends Binder class
    // Here we will fill our AIDL methods. Note: If you are going to perform concurrent operations, Coroutines should be used in our AIDL methods.
    // AIDL IPC - Binder object to pass to the client
    private val aidlBinder = object : AIDLInterface.Stub() {

        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = IPCServerService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT
                else data

            // Update our RecentClient object when the client is bound.
            RecentClient.client = Client(
                packageName,
                pid.toString(),
                clientData,
                "AIDL"
            )

        }

    }

    // ------------------------------------------------------------------------------------
    // Messenger method

    // Messenger IPC - Messenger object contains binder that we will send to client
    private val mMessenger = Messenger(IncomingHandler())

    // Messenger IPC - Message Handler
    internal class IncomingHandler : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msgFromClient: Message) {
            super.handleMessage(msgFromClient)

            // Get message from client. Save recent connected client info.
            val receivedBundle = msgFromClient.data
            RecentClient.client = Client(
                receivedBundle.getString(PACKAGE_NAME),
                receivedBundle.getInt(PID).toString(),
                receivedBundle.getString(DATA),
                "Messenger"
            )

            // Send message to the client. The message contains server info.
            val msgToClient = Message.obtain(this@IncomingHandler, 0)
            val sentBundle = Bundle()
            sentBundle.putInt(CONNECTION_COUNT, connectionCount)
            sentBundle.putInt(PID, Process.myPid())
            msgToClient.data = sentBundle
            // The service can also save the msg.replyTo object to a local variable so that it can send a message to the client at any time.
            msgFromClient.replyTo.send(msgToClient)
        }
    }

    // ------------------------------------------------------------------------------------


    // Return our binder object to the client using the onBind() method. So they can communicate with this service.
    override fun onBind(intent: Intent?): IBinder? {
        connectionCount++
        // Choose which binder we need to return based on the type of the IPC the client make
        return when (intent?.action) {
            "aidlExample" -> aidlBinder
            "messengerExample" -> mMessenger.binder
            else -> null
        }
    }


    // Unbind so the UI is updated, and set our RecentClient object to null.
    override fun onUnbind(intent: Intent?): Boolean {
        RecentClient.client = null
        return super.onUnbind(intent)
    }


}