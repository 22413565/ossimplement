package com.wtorder.app.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.data.model.Menu
import com.wtorder.app.ui.theme.CustomerColor
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerMenuScreen(
    navController: NavController,
    tableNumber: Int,
    viewModel: CustomerViewModel = viewModel()
) {
    val menus by viewModel.menus.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val totalQuantity = cart.sumOf { it.quantity }
    val categories = listOf("전체") + menus.map { it.category }.distinct().filter { it.isNotEmpty() }
    var selectedCategory by remember { mutableStateOf("전체") }

    val filteredMenus = if (selectedCategory == "전체") {
        menus
    } else {
        menus.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("테이블 $tableNumber 주문하기", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomerColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { navController.navigate("customer_order/$tableNumber") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "장바구니",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        if (totalQuantity > 0) {
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(totalQuantity.toString())
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (totalQuantity > 0) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("customer_order/$tableNumber") },
                    containerColor = CustomerColor,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.ShoppingCart, "장바구니")
                    Spacer(Modifier.width(8.dp))
                    Text("주문하기 ($totalQuantity)", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 카테고리 탭
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category, fontWeight = FontWeight.Bold) },
                        selectedContentColor = CustomerColor,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CustomerColor)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMenus) { menu ->
                        MenuCard(menu = menu) {
                            viewModel.addToCart(menu)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCard(menu: Menu, onAddToCart: () -> Unit) {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(enabled = !menu.soldOut) { onAddToCart() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (menu.soldOut) MaterialTheme.colorScheme.surfaceVariant else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 이미지 영역 (임시 회색 박스)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (menu.soldOut) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("품절", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = menu.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    color = if (menu.soldOut) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatter.format(menu.price)}원",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (menu.soldOut) Color.Gray else CustomerColor
                )
            }
        }
    }
}
