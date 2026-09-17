package com.example.back.dto.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {
    private Integer idReserva;
    private String emailUsuario;
    private String numeroHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double total;
    private String estado;
    private String metodoPago;
    private LocalDateTime fechaReserva;
    private String nombreHotel;
    private String urlImagenHabitacion;

    public BookingDTO() {}

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getNombreHotel() { return nombreHotel; }
    public void setNombreHotel(String nombreHotel) { this.nombreHotel = nombreHotel; }

    public String getUrlImagenHabitacion() { return urlImagenHabitacion; }
    public void setUrlImagenHabitacion(String urlImagenHabitacion) { this.urlImagenHabitacion = urlImagenHabitacion; }
}
