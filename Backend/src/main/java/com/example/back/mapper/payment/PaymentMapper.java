package com.example.back.mapper.payment;

import com.example.back.dto.payment.PaymentDTO;
import com.example.back.dto.payment.PaymentDetailDTO;
import com.example.back.models.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "reserva.idReserva", target = "idReserva")
    PaymentDTO pagoToPagoDTO(Payment pago);

    @Mapping(source = "reserva.idReserva", target = "idReserva")
    @Mapping(source = "reserva.habitacion.numero", target = "nombreHabitacion")
    @Mapping(source = "reserva.habitacion.hotel.nombre", target = "nombreHotel")
    @Mapping(source = "reserva.usuario.nombreCompleto", target = "nombreUsuario")
    PaymentDetailDTO pagoToPagoDetalladoDTO(Payment pago);
}
