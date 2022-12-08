package com.uzun.smartframe.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.uzun.smartframe.presentation.view.BluetoothSettingScreen
import com.uzun.smartframe.presentation.viewmodel.BluetoothSettingViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetScreen(
//	navController: NavController,
	viewModel : BluetoothSettingViewModel,
	activityContentScope: @Composable (state: ModalBottomSheetState, scope: CoroutineScope) -> Unit,
) {
	val state = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	val scope = rememberCoroutineScope()

	ModalBottomSheetLayout(
		sheetElevation = 5.dp,
		sheetShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
		sheetState = state,
		sheetContent = {
			BluetoothSettingScreen(viewModel)
		}
	) {
		activityContentScope(state, scope)
	}
}