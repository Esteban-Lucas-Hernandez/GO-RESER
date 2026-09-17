package com.example.back.models.review;

import com.example.back.models.hotel.Hotel;
import com.example.back.models.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "resenas")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Integer idResena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hotel", nullable = false)
    private Hotel hotel;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private Integer calificacion; // 1 a 5

    @Column(name = "fecha_resena")
    private String fechaResena;

    public Review() {}

    public Integer getIdResena() { return idResena; }
    public void setIdResena(Integer idResena) { this.idResena = idResena; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getFechaResena() { return fechaResena; }
    public void setFechaResena(String fechaResena) { this.fechaResena = fechaResena; }
}
