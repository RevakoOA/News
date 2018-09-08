package com.just_me.news.utils

import android.content.Context
import android.telephony.TelephonyManager
import com.just_me.news.news.R


class CountryCodeUtils {
    companion object {
        fun GetCountryID(context: Context): String {
            val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return manager.simCountryIso.toUpperCase()
        }
    }
}