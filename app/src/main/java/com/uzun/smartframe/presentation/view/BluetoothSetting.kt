package com.uzun.smartframe.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.uzun.smartframe.presentation.viewmodel.BluetoothSettingViewModel


@Composable
fun BluetoothSettingScreen(
	viewModel : BluetoothSettingViewModel,
) {
	Column(
		modifier = Modifier
			.fillMaxHeight(0.75f)
			.padding(vertical = 20.dp)
			.padding(horizontal = 10.dp)
	) {

		Button(
			onClick = {
				viewModel.onClickConnect()
			}
		) {
			Text("Connect")
		}

		Button(
			onClick = {}
		) {
			Text("Disconnect")
		}
	}
}

