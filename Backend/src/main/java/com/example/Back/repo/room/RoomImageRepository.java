package com.example.back.repo.room;

import com.example.back.models.room.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
    List<RoomImage> findByHabitacionIdHabitacion(Integer habitacionId);

    @Query("SELECT i FROM RoomImage i WHERE i.idImagen = :id AND i.habitacion.idHabitacion = :habitacionId AND i.habitacion.hotel.id = :hotelId")
    Optional<RoomImage> findByIdAndHabitacionIdAndHotelId(@Param("id") Integer id, @Param("habitacionId") Integer habitacionId, @Param("hotelId") Integer hotelId);

    @Query("SELECT i FROM RoomImage i WHERE i.habitacion.idHabitacion = :habitacionId AND i.habitacion.hotel.id = :hotelId")
    List<RoomImage> findByHabitacionIdAndHotelId(@Param("habitacionId") Integer habitacionId, @Param("hotelId") Integer hotelId);
}
