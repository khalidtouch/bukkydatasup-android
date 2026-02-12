package com.kxtdev.bukkydatasup.network.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkSessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SessionManager {

    override fun saveToken(token: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_TOKEN, token)
        }
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    override fun saveUsername(username: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_USERNAME, username)
        }
    }

    override fun getUsername(): String? {
        return sharedPreferences.getString(KEY_AUTH_USERNAME, null)
    }

    override fun savePassword(password: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_PASSWORD, password)
        }
    }

    override fun getPassword(): String? {
        return sharedPreferences.getString(KEY_AUTH_PASSWORD, null)
    }

    override fun saveLoggedInState(state: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_AUTH_LOGGED_IN_STATE, state)
        }
    }

    override fun getLoggedInState(): String? {
        return sharedPreferences.getString(KEY_AUTH_LOGGED_IN_STATE, null)
    }

    companion object {
        const val KEY_AUTH_TOKEN = "pref_auth_token"
        const val KEY_AUTH_USERNAME = "pref_auth_username"
        const val KEY_AUTH_PASSWORD = "pref_auth_password"
        const val KEY_AUTH_LOGGED_IN_STATE = "pref_auth_logged_in_state"
    }
}

interface SessionManager {
    fun saveToken(token: String?)
    fun getToken(): String?
    fun saveUsername(username: String?)
    fun getUsername(): String?
    fun savePassword(password: String?)
    fun getPassword(): String?
    fun saveLoggedInState(state: String?)
    fun getLoggedInState(): String?
}