package com.wtorder.app.ui.kitchen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.data.model.Order
import com.wtorder.app.ui.theme.KitchenColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenOrderScreen(
    navController: NavController,
    viewModel: KitchenViewModel = viewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주방 전표 목록", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KitchenColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (isLoading && orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = KitchenColor)
            }
        } else if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("현재 접수된 주문이 없습니다.", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                items(orders) { order ->
                    KitchenTicketCard(order = order) {
                        navController.navigate("kitchen_ticket/${order.orderId}")
                    }
                }
            }
        }
    }
}

@Composable
fun KitchenTicketCard(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = KitchenColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "주문 #${order.orderId}",
                        fontWeight = FontWeight.Bold,
                        color = KitchenColor
                    )
                }
                Surface(
                    color = KitchenColor,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "테이블 ${order.tableNumber}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("주문 시간: ${order.orderTime}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            // 주문 항목 요약 (최대 3개)
            order.items.take(3).forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.menuName, fontSize = 16.sp)
                    Text("x${item.quantity}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            if (order.items.size > 3) {
                Text("... 외 ${order.items.size - 3}개", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
