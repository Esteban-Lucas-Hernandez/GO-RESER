package com.example.back.dto.room;

public class RoomImageDTO {
    private Integer idImagen;
    private Integer idHabitacion;
    private String urlImagen;

    public RoomImageDTO() {}

    public Integer getIdImagen() { return idImagen; }
    public void setIdImagen(Integer idImagen) { this.idImagen = idImagen; }

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
