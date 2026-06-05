package com.wtorder.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.AdminColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStoreInfoScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    val store by viewModel.store.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var name by remember(store) { mutableStateOf(store?.name ?: "") }
    var location by remember(store) { mutableStateOf(store?.location ?: "") }
    var contact by remember(store) { mutableStateOf(store?.contact ?: "") }
    var operatingHours by remember(store) { mutableStateOf(store?.operatingHours ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매장 정보 관리", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("role_select") { popUpTo("role_select") { inclusive = true } } }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("admin_monitor") }) {
                        Icon(Icons.Default.BarChart, contentDescription = "대시보드", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { store?.let { viewModel.updateStoreInfo(it.storeId, name, location, contact, operatingHours) } },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdminColor),
                    enabled = name.isNotBlank() && !isLoading
                ) {
                    Text("저장", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        if (isLoading && store == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AdminColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("매장 이름") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("매장 위치 (주소)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("연락처") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = operatingHours,
                    onValueChange = { operatingHours = it },
                    label = { Text("운영 시간") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
