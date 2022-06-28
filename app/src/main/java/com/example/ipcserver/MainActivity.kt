package com.example.ipcserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ipcserver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        Log.i("MainActivity", "IPCServer PID: ${Process.myPid()}")

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val client = RecentClient.client

        binding.connectionStatus.text =
            if (client == null){
                binding.linearLayoutClientState.visibility = View.INVISIBLE
                getString(R.string.no_connected_client)
            } else {
                binding.linearLayoutClientState.visibility = View.VISIBLE
                getString(R.string.last_connected_client_info)
            }

        binding.txtPackageName.text = client?.clientPackageName
        binding.txtServerPid.text = client?.clientProcessId
        binding.txtData.text = client?.clientData
        binding.txtIpcMethod.text = client?.ipcMethod

    }
}