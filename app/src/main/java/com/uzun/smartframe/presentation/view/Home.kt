package com.uzun.smartframe.presentation.view

import android.widget.ToggleButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uzun.smartframe.SmartFrameApp.SmartFrameApp
import com.uzun.smartframe._constant.UIConst.HEIGHT_TOPAPPBAR
import com.uzun.smartframe.presentation.viewmodel.BluetoothSettingViewModel

@Composable
fun HomeScreen(
	viewModel: BluetoothSettingViewModel,
	onclick: () -> Unit = {},
) {
	HomeScreenBackground {
		HomeScreenContent {
			if (viewModel.btnConnected.value) {
				// 조도 온도 습도 감지여부 초음파

				val valueData = viewModel.valueData.observeAsState()

				if(valueData.value?.isNotEmpty() == true) {
					RealtimeStatus(
						viewModel.valueData.value?.get(2) ?: 0.0,
						((1 - viewModel.valueData.value?.get(0)?.div(4096.0)!!) * 100) ?: 0.0,
						(viewModel.valueData.value?.get(3)) ?: 0.0,
					)
				} else {
					RealtimeStatus(0.0,0.0,0.0)
				}

				Row(
					modifier = Modifier,
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center,
				) {
					Text("보안모드 ON/OFF")
					val mCheckedState = remember { mutableStateOf(false) }
					Switch(checked = mCheckedState.value, onCheckedChange = {
						mCheckedState.value = it
						viewModel.onClickSendData(if(it) "1" else "0")
					})


					Button(
						onClick = {
							viewModel.onClickSendData("2")
						},
					) {
						Text("스피커 OFF")
					}
				}
				AlarmLog()
			} else {
				RealtimeStatus()

				Spacer(modifier = Modifier.size(50.dp))
				Button(
					onClick = onclick,
				) {
					Text("블루투스를 연결해주세요")
				}
			}
		}
	}
}


/*** ALARM COMPOSABLE ***/

@Composable
fun AlarmLog() {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxWidth()
			.shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp), clip = true)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color(0xFF007BF7))
				.padding(vertical = 8.dp)
				.padding(horizontal = 10.dp)
				.shadow(
					elevation = 0.dp,
					shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
					clip = true
				)
		) {
			HomeText("Alarm Log List", 20.sp, Color.White)
		}
		LazyColumn(
			modifier = Modifier
				.background(Color(0xFFF8F8F8))
				.padding(horizontal = 20.dp)
				.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			repeat(8) {
				item { AlarmItem() }
			}
		}
	}
}

@Composable
fun AlarmItem() {
	Row(
		modifier = Modifier.height(60.dp),
	) {
		AlarmIcon()
		AlarmContent()
	}
}

@Composable
fun AlarmContent() {
	Row(
		modifier = Modifier
			.padding(10.dp)
			.fillMaxWidth()
	) {
		Column() {
			HomeText("title", 18.sp)
			HomeText("contentcontentntcontent", 15.sp)
		}
	}
}

@Composable
fun AlarmIcon() {
	Box(
		Modifier.size(60.dp)
	) {
		Icon(
			imageVector = Icons.Filled.Warning,
			contentDescription = "waring icon",
			Modifier.size(60.dp),
		)
	}
}

/*** REALTIME STATUS COMPOSABLE ***/

@Composable
fun RealtimeStatus(
	temp: Double = 0.0,
	lumi: Double = 0.0,
	humi: Double = 0.0,
	title: String = "Real Time Status",
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxWidth()
			.shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp), clip = true)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color(0xFF007BF7))
				.padding(vertical = 8.dp)
				.padding(horizontal = 10.dp)
				.shadow(
					elevation = 0.dp,
					shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
					clip = true
				)
		) {
			HomeText(title, 20.sp, Color.White)
		}
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color(0xFFF8F8F8))
				.padding(horizontal = 20.dp),
			verticalArrangement = Arrangement.Center,
		) {

			Spacer(modifier = Modifier.size(10.dp))

			Row(
				modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
			) {
				RealtimeItem("습도", "%", humi)
				RealtimeItem("온도", "도", temp)
				RealtimeItem("조도", "%", lumi)
			}
		}
	}
}

@Composable
fun RealtimeItem(
	title: String = "",
	unit: String = "",
	value: Double = 0.0,
) {
	Column(
		modifier = Modifier.height(120.dp), horizontalAlignment = Alignment.CenterHorizontally
	) {
		HomeText(title)
		Spacer(modifier = Modifier.size(10.dp))

		Surface(
			shape = CircleShape,
			color = Color.White,
			border = BorderStroke(1.dp, Color(0xFF007BF7)),
			modifier = Modifier.size(80.dp)
		) {
			Box(contentAlignment = Alignment.Center) {
				HomeText(text = "${String.format("%.0f", value).toDouble()}$unit")
			}
		}
	}
}

@Composable
fun HomeText(
	text: String = "",
	size: TextUnit = 16.sp,
	color: Color = Color.Black,
	weight: FontWeight = FontWeight.Bold,
	modifier: Modifier = Modifier,
) {
	Text(
		text = text, fontSize = size, color = color, fontWeight = weight
	)
}

@Composable
fun HomeScreenContent(
	content: @Composable ColumnScope.() -> Unit,
) {
	Column(
		modifier = Modifier
			.padding(horizontal = 18.dp)
			.padding(vertical = 18.dp)
			.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(20.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		content()
	}
}

@Composable
fun HomeScreenBackground(
	modifier: Modifier = Modifier,
	innerPadding: PaddingValues = PaddingValues(0.dp),
	content: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.padding(innerPadding)
			.padding(top = HEIGHT_TOPAPPBAR),
	) {
		content()
	}
}

@Preview
@Composable
fun PreviewMain() {
//	SmartFrameApp(viewModel)
}