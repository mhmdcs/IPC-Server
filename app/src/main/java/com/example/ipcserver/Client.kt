package com.example.ipcserver

data class Client(
    val clientPackageName: String?,
    val clientProcessId: String?,
    val clientData: String?,
    val ipcMethod: String,
)

// Create a singleton RecentClient class where we will keep the last connected client. A list of connected clients could also be kept here,
// but in this example, we will only show the details of the last connected client on the UI.
object RecentClient{
    var client: Client? = null
}