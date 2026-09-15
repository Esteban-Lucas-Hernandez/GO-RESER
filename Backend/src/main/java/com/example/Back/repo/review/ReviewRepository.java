package com.example.back.repo.review;

import com.example.back.models.review.Review;
import com.example.back.models.hotel.Hotel;
import com.example.back.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByHotelId(Integer hotelId);

    @Query("SELECT r FROM Review r WHERE r.usuario.idUsuario = :idUsuario")
    List<Review> findByUsuarioIdUsuario(@Param("idUsuario") Integer idUsuario);

    Optional<Review> findByUsuarioAndHotel(User usuario, Hotel hotel);

    @Query("SELECT COUNT(r) > 0 FROM Booking r WHERE r.usuario = :usuario AND r.habitacion.hotel = :hotel AND r.estado = 'confirmada'")
    boolean hasConfirmedReservation(@Param("usuario") User usuario, @Param("hotel") Hotel hotel);
}
