package com.example.back.dto.payment;

import java.time.LocalDateTime;

public class PaymentDetailDTO {
    private Integer idPago;
    private Integer idReserva;
    private String referenciaPago;
    private String metodo;
    private LocalDateTime fechaPago;
    private Double monto;
    private String nombreHabitacion;
    private String nombreHotel;
    private String nombreUsuario;

    public PaymentDetailDTO() {}

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getNombreHabitacion() { return nombreHabitacion; }
    public void setNombreHabitacion(String nombreHabitacion) { this.nombreHabitacion = nombreHabitacion; }

    public String getNombreHotel() { return nombreHotel; }
    public void setNombreHotel(String nombreHotel) { this.nombreHotel = nombreHotel; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}
