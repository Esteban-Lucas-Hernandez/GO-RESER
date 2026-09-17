package com.example.back.models.hotel;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "departamentos")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public State() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
