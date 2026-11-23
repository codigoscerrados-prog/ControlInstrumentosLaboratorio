package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("usuarioPermisoService")
@Transactional(readOnly = true)
public class UsuarioPermisoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioPermisoService(
        UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean puedeEditar() {

        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(
                usuarioRepository::
                    findOneByUsernameIgnoreCase
            )
            .map(usuario -> {

                if (usuario.getRol() == null ||
                    usuario.getRol().getNombre() == null) {
                    return false;
                }

                String rol =
                    usuario.getRol()
                        .getNombre();

                return rol.equalsIgnoreCase("ADMIN")
                    || rol.equalsIgnoreCase("TECNICO");
            })
            .orElse(false);
    }
}
