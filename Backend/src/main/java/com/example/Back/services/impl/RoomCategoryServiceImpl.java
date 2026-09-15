package com.example.back.services.impl;

import com.example.back.dto.room.RoomCategoryDTO;
import com.example.back.mapper.room.RoomCategoryMapper;
import com.example.back.models.room.RoomCategory;
import com.example.back.models.user.User;
import com.example.back.repo.room.RoomCategoryRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.interfaces.RoomCategoryService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomCategoryServiceImpl implements RoomCategoryService {

    @Autowired
    private RoomCategoryRepository categoriaRepository;

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private RoomCategoryMapper categoriaMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public List<RoomCategoryDTO> obtenerCategoriasPorUsuario() {
        User currentUser = securityService.getAuthenticatedUser();
        return categoriaRepository.findByUsuarioIdUsuario(currentUser.getIdUsuario()).stream()
                .map(categoriaMapper::categoriaHabitacionToCategoriaHabitacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomCategoryDTO> obtenerCategoriaPorId(Integer categoriaId) {
        User currentUser = securityService.getAuthenticatedUser();
        return categoriaRepository.findByIdAndUsuarioIdUsuario(categoriaId, currentUser.getIdUsuario())
                .map(categoriaMapper::categoriaHabitacionToCategoriaHabitacionDTO);
    }

    @Override
    public RoomCategoryDTO crearCategoriaParaUsuario(RoomCategoryDTO categoriaDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        RoomCategory categoria = categoriaMapper.categoriaHabitacionDTOToCategoriaHabitacion(categoriaDTO);
        categoria.setUsuario(currentUser);
        RoomCategory saved = categoriaRepository.save(categoria);
        return categoriaMapper.categoriaHabitacionToCategoriaHabitacionDTO(saved);
    }

    @Override
    public RoomCategoryDTO actualizarCategoriaDeUsuario(Integer categoriaId, RoomCategoryDTO categoriaDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        RoomCategory categoria = categoriaRepository.findByIdAndUsuarioIdUsuario(categoriaId, currentUser.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada o no tienes permisos"));

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        RoomCategory updated = categoriaRepository.save(categoria);
        return categoriaMapper.categoriaHabitacionToCategoriaHabitacionDTO(updated);
    }

    @Override
    public void eliminarCategoriaDeUsuario(Integer categoriaId) {
        User currentUser = securityService.getAuthenticatedUser();
        RoomCategory categoria = categoriaRepository.findByIdAndUsuarioIdUsuario(categoriaId, currentUser.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada o no tienes permisos"));

        long roomsCount = habitacionRepository.countByCategoriaId(categoriaId);
        if (roomsCount > 0) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene habitaciones asociadas");
        }

        categoriaRepository.delete(categoria);
    }
}
