package com.wtorder.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wtorder.app.ui.roleselect.RoleSelectScreen
import com.wtorder.app.ui.customer.CustomerMenuScreen
import com.wtorder.app.ui.customer.CustomerOrderScreen
import com.wtorder.app.ui.customer.CustomerConfirmScreen
import com.wtorder.app.ui.kitchen.KitchenOrderScreen
import com.wtorder.app.ui.kitchen.KitchenTicketScreen
import com.wtorder.app.ui.store.StoreMenuManageScreen
import com.wtorder.app.ui.store.StoreMenuEditScreen
import com.wtorder.app.ui.store.StoreOrderMonitorScreen
import com.wtorder.app.ui.store.StoreTicketScreen
import com.wtorder.app.ui.pcmanager.PCManagerTableScreen
import com.wtorder.app.ui.pcmanager.PCManagerOrderScreen
import com.wtorder.app.ui.admin.AdminStoreInfoScreen
import com.wtorder.app.ui.admin.AdminMonitorScreen

object Routes {
    const val ROLE_SELECT = "role_select"

    // Customer
    const val CUSTOMER_MENU = "customer_menu/{tableNumber}"
    const val CUSTOMER_ORDER = "customer_order/{tableNumber}"
    const val CUSTOMER_CONFIRM = "customer_confirm/{tableNumber}"

    // Kitchen
    const val KITCHEN_ORDERS = "kitchen_orders"
    const val KITCHEN_TICKET = "kitchen_ticket/{orderId}"

    // Store
    const val STORE_MENU_MANAGE = "store_menu_manage"
    const val STORE_MENU_EDIT = "store_menu_edit/{menuId}"
    const val STORE_ORDER_MONITOR = "store_order_monitor"
    const val STORE_TICKET = "store_ticket/{orderId}"

    // PC Manager
    const val PC_MANAGER_TABLES = "pc_manager_tables"
    const val PC_MANAGER_ORDER = "pc_manager_order/{tableNumber}"

    // Admin
    const val ADMIN_STORE_INFO = "admin_store_info"
    const val ADMIN_MONITOR = "admin_monitor"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.ROLE_SELECT) {
        // 역할 선택
        composable(Routes.ROLE_SELECT) {
            RoleSelectScreen(navController = navController)
        }

        // Customer
        composable(
            Routes.CUSTOMER_MENU,
            arguments = listOf(navArgument("tableNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val tableNumber = backStackEntry.arguments?.getInt("tableNumber") ?: 1
            CustomerMenuScreen(navController = navController, tableNumber = tableNumber)
        }
        composable(
            Routes.CUSTOMER_ORDER,
            arguments = listOf(navArgument("tableNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val tableNumber = backStackEntry.arguments?.getInt("tableNumber") ?: 1
            CustomerOrderScreen(navController = navController, tableNumber = tableNumber)
        }
        composable(
            Routes.CUSTOMER_CONFIRM,
            arguments = listOf(navArgument("tableNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val tableNumber = backStackEntry.arguments?.getInt("tableNumber") ?: 1
            CustomerConfirmScreen(navController = navController, tableNumber = tableNumber)
        }

        // Kitchen
        composable(Routes.KITCHEN_ORDERS) {
            KitchenOrderScreen(navController = navController)
        }
        composable(
            Routes.KITCHEN_TICKET,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            KitchenTicketScreen(navController = navController, orderId = orderId)
        }

        // Store
        composable(Routes.STORE_MENU_MANAGE) {
            StoreMenuManageScreen(navController = navController)
        }
        composable(
            Routes.STORE_MENU_EDIT,
            arguments = listOf(navArgument("menuId") { type = NavType.LongType })
        ) { backStackEntry ->
            val menuId = backStackEntry.arguments?.getLong("menuId") ?: 0L
            StoreMenuEditScreen(navController = navController, menuId = menuId)
        }
        composable(Routes.STORE_ORDER_MONITOR) {
            StoreOrderMonitorScreen(navController = navController)
        }
        composable(
            Routes.STORE_TICKET,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            StoreTicketScreen(navController = navController, orderId = orderId)
        }

        // PC Manager
        composable(Routes.PC_MANAGER_TABLES) {
            PCManagerTableScreen(navController = navController)
        }
        composable(
            Routes.PC_MANAGER_ORDER,
            arguments = listOf(navArgument("tableNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val tableNumber = backStackEntry.arguments?.getInt("tableNumber") ?: 1
            PCManagerOrderScreen(navController = navController, tableNumber = tableNumber)
        }

        // Admin
        composable(Routes.ADMIN_STORE_INFO) {
            AdminStoreInfoScreen(navController = navController)
        }
        composable(Routes.ADMIN_MONITOR) {
            AdminMonitorScreen(navController = navController)
        }
    }
}
