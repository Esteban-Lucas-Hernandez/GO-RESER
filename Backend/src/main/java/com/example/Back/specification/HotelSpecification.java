package com.example.back.specification;

import com.example.back.models.hotel.Hotel;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecification {

    public static Specification<Hotel> hasCity(Integer cityId) {
        return (root, query, cb) -> cityId == null ? null : cb.equal(root.get("ciudad").get("id"), cityId);
    }

    public static Specification<Hotel> hasMinStars(Integer stars) {
        return (root, query, cb) -> stars == null ? null : cb.greaterThanOrEqualTo(root.get("estrellas"), stars);
    }

    public static Specification<Hotel> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank()) ? null : 
                cb.like(cb.lower(root.get("nombre")), "%" + name.toLowerCase() + "%");
    }
}
