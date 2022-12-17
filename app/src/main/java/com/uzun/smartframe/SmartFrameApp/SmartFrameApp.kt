package com.uzun.smartframe.SmartFrameApp

import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.uzun.smartframe.presentation.component.BottomSheetScreen
import com.uzun.smartframe.presentation.view.HomeScreen
import com.uzun.smartframe.presentation.viewmodel.BluetoothSettingViewModel
import com.uzun.smartframe.ui.theme.SmartFrameTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SmartFrameApp(viewModel: BluetoothSettingViewModel) {
	SmartFrameTheme {
		BottomSheetScreen(viewModel) { state, scope ->
			Scaffold(
				topBar = {
					SmallTopAppBar(
						title = { Text("Smart Frame") },
						actions = {
							IconButton(onClick = {
								scope.launch {
									state.animateTo(
										ModalBottomSheetValue.Expanded,
										tween(500)
									)
								}
							}) {
								Icon(
									imageVector = Icons.Filled.Menu,
									contentDescription = "Localized description"
								)
							}
						}
					)
				}
			) {
				HomeScreen(
					viewModel = viewModel,
					onclick = {
						scope.launch {
							state.animateTo(
								ModalBottomSheetValue.Expanded,
								tween(500)
							)
						}
					}
				)
				it
			}
		}
	}
}
