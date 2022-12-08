package com.uzun.smartframe.util

import android.widget.Toast
import com.uzun.smartframe.SmartFrameApplication

class Util {
	companion object{
		fun showNotification(msg: String) {
			Toast.makeText(SmartFrameApplication.applicationContext(), msg, Toast.LENGTH_SHORT).show()
		}
	}
}