package com.example.back.models.room;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_habitacion")
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Integer idImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Room habitacion;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    public RoomImage() {}

    public RoomImage(Room habitacion, String urlImagen) {
        this.habitacion = habitacion;
        this.urlImagen = urlImagen;
    }

    public Integer getIdImagen() { return idImagen; }
    public void setIdImagen(Integer idImagen) { this.idImagen = idImagen; }

    public Room getHabitacion() { return habitacion; }
    public void setHabitacion(Room habitacion) { this.habitacion = habitacion; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
