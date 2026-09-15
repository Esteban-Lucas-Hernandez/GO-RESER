package com.example.back.controllers.superadmin;

import com.example.back.dto.user.UserDTO;
import com.example.back.services.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superadmin/usuarios")
public class SuperAdminUsersController {

    @Autowired
    private SuperAdminService superAdminService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> listarUsuarios() {
        return ResponseEntity.ok(superAdminService.listarUsuarios());
    }

    @PutMapping("/{idUsuario}/roles")
    public ResponseEntity<UserDTO> actualizarRoles(
            @PathVariable Integer idUsuario,
            @RequestBody List<String> roles) {
        return ResponseEntity.ok(superAdminService.actualizarRoles(idUsuario, roles));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer idUsuario) {
        superAdminService.eliminarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
