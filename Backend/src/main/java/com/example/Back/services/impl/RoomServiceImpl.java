package com.example.back.services.impl;

import com.example.back.dto.room.CreateRoomDTO;
import com.example.back.dto.room.RoomDTO;
import com.example.back.mapper.room.RoomMapper;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.room.Room;
import com.example.back.models.room.RoomCategory;
import com.example.back.models.room.RoomImage;
import com.example.back.models.user.User;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomCategoryRepository;
import com.example.back.repo.room.RoomImageRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.interfaces.RoomService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomCategoryRepository categoriaRepository;

    @Autowired
    private RoomImageRepository imagenRepository;

    @Autowired
    private RoomMapper habitacionMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public List<RoomDTO> getHabitacionesByHotelId(Integer hotelId) {
        return habitacionRepository.findByHotelId(hotelId).stream()
                .map(habitacionMapper::habitacionToHabitacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDTO> getHabitacionByIdAndHotelId(Integer habitacionId, Integer hotelId) {
        return habitacionRepository.findByIdHabitacionAndHotelId(habitacionId, hotelId)
                .map(habitacionMapper::habitacionToHabitacionDTO);
    }

    @Override
    public List<RoomDTO> getHabitacionesDeMisHoteles() {
        User currentUser = securityService.getAuthenticatedUser();
        List<Hotel> misHoteles = hotelRepository.findByUsuarioIdUsuario(currentUser.getIdUsuario());
        List<RoomDTO> todas = new ArrayList<>();
        for (Hotel hotel : misHoteles) {
            todas.addAll(getHabitacionesByHotelId(hotel.getId()));
        }
        return todas;
    }

    @Override
    public Optional<RoomDTO> createHabitacion(Integer hotelId, RoomDTO habitacionDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        Hotel hotel = hotelRepository.findByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado o sin permisos"));

        Room habitacion = new Room();
        habitacion.setHotel(hotel);
        habitacion.setNumero(habitacionDTO.getNumero());
        habitacion.setCapacidad(habitacionDTO.getCapacidad() != null ? habitacionDTO.getCapacidad() : 1);
        habitacion.setPrecio(habitacionDTO.getPrecio());
        habitacion.setDescripcion(habitacionDTO.getDescripcion());

        if (habitacionDTO.getEstado() != null) {
            habitacion.setEstado(Room.EstadoHabitacion.valueOf(habitacionDTO.getEstado().toLowerCase()));
        }

        if (habitacionDTO.getCategoria() != null && habitacionDTO.getCategoria().getId() != null) {
            RoomCategory cat = categoriaRepository.findById(habitacionDTO.getCategoria().getId()).orElse(null);
            habitacion.setCategoria(cat);
        }

        Room saved = habitacionRepository.save(habitacion);
        return Optional.of(habitacionMapper.habitacionToHabitacionDTO(saved));
    }

    @Override
    public Optional<RoomDTO> createHabitacionDesdeDTO(Integer hotelId, CreateRoomDTO crearHabitacionDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        Hotel hotel = hotelRepository.findByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado o sin permisos"));

        Room habitacion = new Room();
        habitacion.setHotel(hotel);
        habitacion.setNumero(crearHabitacionDTO.getNumero());
        habitacion.setCapacidad(crearHabitacionDTO.getCapacidad() != null ? crearHabitacionDTO.getCapacidad() : 1);
        habitacion.setPrecio(crearHabitacionDTO.getPrecio());
        habitacion.setDescripcion(crearHabitacionDTO.getDescripcion());

        if (crearHabitacionDTO.getEstado() != null) {
            try {
                habitacion.setEstado(Room.EstadoHabitacion.valueOf(crearHabitacionDTO.getEstado().toLowerCase()));
            } catch (Exception e) {
                habitacion.setEstado(Room.EstadoHabitacion.disponible);
            }
        }

        if (crearHabitacionDTO.getCategoriaId() != null) {
            RoomCategory cat = categoriaRepository.findById(crearHabitacionDTO.getCategoriaId()).orElse(null);
            habitacion.setCategoria(cat);
        }

        Room saved = habitacionRepository.save(habitacion);

        if (crearHabitacionDTO.getImagenesUrls() != null && !crearHabitacionDTO.getImagenesUrls().isEmpty()) {
            for (String url : crearHabitacionDTO.getImagenesUrls()) {
                if (url != null && !url.isBlank()) {
                    imagenRepository.save(new RoomImage(saved, url));
                }
            }
        } else if (crearHabitacionDTO.getImagenUrl() != null && !crearHabitacionDTO.getImagenUrl().isBlank()) {
            imagenRepository.save(new RoomImage(saved, crearHabitacionDTO.getImagenUrl()));
        }

        return Optional.of(habitacionMapper.habitacionToHabitacionDTO(saved));
    }

    @Override
    public Optional<RoomDTO> updateHabitacion(Integer habitacionId, Integer hotelId, RoomDTO habitacionDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        if (!hotelRepository.existsByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())) {
            return Optional.empty();
        }

        Room habitacion = habitacionRepository.findByIdHabitacionAndHotelId(habitacionId, hotelId)
                .orElse(null);
        if (habitacion == null) return Optional.empty();

        habitacion.setNumero(habitacionDTO.getNumero());
        habitacion.setCapacidad(habitacionDTO.getCapacidad());
        habitacion.setPrecio(habitacionDTO.getPrecio());
        habitacion.setDescripcion(habitacionDTO.getDescripcion());

        if (habitacionDTO.getEstado() != null) {
            try {
                habitacion.setEstado(Room.EstadoHabitacion.valueOf(habitacionDTO.getEstado().toLowerCase()));
            } catch (Exception ignored) {}
        }

        if (habitacionDTO.getCategoria() != null && habitacionDTO.getCategoria().getId() != null) {
            RoomCategory cat = categoriaRepository.findById(habitacionDTO.getCategoria().getId()).orElse(null);
            habitacion.setCategoria(cat);
        }

        habitacion.setUpdatedAt(LocalDateTime.now());
        Room saved = habitacionRepository.save(habitacion);
        return Optional.of(habitacionMapper.habitacionToHabitacionDTO(saved));
    }

    @Override
    public boolean deleteHabitacion(Integer habitacionId, Integer hotelId) {
        User currentUser = securityService.getAuthenticatedUser();
        if (!hotelRepository.existsByIdAndUsuarioIdUsuario(hotelId, currentUser.getIdUsuario())) {
            return false;
        }

        Optional<Room> habitacionOpt = habitacionRepository.findByIdHabitacionAndHotelId(habitacionId, hotelId);
        if (habitacionOpt.isPresent()) {
            habitacionRepository.delete(habitacionOpt.get());
            return true;
        }
        return false;
    }
}
