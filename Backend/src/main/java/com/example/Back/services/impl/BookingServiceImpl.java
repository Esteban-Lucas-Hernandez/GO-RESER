package com.example.back.services.impl;

import com.example.back.dto.booking.BookingDTO;
import com.example.back.dto.booking.CreateBookingDTO;
import com.example.back.mapper.booking.BookingMapper;
import com.example.back.models.booking.Booking;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.room.Room;
import com.example.back.models.user.User;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.room.RoomRepository;
import com.example.back.services.interfaces.BookingService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository reservaRepository;

    @Autowired
    private RoomRepository habitacionRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private SecurityService securityService;

    private final BookingMapper reservaMapper = BookingMapper.INSTANCE;

    @Override
    public BookingDTO crearReserva(Integer idHabitacion, CreateBookingDTO reservaDTO) {
        User usuario = securityService.getAuthenticatedUser();
        Room habitacion = habitacionRepository.findByIdHabitacion(idHabitacion)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        if (reservaDTO.getFechaInicio().isAfter(reservaDTO.getFechaFin()) ||
                reservaDTO.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException("Rango de fechas no válido");
        }

        if (reservaRepository.existsSolapadas(habitacion, reservaDTO.getFechaInicio(), reservaDTO.getFechaFin())) {
            throw new RuntimeException("La habitación no está disponible para las fechas seleccionadas");
        }

        long dias = ChronoUnit.DAYS.between(reservaDTO.getFechaInicio(), reservaDTO.getFechaFin());
        if (dias <= 0) dias = 1;
        double total = dias * habitacion.getPrecio();

        Booking reserva = new Booking();
        reserva.setUsuario(usuario);
        reserva.setHabitacion(habitacion);
        reserva.setFechaInicio(reservaDTO.getFechaInicio());
        reserva.setFechaFin(reservaDTO.getFechaFin());
        reserva.setTotal(total);
        reserva.setEstado(Booking.EstadoReserva.pendiente);
        reserva.setFechaReserva(LocalDateTime.now());

        if (reservaDTO.getMetodoPago() != null) {
            try {
                reserva.setMetodoPago(Booking.MetodoPago.valueOf(reservaDTO.getMetodoPago().toLowerCase()));
            } catch (Exception e) {
                reserva.setMetodoPago(Booking.MetodoPago.tarjeta);
            }
        }

        Booking guardada = reservaRepository.save(reserva);
        return reservaMapper.reservaToReservaDTO(guardada);
    }

    @Override
    public List<BookingDTO> getReservasPorUsuario() {
        User usuario = securityService.getAuthenticatedUser();
        return reservaRepository.findByUsuario(usuario).stream()
                .map(reservaMapper::reservaToReservaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getReservasPorHabitacion(Integer idHabitacion) {
        Room habitacion = habitacionRepository.findByIdHabitacion(idHabitacion)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        return reservaRepository.findByHabitacion(habitacion).stream()
                .map(reservaMapper::reservaToReservaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getReservasDeMisHoteles() {
        User currentUser = securityService.getAuthenticatedUser();
        List<Hotel> misHoteles = hotelRepository.findByUsuarioIdUsuario(currentUser.getIdUsuario());
        List<BookingDTO> reservas = new ArrayList<>();
        for (Hotel h : misHoteles) {
            reservas.addAll(getReservasPorHotelDeUsuario(h.getId()));
        }
        return reservas;
    }

    @Override
    public List<BookingDTO> getReservasPorHotelDeUsuario(Integer idHotel) {
        return reservaRepository.findByHabitacionHotelId(idHotel).stream()
                .map(reservaMapper::reservaToReservaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO cancelarReserva(Integer idReserva) {
        User usuario = securityService.getAuthenticatedUser();
        Booking reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("No tienes permisos para cancelar esta reserva");
        }

        reserva.setEstado(Booking.EstadoReserva.cancelada);
        Booking guardada = reservaRepository.save(reserva);
        return reservaMapper.reservaToReservaDTO(guardada);
    }

    @Override
    public Booking getReservaPorId(Integer idReserva) {
        return reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public List<Object[]> getFechasReservadas(Integer idHabitacion) {
        return getFechasReservadasConfirmadas(idHabitacion);
    }

    @Override
    public List<Object[]> getFechasReservadasConfirmadas(Integer idHabitacion) {
        Room habitacion = habitacionRepository.findByIdHabitacion(idHabitacion)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        return reservaRepository.findFechasReservadasConfirmadas(habitacion);
    }

    @Override
    public BookingDTO confirmarReserva(Integer idReserva) {
        Booking reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(Booking.EstadoReserva.confirmada);
        Booking guardada = reservaRepository.save(reserva);
        return reservaMapper.reservaToReservaDTO(guardada);
    }

    @Override
    public int eliminarReservasAntiguasYCanceladas() {
        return 0;
    }
}
