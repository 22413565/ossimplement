package com.wtorder.controller

import com.wtorder.dto.MenuUpdateRequest
import com.wtorder.model.Menu
import com.wtorder.service.MenuService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/menus")
class MenuController(
    private val menuService: MenuService
) {
    @GetMapping
    fun getAllMenus(): ResponseEntity<List<Menu>> =
        ResponseEntity.ok(menuService.getAllMenus())

    @GetMapping("/{id}")
    fun getMenuById(@PathVariable id: Long): ResponseEntity<Menu> =
        ResponseEntity.ok(menuService.getMenuById(id))

    @GetMapping("/category/{category}")
    fun getMenusByCategory(@PathVariable category: String): ResponseEntity<List<Menu>> =
        ResponseEntity.ok(menuService.getMenusByCategory(category))

    @PostMapping
    fun createMenu(@RequestBody menu: Menu): ResponseEntity<Menu> =
        ResponseEntity.ok(menuService.createMenu(menu))

    @PutMapping("/{id}")
    fun updateMenu(
        @PathVariable id: Long,
        @RequestBody request: MenuUpdateRequest
    ): ResponseEntity<Menu> =
        ResponseEntity.ok(
            menuService.updateMenu(id, request.name, request.price, request.description, request.category, request.soldOut)
        )

    @DeleteMapping("/{id}")
    fun deleteMenu(@PathVariable id: Long): ResponseEntity<Void> {
        menuService.deleteMenu(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/photo")
    fun uploadPhoto(
        @PathVariable id: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Menu> {
        val uploadDir = Paths.get("uploads/menus")
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
        val fileName = "${UUID.randomUUID()}_${file.originalFilename}"
        val filePath = uploadDir.resolve(fileName)
        Files.copy(file.inputStream, filePath)

        val menu = menuService.updateMenuPhoto(id, "/uploads/menus/$fileName")
        return ResponseEntity.ok(menu)
    }
}
