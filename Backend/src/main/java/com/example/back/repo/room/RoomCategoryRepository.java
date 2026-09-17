package com.example.back.repo.room;

import com.example.back.models.room.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Integer> {
    @Query("SELECT c FROM RoomCategory c WHERE c.usuario.idUsuario = :usuarioId")
    List<RoomCategory> findByUsuarioIdUsuario(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT c FROM RoomCategory c WHERE c.id = :id AND c.usuario.idUsuario = :usuarioId")
    Optional<RoomCategory> findByIdAndUsuarioIdUsuario(@Param("id") Integer id, @Param("usuarioId") Integer usuarioId);
}
