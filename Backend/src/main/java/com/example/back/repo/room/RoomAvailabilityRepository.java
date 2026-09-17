package com.example.back.repo.room;

import com.example.back.models.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT h FROM Room h WHERE h.estado = com.example.back.models.room.Room.EstadoHabitacion.disponible ORDER BY h.precio ASC")
    List<Room> findCheapestRooms();

    List<Room> findTop1ByEstadoOrderByPrecioAsc(Room.EstadoHabitacion estado);
}
