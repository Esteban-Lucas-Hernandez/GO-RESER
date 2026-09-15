package com.example.back.repo.booking;

import com.example.back.models.booking.Booking;
import com.example.back.models.user.User;
import com.example.back.models.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    
    @Query("SELECT COUNT(r) > 0 FROM Booking r WHERE r.habitacion = :habitacion " +
           "AND r.estado = 'confirmada' " +
           "AND ((r.fechaInicio <= :fin AND r.fechaFin >= :inicio))")
    boolean existsSolapadas(@Param("habitacion") Room habitacion,
                           @Param("inicio") LocalDate inicio,
                           @Param("fin") LocalDate fin);

    List<Booking> findByUsuario(User usuario);
    List<Booking> findByUsuarioIdUsuario(Integer usuarioId);
    List<Booking> findByHabitacion(Room habitacion);
    List<Booking> findByHabitacionHotelId(Integer hotelId);

    @Query("SELECT r.fechaInicio, r.fechaFin FROM Booking r WHERE r.habitacion = :habitacion " +
           "AND r.estado = 'confirmada' AND r.fechaFin >= CURRENT_DATE")
    List<Object[]> findFechasReservadasConfirmadas(@Param("habitacion") Room habitacion);
}
