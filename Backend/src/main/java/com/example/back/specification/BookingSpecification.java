package com.example.back.specification;

import com.example.back.models.booking.Booking;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class BookingSpecification {

    public static Specification<Booking> hasUser(Integer userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("usuario").get("idUsuario"), userId);
    }

    public static Specification<Booking> hasStatus(String status) {
        return (root, query, cb) -> (status == null || status.isBlank()) ? null : 
                cb.equal(cb.lower(root.get("estado")), status.toLowerCase());
    }

    public static Specification<Booking> betweenDates(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null) return cb.between(root.get("fechaInicio"), start, end);
            if (start != null) return cb.greaterThanOrEqualTo(root.get("fechaInicio"), start);
            return cb.lessThanOrEqualTo(root.get("fechaFin"), end);
        };
    }
}
