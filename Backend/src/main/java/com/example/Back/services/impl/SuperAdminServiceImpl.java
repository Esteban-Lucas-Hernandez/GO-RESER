package com.example.back.services.impl;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.hotel.HotelDTO;
import com.example.back.dto.room.RoomDTO;
import com.example.back.dto.user.UserDTO;
import com.example.back.mapper.booking.BookingMapper;
import com.example.back.mapper.hotel.HotelMapper;
import com.example.back.mapper.room.RoomMapper;
import com.example.back.mapper.user.UserMapper;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.room.Room;
import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import com.example.back.services.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository habitacionRepository;
    private final BookingRepository reservaRepository;
    private final UserMapper usuarioMapper;
    private final HotelMapper hotelMapper;
    private final RoomMapper habitacionMapper;
    private final BookingMapper reservaMapper = BookingMapper.INSTANCE;

    @Autowired
    public SuperAdminServiceImpl(UserRepository usuarioRepository, RoleRepository roleRepository,
                                 HotelRepository hotelRepository, RoomRepository habitacionRepository,
                                 BookingRepository reservaRepository, UserMapper usuarioMapper,
                                 HotelMapper hotelMapper, RoomMapper habitacionMapper) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.hotelRepository = hotelRepository;
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioMapper = usuarioMapper;
        this.hotelMapper = hotelMapper;
        this.habitacionMapper = habitacionMapper;
    }

    @Override
    public List<UserDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User obtenerUsuarioPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UserDTO actualizarRoles(Integer idUsuario, List<String> nombresRoles) {
        User usuario = obtenerUsuarioPorId(idUsuario);
        List<Role> roles = roleRepository.findByNombreIn(nombresRoles);
        usuario.setRoles(new HashSet<>(roles));
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UserDTO cambiarEstadoUsuario(Integer idUsuario, Boolean estado) {
        User usuario = obtenerUsuarioPorId(idUsuario);
        usuario.setEstado(estado);
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarUsuario(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    @Override
    public List<HotelDTO> listarHoteles() {
        return hotelMapper.hotelsToHotelDTOs(hotelRepository.findAll());
    }

    @Override
    public boolean eliminarHotelConDependencias(Integer idHotel) {
        Hotel hotel = hotelRepository.findById(idHotel).orElse(null);
        if (hotel != null) {
            hotelRepository.delete(hotel);
            return true;
        }
        return false;
    }

    @Override
    public List<RoomDTO> listarHabitaciones() {
        return habitacionRepository.findAll().stream()
                .map(habitacionMapper::habitacionToHabitacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> listarHabitacionesPorHotel(Integer idHotel) {
        return habitacionRepository.findByHotelId(idHotel).stream()
                .map(habitacionMapper::habitacionToHabitacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> listarReservas() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::reservaToReservaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> listarReservasPorHotel(Integer idHotel) {
        return reservaRepository.findByHabitacionHotelId(idHotel).stream()
                .map(reservaMapper::reservaToReservaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO obtenerReservaPorId(Integer idReserva) {
        return reservaRepository.findById(idReserva)
                .map(reservaMapper::reservaToReservaDTO)
                .orElse(null);
    }
}
