package com.example.back.services.impl;

import com.example.back.dto.review.ReviewDTO;
import com.example.back.mapper.review.ReviewMapper;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.review.Review;
import com.example.back.models.user.User;
import com.example.back.repo.hotel.HotelRepository;
import com.example.back.repo.review.ReviewRepository;
import com.example.back.services.interfaces.ReviewService;
import com.example.back.services.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository resenaRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewMapper resenaMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public List<ReviewDTO> obtenerResenasPorHotel(Integer hotelId) {
        return resenaRepository.findByHotelId(hotelId).stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> obtenerTodasLasResenas() {
        return resenaRepository.findAll().stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> obtenerResenasPorUsuario(Integer idUsuario) {
        return resenaRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(resenaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewDTO> obtenerResenaPorId(Integer id) {
        return resenaRepository.findById(id).map(resenaMapper::toDTO);
    }

    @Override
    public ReviewDTO crearResena(ReviewDTO resenaDTO, Integer hotelId) throws Exception {
        User usuario = securityService.getAuthenticatedUser();
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        if (!resenaRepository.hasConfirmedReservation(usuario, hotel)) {
            throw new RuntimeException("Solo puedes dejar una reseña si tienes una reserva confirmada en este hotel");
        }

        Review resena = resenaMapper.toEntity(resenaDTO);
        resena.setUsuario(usuario);
        resena.setHotel(hotel);
        resena.setFechaResena(LocalDate.now().toString());

        Review guardada = resenaRepository.save(resena);
        return resenaMapper.toDTO(guardada);
    }

    @Override
    public ReviewDTO actualizarResena(Integer id, ReviewDTO resenaDTO) throws Exception {
        User usuario = securityService.getAuthenticatedUser();
        Review resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (!resena.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("No tienes permisos para editar esta reseña");
        }

        resena.setComentario(resenaDTO.getComentario());
        resena.setCalificacion(resenaDTO.getCalificacion());
        Review guardada = resenaRepository.save(resena);
        return resenaMapper.toDTO(guardada);
    }

    @Override
    public void eliminarResena(Integer id) throws Exception {
        User usuario = securityService.getAuthenticatedUser();
        Review resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (!resena.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("No tienes permisos para eliminar esta reseña");
        }

        resenaRepository.delete(resena);
    }

    @Override
    public List<ReviewDTO> getResenasDeMisHoteles() {
        User currentUser = securityService.getAuthenticatedUser();
        List<Hotel> misHoteles = hotelRepository.findByUsuarioIdUsuario(currentUser.getIdUsuario());
        List<ReviewDTO> resenas = new ArrayList<>();
        for (Hotel h : misHoteles) {
            resenas.addAll(obtenerResenasPorHotel(h.getId()));
        }
        return resenas;
    }

    @Override
    public List<ReviewDTO> getResenasPorHotelDeUsuario(Integer idHotel) {
        return obtenerResenasPorHotel(idHotel);
    }
}
