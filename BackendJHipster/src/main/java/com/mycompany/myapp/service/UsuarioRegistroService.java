package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.RolRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.RegistroUsuarioDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioRegistroService {

    private static final String ENTITY_NAME = "usuario";

    private final UserRepository userRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioRegistroService(
        UserRepository userRepository,
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegistroUsuarioDTO dto) {

        String username =
            dto.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        String correo =
            dto.getCorreo()
                .trim()
                .toLowerCase(Locale.ROOT);

        String nombreRol =
            dto.getRol()
                .trim()
                .toUpperCase(Locale.ROOT);


        // Solo existen estos tres roles funcionales.
        if (
            !nombreRol.equals("ADMIN") &&
            !nombreRol.equals("TECNICO") &&
            !nombreRol.equals("CONSULTA")
        ) {
            throw new BadRequestAlertException(
                "Rol no permitido",
                ENTITY_NAME,
                "rolinvalido"
            );
        }


        // No permitir username duplicado.
        if (
            userRepository.findOneByLogin(username).isPresent() ||
            usuarioRepository.existsByUsernameIgnoreCase(username)
        ) {
            throw new BadRequestAlertException(
                "El nombre de usuario ya existe",
                ENTITY_NAME,
                "usernameexists"
            );
        }


        // No permitir correo duplicado.
        if (
            userRepository.findOneByEmailIgnoreCase(correo).isPresent() ||
            usuarioRepository.existsByCorreoIgnoreCase(correo)
        ) {
            throw new BadRequestAlertException(
                "El correo ya existe",
                ENTITY_NAME,
                "emailexists"
            );
        }


        Rol rol =
            rolRepository
                .findOneByNombreIgnoreCase(nombreRol)
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "El rol no existe",
                            ENTITY_NAME,
                            "rolnotfound"
                        )
                );


        /*
         * ==================================================
         * 1. CUENTA REAL DE AUTENTICACIÓN JHIPSTER
         * ==================================================
         */

        User user = new User();

        user.setLogin(username);

        user.setEmail(correo);

        user.setPassword(
            passwordEncoder.encode(
                dto.getPassword()
            )
        );

        user.setActivated(true);

        user.setLangKey("es");


        Set<Authority> authorities =
            new HashSet<>();


        Authority authorityUser =
            authorityRepository
                .findById(
                    AuthoritiesConstants.USER
                )
                .orElseThrow();


        authorities.add(
            authorityUser
        );


        /*
         * Si el rol funcional es ADMIN,
         * también recibe ROLE_ADMIN
         * dentro de Spring Security.
         */
        if (nombreRol.equals("ADMIN")) {

            Authority authorityAdmin =
                authorityRepository
                    .findById(
                        AuthoritiesConstants.ADMIN
                    )
                    .orElseThrow();

            authorities.add(
                authorityAdmin
            );
        }


        user.setAuthorities(
            authorities
        );


        userRepository.save(
            user
        );


        /*
         * ==================================================
         * 2. USUARIO FUNCIONAL DEL SISTEMA
         * ==================================================
         */

        String usuarioCreacion =
            SecurityUtils
                .getCurrentUserLogin()
                .orElse("SYSTEM");


        Usuario usuario =
            new Usuario();

        usuario.setUsername(
            username
        );

        /*
         * Esta columna existe porque fue generada
         * originalmente por la entidad Usuario.
         *
         * NO guardamos la contraseña real aquí.
         */
        usuario.setPassword(
            "GESTIONADA_EN_JHI_USER"
        );

        usuario.setCorreo(
            correo
        );

        usuario.setEstado(
            "ACTIVO"
        );

        usuario.setRol(
            rol
        );

        usuario.setFechaCreacion(
            Instant.now()
        );

        usuario.setUsuarioCreacion(
            usuarioCreacion
        );

        usuario.setFechaModificacion(
            null
        );

        usuario.setUsuarioModificacion(
            null
        );


        return usuarioRepository.save(
            usuario
        );
    }
}
