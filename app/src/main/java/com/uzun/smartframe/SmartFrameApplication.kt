package com.uzun.smartframe

import android.app.Application
import android.content.Context

class SmartFrameApplication : Application() {

	init{
		instance = this
	}

	companion object {
		lateinit var instance: SmartFrameApplication
		fun applicationContext() : Context {
			return instance.applicationContext
		}
	}

	override fun onCreate() {
		super.onCreate()
	}

}