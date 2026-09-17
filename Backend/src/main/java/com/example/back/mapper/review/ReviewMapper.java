package com.example.back.mapper.review;

import com.example.back.dto.review.ReviewDTO;
import com.example.back.models.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDTO toDTO(Review resena) {
        if (resena == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setIdResena(resena.getIdResena());
        if (resena.getUsuario() != null) {
            dto.setIdUsuario(resena.getUsuario().getIdUsuario());
            dto.setNombreUsuario(resena.getUsuario().getNombreCompleto());
            dto.setFotoUrl(resena.getUsuario().getFotoUrl());
        }
        if (resena.getHotel() != null) {
            dto.setIdHotel(resena.getHotel().getId());
        }
        dto.setComentario(resena.getComentario());
        dto.setCalificacion(resena.getCalificacion());
        dto.setFechaResena(resena.getFechaResena());
        return dto;
    }

    public Review toEntity(ReviewDTO resenaDTO) {
        if (resenaDTO == null) return null;
        Review resena = new Review();
        resena.setIdResena(resenaDTO.getIdResena());
        resena.setComentario(resenaDTO.getComentario());
        resena.setCalificacion(resenaDTO.getCalificacion());
        return resena;
    }
}
