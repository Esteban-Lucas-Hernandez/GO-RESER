package com.example.back.controllers.admin;

import com.example.back.dto.room.RoomCategoryDTO;
import com.example.back.services.interfaces.RoomCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categoria")
public class AdminRoomCategoryController {

    @Autowired
    private RoomCategoryService categoriaService;

    @GetMapping
    public ResponseEntity<List<RoomCategoryDTO>> obtenerCategorias() {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasPorUsuario());
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<RoomCategoryDTO> obtenerCategoria(@PathVariable Integer categoriaId) {
        return categoriaService.obtenerCategoriaPorId(categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomCategoryDTO> crearCategoria(@RequestBody RoomCategoryDTO categoriaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoriaParaUsuario(categoriaDTO));
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<RoomCategoryDTO> actualizarCategoria(
            @PathVariable Integer categoriaId,
            @RequestBody RoomCategoryDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaService.actualizarCategoriaDeUsuario(categoriaId, categoriaDTO));
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer categoriaId) {
        categoriaService.eliminarCategoriaDeUsuario(categoriaId);
        return ResponseEntity.noContent().build();
    }
}
