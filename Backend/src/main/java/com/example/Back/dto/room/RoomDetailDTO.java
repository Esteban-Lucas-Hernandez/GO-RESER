package com.example.back.dto.room;

import java.time.LocalTime;
import java.util.List;

public class RoomDetailDTO {
    private Integer idHabitacion;
    private Integer idHotel;
    private RoomCategoryDTO categoria;
    private String numero;
    private Integer capacidad;
    private Double precio;
    private String descripcion;
    private String estado;
    private List<String> imagenesUrls;
    private String hotelNombre;
    private String descripcionHotel;
    private String ciudadNombre;
    private String departamentoNombre;
    private LocalTime checkIn;
    private LocalTime checkOut;

    public RoomDetailDTO() {}

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public Integer getIdHotel() { return idHotel; }
    public void setIdHotel(Integer idHotel) { this.idHotel = idHotel; }

    public RoomCategoryDTO getCategoria() { return categoria; }
    public void setCategoria(RoomCategoryDTO categoria) { this.categoria = categoria; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<String> getImagenesUrls() { return imagenesUrls; }
    public void setImagenesUrls(List<String> imagenesUrls) { this.imagenesUrls = imagenesUrls; }

    public String getHotelNombre() { return hotelNombre; }
    public void setHotelNombre(String hotelNombre) { this.hotelNombre = hotelNombre; }

    public String getDescripcionHotel() { return descripcionHotel; }
    public void setDescripcionHotel(String descripcionHotel) { this.descripcionHotel = descripcionHotel; }

    public String getCiudadNombre() { return ciudadNombre; }
    public void setCiudadNombre(String ciudadNombre) { this.ciudadNombre = ciudadNombre; }

    public String getDepartamentoNombre() { return departamentoNombre; }
    public void setDepartamentoNombre(String departamentoNombre) { this.departamentoNombre = departamentoNombre; }

    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }

    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }
}
