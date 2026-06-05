package com.wtorder.app.ui.pcmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.data.model.RestaurantTable
import com.wtorder.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PCManagerTableScreen(
    navController: NavController,
    viewModel: PCManagerViewModel = viewModel()
) {
    val tables by viewModel.tables.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("전체 테이블 현황", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("role_select") { popUpTo("role_select") { inclusive = true } } }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PCManagerColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            // 범례
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegendItem("빈 자리", TableEmpty)
                LegendItem("주문중", TableOccupied)
                LegendItem("직원호출", TableNeedsService)
            }

            if (isLoading && tables.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PCManagerColor)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // PC 화면 고려하여 3열~4열
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tables) { table ->
                        TableCard(table = table) {
                            navController.navigate("pc_manager_order/${table.tableNumber}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(16.dp).clip(RoundedCornerShape(4.dp)).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun TableCard(table: RestaurantTable, onClick: () -> Unit) {
    val bgColor = when (table.status) {
        "OCCUPIED" -> TableOccupied
        "NEEDS_SERVICE" -> TableNeedsService
        else -> TableEmpty
    }
    
    val contentColor = if (table.status == "EMPTY") Color.DarkGray else Color.White

    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "${table.tableNumber}",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
