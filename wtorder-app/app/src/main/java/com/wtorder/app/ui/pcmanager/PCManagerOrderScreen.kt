package com.wtorder.app.ui.pcmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.PCManagerColor
import com.wtorder.app.data.model.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PCManagerOrderScreen(
    navController: NavController,
    tableNumber: Int,
    viewModel: PCManagerViewModel = viewModel()
) {
    LaunchedEffect(tableNumber) {
        viewModel.loadTableOrders(tableNumber)
    }

    val orders by viewModel.currentTableOrders.collectAsState()
    val menus by viewModel.menus.collectAsState()
    var showAddMenuDialog by remember { mutableStateOf(false) }

    val allItems = orders.flatMap { it.items }
    val totalPrice = allItems.sumOf { it.price * it.quantity }

    if (showAddMenuDialog) {
        AddMenuDialog(
            menus = menus,
            onMenuSelected = { menu ->
                viewModel.addMenuItemToTable(tableNumber, menu.menuId)
                showAddMenuDialog = false
            },
            onDismiss = { showAddMenuDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("테이블 $tableNumber 상세", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddMenuDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "메뉴 추가", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PCManagerColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("총 주문 금액", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("${totalPrice}원", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PCManagerColor)
                }
            }
        }
    ) { padding ->
        if (allItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("주문 내역이 없습니다.", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.menuName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("${item.price}원", color = Color.Gray)
                            }
                            Text("x${item.quantity}", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 16.dp))
                            
                            IconButton(onClick = { viewModel.deleteOrderItem(tableNumber, item.itemId) }) {
                                Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddMenuDialog(
    menus: List<Menu>,
    onMenuSelected: (Menu) -> Unit,
    onDismiss: () -> Unit
) {
    val availableMenus = menus.filter { !it.soldOut }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("추가할 메뉴 선택", fontWeight = FontWeight.Bold) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(availableMenus) { menu ->
                    Card(
                        modifier = Modifier.clickable { onMenuSelected(menu) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(menu.name, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text("${menu.price}원", color = PCManagerColor, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        }
    )
}
