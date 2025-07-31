package com.baghdad.novix.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.baghdad.viewmodel.util.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ConnectivityObserverImpl(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {
    override fun isConnected(): Flow<Boolean> {
        return callbackFlow<Boolean> {
            val callBack = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    trySend(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

                }
            }

            connectivityManager.registerDefaultNetworkCallback(callBack)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }

        }
    }
}