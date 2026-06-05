package com.wtorder.app.ui.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wtorder.app.ui.theme.CustomerColor
import com.wtorder.app.ui.theme.Success

@Composable
fun CustomerConfirmScreen(
    navController: NavController,
    tableNumber: Int,
    viewModel: CustomerViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "주문 완료",
            tint = Success,
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "주문이 완료되었습니다!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "주방에서 맛있게 조리 중입니다.\n잠시만 기다려주세요.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CustomerColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("메뉴판으로 돌아가기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { viewModel.callStaff(tableNumber) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = "직원 호출")
            Spacer(modifier = Modifier.width(8.dp))
            Text("직원 호출하기", fontSize = 18.sp, color = CustomerColor)
        }
    }
}
