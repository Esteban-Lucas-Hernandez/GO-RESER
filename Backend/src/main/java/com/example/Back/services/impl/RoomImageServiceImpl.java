package com.example.back.services.impl;

import com.example.back.dto.room.RoomImageDTO;
import com.example.back.mapper.room.RoomImageMapper;
import com.example.back.models.room.Room;
import com.example.back.models.room.RoomImage;
import com.example.back.models.user.User;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomImageRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.interfaces.RoomImageService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomImageServiceImpl implements RoomImageService {

    @Autowired
    private RoomImageRepository imagenRepository;

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomImageMapper imagenMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public List<RoomImageDTO> getImagenesByHabitacionId(Integer hotelId, Integer habitacionId) {
        User currentUser = securityService.getAuthenticatedUser();
        if (!hotelRepository.existsByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())) {
            throw new RuntimeException("No tienes permisos para este hotel");
        }
        return imagenMapper.imagenesHabitacionToImagenesHabitacionDTOs(
                imagenRepository.findByHabitacionIdAndHotelId(habitacionId, hotelId)
        );
    }

    @Override
    public RoomImageDTO createImagen(Integer hotelId, Integer habitacionId, RoomImageDTO imagenDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        if (!hotelRepository.existsByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())) {
            throw new RuntimeException("No tienes permisos para este hotel");
        }

        Room habitacion = habitacionRepository.findByIdHabitacionAndHotelId(habitacionId, hotelId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada en el hotel"));

        RoomImage imagen = new RoomImage(habitacion, imagenDTO.getUrlImagen());
        RoomImage saved = imagenRepository.save(imagen);
        return imagenMapper.imagenHabitacionToImagenHabitacionDTO(saved);
    }

    @Override
    public boolean deleteImagen(Integer hotelId, Integer habitacionId, Integer imagenId) {
        User currentUser = securityService.getAuthenticatedUser();
        if (!hotelRepository.existsByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())) {
            return false;
        }

        Optional<RoomImage> imagenOpt = imagenRepository.findByIdAndHabitacionIdAndHotelId(imagenId, habitacionId, hotelId);
        if (imagenOpt.isPresent()) {
            imagenRepository.delete(imagenOpt.get());
            return true;
        }
        return false;
    }
}
