package com.wtorder.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.wtorder.app.ui.theme.AdminColor
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMonitorScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    val dashboard by viewModel.dashboard.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("운영 데이터 모니터링", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (dashboard == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AdminColor)
            }
        } else {
            val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DashboardCard("총 누적 매출", "${formatter.format(dashboard!!.totalRevenue)}원", AdminColor)
                }
                item {
                    DashboardCard("총 주문 건수", "${dashboard!!.totalOrders}건", Color(0xFF1971C2))
                }
                item {
                    DashboardCard("진행 중인 주문", "${dashboard!!.activeOrders}건", Color(0xFFE03131))
                }
                item {
                    DashboardCard("현재 사용 테이블", "${dashboard!!.occupiedTables} / ${dashboard!!.totalTables}", Color(0xFFE67700))
                }
                item {
                    DashboardCard("등록된 메뉴", "${dashboard!!.totalMenuItems}개", Color(0xFF5C940D))
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
