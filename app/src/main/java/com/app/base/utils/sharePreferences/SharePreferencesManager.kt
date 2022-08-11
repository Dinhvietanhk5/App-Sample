package com.app.base.utils.sharePreferences

import android.content.Context
import android.content.SharedPreferences

val PREF_NAME = "vu_media"

class SharePreferencesManager {
    companion object {
        var sInstance: SharePreferencesManager? = null
        var mPref: SharedPreferences? = null

        @Synchronized
        fun initializeInstance(context: Context?) {
            if (sInstance == null) {
                sInstance =
                    SharePreferencesManager(
                        context!!
                    )
            }
        }

        @Synchronized
        fun getInstance(): SharePreferencesManager {
            checkNotNull(sInstance) {
                SharePreferencesManager::class.java.simpleName +
                        " is not initialized, call initializeInstance(..) method first."
            }
            return sInstance as SharePreferencesManager
        }
    }

    constructor(context: Context) {
        mPref = context.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setValue(key: SPKeyEnum, value: String?) {
        mPref!!.edit()
            .putString(key.toString(), value)
            .apply()
    }

    fun setValue(key: SPKeyEnum, value: Int) {
        mPref!!.edit()
            .putInt(key.toString(), value)
            .apply()
    }

    fun setValue(key: SPKeyEnum, value: Long) {
        mPref!!.edit()
            .putLong(key.toString(), value)
            .apply()
    }

    fun setValue(key: SPKeyEnum, value: Boolean?) {
        mPref!!.edit()
            .putBoolean(key.toString(), value!!)
            .apply()
    }


    fun getValue(key: SPKeyEnum, defau: String?): String? {
        return mPref!!.getString(key.toString(), defau)
    }

    fun getValue(key: SPKeyEnum, defau: Int): Int {
        return mPref!!.getInt(key.toString(), defau)
    }

    fun getValue(key: SPKeyEnum, defau: Long): Long {
        return mPref!!.getLong(key.toString(), defau)
    }

    fun getValue(key: SPKeyEnum, defau: Boolean): Boolean {
        return mPref!!.getBoolean(key.toString(), defau)
    }

    fun getBoolean(key: SPKeyEnum, defau: Boolean?): Boolean? {
        return mPref!!.getBoolean(key.toString(), defau!!)
    }

    fun getLong(key: SPKeyEnum, defau: Long?): Long? {
        return mPref!!.getLong(key.toString(), defau!!)
    }


    fun getValue(key: SPKeyEnum): String? {
        return mPref!!.getString(key.toString(), "")
    }


    fun setValueBool(key: SPKeyEnum, value: Boolean) {
        mPref!!.edit()
            .putBoolean(key.toString(), value)
            .apply()
    }

    fun getValueBool(key: SPKeyEnum): Boolean {
        return mPref!!.getBoolean(key.toString(), false)
    }

    fun remove(key: SPKeyEnum) {
        mPref!!.edit()
            .remove(key.toString())
            .apply()
    }

    fun clear(): Boolean {
        return mPref!!.edit()
            .clear()
            .commit()
    }

}