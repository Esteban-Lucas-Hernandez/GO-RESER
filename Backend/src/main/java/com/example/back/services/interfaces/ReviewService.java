package com.example.back.services.interfaces;

import com.example.back.dto.review.ReviewDTO;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewDTO> obtenerResenasPorHotel(Integer hotelId);
    List<ReviewDTO> obtenerTodasLasResenas();
    List<ReviewDTO> obtenerResenasPorUsuario(Integer idUsuario);
    Optional<ReviewDTO> obtenerResenaPorId(Integer id);
    ReviewDTO crearResena(ReviewDTO resenaDTO, Integer hotelId) throws Exception;
    ReviewDTO actualizarResena(Integer id, ReviewDTO resenaDTO) throws Exception;
    void eliminarResena(Integer id) throws Exception;
    List<ReviewDTO> getResenasDeMisHoteles();
    List<ReviewDTO> getResenasPorHotelDeUsuario(Integer idHotel);
}
