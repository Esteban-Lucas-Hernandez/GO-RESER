package com.example.back.models.payment;

import com.example.back.models.booking.Booking;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Booking reserva;

    @Column(name = "referencia_pago", unique = true)
    private String referenciaPago;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodo;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private Double monto;

    public enum MetodoPago {
        tarjeta,
        efectivo,
        transferencia,
        nequi,
        daviplata
    }

    public Payment() {}

    @PrePersist
    public void prePersist() {
        if (fechaPago == null) {
            fechaPago = LocalDateTime.now();
        }
    }

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }

    public Booking getReserva() { return reserva; }
    public void setReserva(Booking reserva) { this.reserva = reserva; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
