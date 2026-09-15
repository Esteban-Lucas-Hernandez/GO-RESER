package com.example.back.services.impl;

import com.example.back.dto.hotel.HotelDTO;
import com.example.back.mapper.hotel.HotelMapper;
import com.example.back.models.hotel.City;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.user.User;
import com.example.back.repo.hotel.CityRepository;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.interfaces.HotelService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository ciudadRepository;
    private final UserRepository usuarioRepository;
    private final HotelMapper hotelMapper;
    private final SecurityService securityService;
    private final RoomRepository habitacionRepository;
    private final BookingRepository reservaRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, CityRepository ciudadRepository,
                            UserRepository usuarioRepository, HotelMapper hotelMapper,
                            SecurityService securityService, RoomRepository habitacionRepository,
                            BookingRepository reservaRepository) {
        this.hotelRepository = hotelRepository;
        this.ciudadRepository = ciudadRepository;
        this.usuarioRepository = usuarioRepository;
        this.hotelMapper = hotelMapper;
        this.securityService = securityService;
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<Hotel> getAllHoteles() {
        return hotelRepository.findAll();
    }

    @Override
    public List<HotelDTO> getAllHotelesDTO() {
        return hotelMapper.hotelsToHotelDTOs(hotelRepository.findAll());
    }

    @Override
    public List<HotelDTO> getHotelesByCurrentUser() {
        User currentUser = securityService.getAuthenticatedUser();
        return hotelMapper.hotelsToHotelDTOs(hotelRepository.findByUsuarioIdUsuario(currentUser.getIdUsuario()));
    }

    @Override
    public Optional<Hotel> getHotelById(Integer id) {
        return hotelRepository.findById(id);
    }

    @Override
    public Hotel createHotel(HotelDTO hotelDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        Hotel hotel = hotelMapper.hotelDTOToHotel(hotelDTO);
        hotel.setUsuario(currentUser);

        if (hotelDTO.getCiudadId() != null) {
            City ciudad = ciudadRepository.findById(hotelDTO.getCiudadId())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            hotel.setCiudad(ciudad);
        }

        hotel.setCreatedAt(LocalDateTime.now());
        hotel.setUpdatedAt(LocalDateTime.now());
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel updateHotel(Integer id, HotelDTO hotelDTO) {
        User currentUser = securityService.getAuthenticatedUser();
        Hotel hotel = hotelRepository.findByIdAndUsuarioIdUsuario(id, currentUser.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado o no tienes permisos"));

        hotelMapper.updateHotelFromDTO(hotelDTO, hotel);

        if (hotelDTO.getCiudadId() != null) {
            City ciudad = ciudadRepository.findById(hotelDTO.getCiudadId())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            hotel.setCiudad(ciudad);
        }

        hotel.setUpdatedAt(LocalDateTime.now());
        return hotelRepository.save(hotel);
    }

    @Override
    public boolean deleteHotelWithCascade(Integer id) {
        User currentUser = securityService.getAuthenticatedUser();
        Optional<Hotel> hotelOpt = hotelRepository.findByIdAndUsuarioIdUsuario(id, currentUser.getIdUsuario());
        if (hotelOpt.isPresent()) {
            hotelRepository.delete(hotelOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean hasHabitaciones(Integer hotelId) {
        return !habitacionRepository.findByHotelId(hotelId).isEmpty();
    }
}
