package com.wtorder.app.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.ArrowBack
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
import com.wtorder.app.data.model.Menu
import com.wtorder.app.ui.theme.StoreColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMenuManageScreen(
    navController: NavController,
    viewModel: StoreViewModel = viewModel()
) {
    val menus by viewModel.menus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매장 메뉴 관리", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("role_select") { popUpTo("role_select") { inclusive = true } } }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("store_order_monitor") }) {
                        Icon(Icons.Default.Monitor, contentDescription = "주문 모니터링", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StoreColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("store_menu_edit/0") },
                containerColor = StoreColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "메뉴 추가")
            }
        }
    ) { padding ->
        if (isLoading && menus.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StoreColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menus) { menu ->
                    StoreMenuCard(
                        menu = menu,
                        onEdit = { navController.navigate("store_menu_edit/${menu.menuId}") },
                        onToggleSoldOut = { viewModel.toggleSoldOut(menu) }
                    )
                }
            }
        }
    }
}

@Composable
fun StoreMenuCard(menu: Menu, onEdit: () -> Unit, onToggleSoldOut: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(if (menu.soldOut) Color.DarkGray else Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(menu.name, fontWeight = FontWeight.Bold)
                Text("${menu.price}원 | ${menu.category}", color = Color.Gray)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("품절", color = if (menu.soldOut) Color.Red else Color.Gray)
                    Switch(
                        checked = menu.soldOut,
                        onCheckedChange = { onToggleSoldOut() },
                        colors = SwitchDefaults.colors(checkedThumbColor = StoreColor, checkedTrackColor = StoreColor.copy(alpha = 0.5f))
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "수정", tint = StoreColor)
                }
            }
        }
    }
}
