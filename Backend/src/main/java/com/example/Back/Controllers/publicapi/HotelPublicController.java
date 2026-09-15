package com.example.back.controllers.publicapi;

import com.example.back.dto.hotel.HotelPublicDTO;
import com.example.back.dto.room.RoomDTO;
import com.example.back.dto.room.RoomDetailDTO;
import com.example.back.mapper.hotel.HotelPublicMapper;
import com.example.back.mapper.room.RoomDetailMapper;
import com.example.back.mapper.room.RoomMapper;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.room.Room;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class HotelPublicController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private HotelPublicMapper hotelPublicMapper;

    @Autowired
    private RoomMapper habitacionMapper;

    @Autowired
    private RoomDetailMapper habitacionDetalleMapper;

    @GetMapping("/hoteles")
    public ResponseEntity<List<HotelPublicDTO>> getHotelesPublicos() {
        List<Hotel> hoteles = hotelRepository.findAll();
        List<HotelPublicDTO> hotelesDTO = hotelPublicMapper.hotelsToHotelPublicDTOs(hoteles);
        return ResponseEntity.ok(hotelesDTO);
    }

    @GetMapping("/hoteles/{idHotel}/habitaciones")
    public ResponseEntity<List<RoomDTO>> getHabitacionesPublicasPorHotel(@PathVariable Integer idHotel) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        List<Room> habitaciones = habitacionRepository.findByHotelAndEstado(hotel, Room.EstadoHabitacion.disponible);
        List<RoomDTO> habitacionesDTO = habitacionMapper.habitacionesToHabitacionDTOs(habitaciones);
        return ResponseEntity.ok(habitacionesDTO);
    }

    @GetMapping("/hoteles/{idHotel}/habitaciones/{idHabitacion}")
    public ResponseEntity<RoomDetailDTO> getDetalleHabitacion(
            @PathVariable Integer idHotel,
            @PathVariable Integer idHabitacion) {

        Room habitacion = habitacionRepository.findByIdHabitacionAndHotelId(idHabitacion, idHotel)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada para este hotel"));

        RoomDetailDTO detalleDTO = habitacionDetalleMapper.habitacionToHabitacionDetalleDTO(habitacion);
        return ResponseEntity.ok(detalleDTO);
    }
}
