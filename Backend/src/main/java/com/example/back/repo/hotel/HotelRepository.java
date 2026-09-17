package com.example.back.repo.hotel;

import com.example.back.models.hotel.Hotel;
import com.example.back.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer>, JpaSpecificationExecutor<Hotel> {
    List<Hotel> findByUsuario(User usuario);
    List<Hotel> findByUsuarioIdUsuario(Integer usuarioId);
    Optional<Hotel> findByIdAndUsuarioIdUsuario(Integer id, Integer usuarioId);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Hotel h WHERE h.id = :id AND h.usuario.idUsuario = :usuarioId")
    boolean existsByIdAndUsuarioIdUsuario(@Param("id") Integer id, @Param("usuarioId") Integer usuarioId);
}
