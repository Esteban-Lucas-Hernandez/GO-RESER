package com.example.back.repo.room;

import com.example.back.models.room.Room;
import com.example.back.models.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByHotel(Hotel hotel);
    List<Room> findByHotelId(Integer hotelId);
    List<Room> findByHotelAndEstado(Hotel hotel, Room.EstadoHabitacion estado);
    Optional<Room> findByIdHabitacion(Integer idHabitacion);

    @Query("SELECT h FROM Room h WHERE h.idHabitacion = :idHabitacion AND h.hotel.id = :hotelId")
    Optional<Room> findByIdHabitacionAndHotelId(@Param("idHabitacion") Integer idHabitacion, @Param("hotelId") Integer hotelId);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Room h WHERE h.idHabitacion = :idHabitacion AND h.hotel.id = :hotelId")
    boolean existsByIdHabitacionAndHotelId(@Param("idHabitacion") Integer idHabitacion, @Param("hotelId") Integer hotelId);

    @Query("SELECT COUNT(h) FROM Room h WHERE h.categoria.id = :categoriaId")
    long countByCategoriaId(@Param("categoriaId") Integer categoriaId);
}
