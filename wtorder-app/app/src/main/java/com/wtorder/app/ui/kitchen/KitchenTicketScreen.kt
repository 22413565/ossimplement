package com.wtorder.app.ui.kitchen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.KitchenColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenTicketScreen(
    navController: NavController,
    orderId: Long,
    viewModel: KitchenViewModel = viewModel() // 실제로는 shared ViewModel 이나 DB 조회 필요, 여기서는 UI 표시용
) {
    val orders by viewModel.orders.collectAsState()
    val order = orders.find { it.orderId == orderId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주방 전표 상세", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KitchenColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (order != null) {
                Surface(shadowElevation = 8.dp) {
                    Button(
                        onClick = {
                            viewModel.completeOrder(orderId)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KitchenColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("조리 완료 처리", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        if (order == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Text("주문 정보를 찾을 수 없습니다.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "테이블 ${order.tableNumber}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = KitchenColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("주문 번호: #${order.orderId}", fontSize = 16.sp, color = Color.Gray)
                        Text("주문 시간: ${order.orderTime}", fontSize = 16.sp, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                            items(order.items) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.menuName, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                                    Text("x${item.quantity}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
