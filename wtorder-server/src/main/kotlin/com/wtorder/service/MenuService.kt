package com.wtorder.service

import com.wtorder.model.Menu
import com.wtorder.repository.MenuRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MenuService(
    private val menuRepository: MenuRepository
) {
    fun getAllMenus(): List<Menu> = menuRepository.findAll()

    fun getMenuById(id: Long): Menu =
        menuRepository.findById(id).orElseThrow { RuntimeException("메뉴를 찾을 수 없습니다: $id") }

    fun getMenusByCategory(category: String): List<Menu> =
        menuRepository.findByCategory(category)

    fun createMenu(menu: Menu): Menu = menuRepository.save(menu)

    fun updateMenu(id: Long, name: String?, price: Int?, description: String?, category: String?, soldOut: Boolean?): Menu {
        val menu = getMenuById(id)
        name?.let { menu.name = it }
        price?.let { menu.price = it }
        description?.let { menu.description = it }
        category?.let { menu.category = it }
        soldOut?.let { menu.soldOut = it }
        return menuRepository.save(menu)
    }

    fun updateMenuPhoto(id: Long, imagePath: String): Menu {
        val menu = getMenuById(id)
        menu.imagePath = imagePath
        return menuRepository.save(menu)
    }

    fun deleteMenu(id: Long) {
        if (!menuRepository.existsById(id)) {
            throw RuntimeException("메뉴를 찾을 수 없습니다: $id")
        }
        menuRepository.deleteById(id)
    }
}
