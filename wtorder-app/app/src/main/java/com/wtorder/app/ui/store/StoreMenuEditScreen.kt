package com.wtorder.app.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.StoreColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMenuEditScreen(
    navController: NavController,
    menuId: Long,
    viewModel: StoreViewModel = viewModel()
) {
    val menus by viewModel.menus.collectAsState()
    val isEditMode = menuId != 0L
    val existingMenu = if (isEditMode) menus.find { it.menuId == menuId } else null

    var name by remember { mutableStateOf(existingMenu?.name ?: "") }
    var price by remember { mutableStateOf(existingMenu?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(existingMenu?.description ?: "") }
    var category by remember { mutableStateOf(existingMenu?.category ?: "") }
    var soldOut by remember { mutableStateOf(existingMenu?.soldOut ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "메뉴 수정" else "새 메뉴 추가", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = {
                            viewModel.deleteMenu(menuId)
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StoreColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        viewModel.saveMenu(menuId, name, price.toIntOrNull() ?: 0, description, category, soldOut)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StoreColor),
                    enabled = name.isNotBlank() && price.isNotBlank()
                ) {
                    Text("저장", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 사진 업로드 UI는 생략 (나중에 추가 가능)
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("메뉴 이름") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("가격 (원)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("카테고리 (예: 고기, 식사, 음료)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("메뉴 설명") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("품절 처리", fontWeight = FontWeight.Bold)
                Switch(
                    checked = soldOut,
                    onCheckedChange = { soldOut = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = StoreColor)
                )
            }
        }
    }
}
