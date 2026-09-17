package com.example.back.services.interfaces;

import com.example.back.dto.room.RoomCategoryDTO;
import java.util.List;
import java.util.Optional;

public interface RoomCategoryService {
    List<RoomCategoryDTO> obtenerCategoriasPorUsuario();
    Optional<RoomCategoryDTO> obtenerCategoriaPorId(Integer categoriaId);
    RoomCategoryDTO crearCategoriaParaUsuario(RoomCategoryDTO categoriaDTO);
    RoomCategoryDTO actualizarCategoriaDeUsuario(Integer categoriaId, RoomCategoryDTO categoriaDTO);
    void eliminarCategoriaDeUsuario(Integer categoriaId);
}
