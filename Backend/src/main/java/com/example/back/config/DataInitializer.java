package com.example.back.config;

import com.example.back.models.user.Role;
import com.example.back.models.user.User;
import com.example.back.models.booking.Booking;
import com.example.back.models.payment.Payment;
import com.example.back.repo.booking.BookingRepository;
import com.example.back.repo.payment.PaymentRepository;
import com.example.back.repo.user.RoleRepository;
import com.example.back.repo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookingRepository reservaRepository;
    private final PaymentRepository pagoRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository usuarioRepository, 
                           PasswordEncoder passwordEncoder, BookingRepository reservaRepository, 
                           PaymentRepository pagoRepository) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservaRepository = reservaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            Role roleSuperAdmin = roleRepository.findByName("ROLE_SUPERADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_SUPERADMIN")));

            if (usuarioRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User();
                admin.setNombreCompleto("Administrador Principal");
                admin.setEmail("admin@admin.com");
                admin.setTelefono("0000000000");
                admin.setDocumento("ADMIN000");
                admin.setContrasena(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(roleAdmin));
                usuarioRepository.save(admin);
                System.out.println("✅ ADMIN creado (ID 1): admin@admin.com / admin123");
            }

            if (usuarioRepository.findByEmail("superadmin@admin.com").isEmpty()) {
                User superAdmin = new User();
                superAdmin.setNombreCompleto("Super Administrador");
                superAdmin.setEmail("superadmin@admin.com");
                superAdmin.setTelefono("0000000001");
                superAdmin.setDocumento("SUPERADMIN001");
                superAdmin.setContrasena(passwordEncoder.encode("superadmin123"));
                superAdmin.setRoles(Set.of(roleSuperAdmin));
                usuarioRepository.save(superAdmin);
                System.out.println("✅ SUPERADMIN creado (ID 2): superadmin@admin.com / superadmin123");
            }

            if (usuarioRepository.findByEmail("cliente@gmail.com").isEmpty()) {
                User cliente = new User();
                cliente.setNombreCompleto("Lucas Hernández (Cliente)");
                cliente.setEmail("cliente@gmail.com");
                cliente.setTelefono("+57 300 455 4430");
                cliente.setDocumento("1114291676");
                cliente.setContrasena(passwordEncoder.encode("123456"));
                cliente.setRoles(Set.of(roleUser));
                usuarioRepository.save(cliente);
                System.out.println("✅ CLIENTE creado (ID 3): cliente@gmail.com / 123456");
            }

            // Sincronizar pagos para el 100% de las reservas existentes que no tengan pago
            List<Booking> todasLasReservas = reservaRepository.findAll();
            for (Booking r : todasLasReservas) {
                List<Payment> pagos = pagoRepository.findByReservaIdReserva(r.getIdReserva());
                if (pagos == null || pagos.isEmpty()) {
                    Payment pago = new Payment();
                    pago.setReserva(r);
                    pago.setMonto(r.getTotal() != null ? r.getTotal() : 0.0);
                    pago.setFechaPago(r.getFechaReserva() != null ? r.getFechaReserva() : LocalDateTime.now());
                    pago.setReferenciaPago("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    try {
                        pago.setMetodo(Payment.MetodoPago.valueOf(r.getMetodoPago() != null ? r.getMetodoPago().name() : "tarjeta"));
                    } catch (Exception e) {
                        pago.setMetodo(Payment.MetodoPago.tarjeta);
                    }
                    pagoRepository.save(pago);
                    System.out.println("✅ Pago sincronizado para reserva ID " + r.getIdReserva());
                }
            }
        };
    }
}

