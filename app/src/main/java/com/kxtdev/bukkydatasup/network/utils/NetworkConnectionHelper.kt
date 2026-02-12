package com.kxtdev.bukkydatasup.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.NetworkOnMainThreadException
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException
import javax.inject.Inject


@Suppress("DEPRECATION")
class NetworkConnectionHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val manager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as
            ConnectivityManager

    fun isConnected(): Boolean = isConnectedToNetwork() && isConnectedToInternet()

    private fun isConnectedToNetwork(): Boolean {
        return manager.activeNetworkInfo != null && manager.activeNetworkInfo?.isConnected ?: false
    }

    private fun isConnectedToInternet(): Boolean {
        return try {
            !InetAddress.getByName("www.google.com").equals("")
        } catch(e: UnknownHostException) {
            e.printStackTrace()
            Log.e("NetworkConnectionHelper", "exception UnknownHostException: ${e.message}")
            false
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("NetworkConnectionHelper", "exception IOException: ${e.message}")
            false
        } catch (e: NetworkOnMainThreadException) {
            e.printStackTrace()
            Log.e("NetworkConnectionHelper", "exception NetworkOnMainThreadException: ${e.message} ${e.cause?.message}")
            false
        }  catch (e: Exception) {
            e.printStackTrace()
            Log.e("NetworkConnectionHelper", "exception Exception: ${e.message} ${e::class.java.simpleName}")
            false
        }
    }
}