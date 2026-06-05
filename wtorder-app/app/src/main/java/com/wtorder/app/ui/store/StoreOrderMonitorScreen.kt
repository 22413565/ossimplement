package com.wtorder.app.ui.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.StoreColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreOrderMonitorScreen(
    navController: NavController,
    viewModel: StoreViewModel = viewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val staffCalls by viewModel.staffCalls.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매장 주문 모니터링", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StoreColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (staffCalls.isNotEmpty()) {
                item {
                    Text("🔔 직원 호출", fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
                }
                items(staffCalls) { call ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE3E3)),
                        modifier = Modifier.fillMaxWidth().clickable { viewModel.acknowledgeStaffCall(call.callId) }
                    ) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("테이블 ${call.tableNumber} 호출", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text("확인하기", color = StoreColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("📋 진행 중인 주문", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            }
            
            if (orders.isEmpty()) {
                item {
                    Text("진행 중인 주문이 없습니다.", color = Color.Gray)
                }
            } else {
                items(orders) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("store_ticket/${order.orderId}") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("테이블 ${order.tableNumber}", fontWeight = FontWeight.Bold, color = StoreColor)
                                Text("${order.totalPrice}원", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("주문 시간: ${order.orderTime}", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
