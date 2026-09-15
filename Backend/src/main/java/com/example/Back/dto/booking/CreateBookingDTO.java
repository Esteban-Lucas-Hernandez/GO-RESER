package com.example.back.dto.booking;

import java.time.LocalDate;

public class CreateBookingDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String metodoPago;

    public CreateBookingDTO() {}

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
