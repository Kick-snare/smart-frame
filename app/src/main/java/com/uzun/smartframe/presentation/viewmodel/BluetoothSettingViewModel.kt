package com.uzun.smartframe.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uzun.smartframe.Repository
import com.uzun.smartframe.util.Event
import com.uzun.smartframe.util.Util
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.nio.charset.Charset

class BluetoothSettingViewModel(private val repository: Repository): ViewModel() {

	val connected: LiveData<Boolean?>
		get() = repository.connected
	val progressState: LiveData<String>
		get() = repository.progressState

	var _btnConnected = mutableStateOf(false)
	val btnConnected : State<Boolean> = _btnConnected

	var inProgressView = ObservableBoolean(false)

	var txtProgress: ObservableField<String> = ObservableField("")

	private val _requestBleOn = MutableLiveData<Event<Boolean>>()
	val requestBleOn: LiveData<Event<Boolean>>
		get() = _requestBleOn

	val inProgress: LiveData<Event<Boolean>>
		get() = repository.inProgress

	val connectError: LiveData<Event<Boolean>>
		get() = repository.connectError

	val stateData: LiveData<List<Double>?>
		get() = repository.stateData
	val valueData: LiveData<List<Double>?>
		get() = repository.valueData

	private val _state = MutableStateFlow<String>("")
	val state: StateFlow<String> = _state

	fun setInProgress(en: Boolean) {
		repository.inProgress.value = Event(en)
	}

	fun btnConnect(on: Boolean) {
		_btnConnected.value = on
	}

	fun onClickConnect() {
		if (connected.value == false || connected.value == null) {
			if (repository.isBluetoothSupport()) {   // 블루투스 지원 체크
				if (repository.isBluetoothEnabled()) { // 블루투스 활성화 체크
					//Progress Bar
					setInProgress(true)
					Util.showNotification("디바이스 스캔 시작")
					repository.scanDevice()
				} else {
					// 블루투스를 지원하지만 비활성 상태인 경우
					// 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
					_requestBleOn.value = Event(true)
				}
			} else { //블루투스 지원 불가
				Util.showNotification("블루투스가 지원되지 않습니다")
			}
		} else {
			repository.disconnect()
		}
	}

	fun unregisterReceiver() {
		repository.unregisterReceiver()
	}

	fun onClickSendData(sendTxt: String) {
		val byteArr = sendTxt.toByteArray(Charset.defaultCharset())
		repository.sendByteData(byteArr)
		Util.showNotification("send data!")
	}
}




