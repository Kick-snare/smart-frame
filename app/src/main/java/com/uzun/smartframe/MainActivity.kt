package com.uzun.smartframe

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.uzun.smartframe.SmartFrameApp.SmartFrameApp
import com.uzun.smartframe.presentation.viewmodel.BluetoothSettingViewModel
import com.uzun.smartframe.util.Util

class MainActivity : ComponentActivity() {

	private val viewModel = BluetoothSettingViewModel(repository = Repository())

	val REQUEST_ALL_PERMISSION = 1
	@RequiresApi(Build.VERSION_CODES.S)
	val permissions = arrayOf(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.BLUETOOTH,
		Manifest.permission.BLUETOOTH_SCAN,
		Manifest.permission.BLUETOOTH_CONNECT,
		Manifest.permission.BLUETOOTH_ADMIN,
	)

	@RequiresApi(Build.VERSION_CODES.S)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)



		if (!hasPermissions(this, permissions)) {
			requestPermissions(permissions, REQUEST_ALL_PERMISSION)
		}
		initObserving()
		setContent { SmartFrameApp(viewModel) }
	}

	private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			val intent = result.data
			viewModel.onClickConnect()
		}
	}



	private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
		for (permission in permissions) {
			if (context?.let { ActivityCompat.checkSelfPermission(it, permission) }
				!= PackageManager.PERMISSION_GRANTED
			) {
				return false
			}
		}
		return true
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String?>,
		grantResults: IntArray,
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		when (requestCode) {
			REQUEST_ALL_PERMISSION -> {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "블루투스 권한 획득 성공", Toast.LENGTH_SHORT).show()
				} else {
					requestPermissions(permissions, REQUEST_ALL_PERMISSION)
					Toast.makeText(this, "블루투스 권한 획득 실패", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private fun initObserving() {

		//Progress
		viewModel.inProgress.observe(this) {
			if (it.getContentIfNotHandled() == true)
				viewModel.inProgressView.set(true)
			else
				viewModel.inProgressView.set(false)
		}
		//Progress text
		viewModel.progressState.observe(this) {
			viewModel.txtProgress.set(it)
		}

		//Bluetooth On 요청
		viewModel.requestBleOn.observe(this) {
			val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
			startForResult.launch(enableBtIntent)
		}

		//Bluetooth Connect/Disconnect Event
		viewModel.connected.observe(this) {
			if (it != null) {
				if (it) {
					viewModel.setInProgress(false)
					viewModel.btnConnect(true)
					Util.showNotification("디바이스와 연결되었습니다.")
				} else {
					viewModel.setInProgress(false)
					viewModel.btnConnect(false)
					Util.showNotification("디바이스와 연결이 해제되었습니다.")
				}
			}
		}

		//Bluetooth Connect Error
		viewModel.connectError.observe(this) {
			Util.showNotification("Connect Error. Please check the device")
			viewModel.setInProgress(false)
		}
	}
}
