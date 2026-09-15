package com.example.back.dto.review;

public class ReviewDTO {
    private Integer idResena;
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idHotel;
    private String comentario;
    private Integer calificacion;
    private String fechaResena;
    private String fotoUrl;

    public ReviewDTO() {}

    public Integer getIdResena() { return idResena; }
    public void setIdResena(Integer idResena) { this.idResena = idResena; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Integer getIdHotel() { return idHotel; }
    public void setIdHotel(Integer idHotel) { this.idHotel = idHotel; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getFechaResena() { return fechaResena; }
    public void setFechaResena(String fechaResena) { this.fechaResena = fechaResena; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}
