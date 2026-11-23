package org.goya.daw2.dwes.d2_ud3;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private static final String USUARIO = "usuario";

    // Usuarios válidos y sus contraseñas hasheadas con BCrypt.
    // La contraseña original nunca aparece en el código.
    private static final Map<String, String> USUARIOS = Map.of(
            "ana", "$2a$10$uV.vtipyF7xl4CAofM7FK.qmCe3H/ghAVLFP//3ZYL78/ct1Mnhf2",
            "juan", "$2a$10$n0jtPNaaYyqX/Hvxw0yxuuWqbdiwqC3OFDNNpBVvLRQCTdwJpoFaS",
            "profesor", "$2a$10$Db56YCWbURFaItBjI2K.yuz9isLiVULvv4KPpTA1JYmHbTAQvWRYy");

    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    // Las tres páginas privadas comparten el mismo método y la misma plantilla.
    @GetMapping({ "/", "/pagina1", "/pagina2" })
    public String pagina(HttpServletRequest peticion, HttpSession sesion, Model modelo) {
        String nombre = (String) sesion.getAttribute(USUARIO);

        if (nombre == null) {
            return "redirect:/login";
        }

        String ruta = peticion.getRequestURI();
        String pagina = ruta.equals("/") ? "inicio" : ruta.substring(1);

        modelo.addAttribute("usuario", nombre);
        modelo.addAttribute("pagina", pagina);
        return "pagina";
    }

    // Si ya está autenticado, /login muestra directamente la página de inicio.
    @GetMapping("/login")
    public String verLogin(HttpSession sesion, Model modelo) {
        String nombre = (String) sesion.getAttribute(USUARIO);

        if (nombre != null) {
            modelo.addAttribute("usuario", nombre);
            modelo.addAttribute("pagina", "inicio");
            return "pagina";
        }

        return "login";
    }

    // Comprobación de la contraseña: se compara con el hash guardado.
    @PostMapping("/login")
    public String entrar(
            @RequestParam(value = "usuario", defaultValue = "") String usuario,
            @RequestParam(value = "password", defaultValue = "") String password,
            HttpSession sesion, Model modelo) {

        String hash = USUARIOS.get(usuario);

        if (hash == null || !codificador.matches(password, hash)) {
            modelo.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }

        sesion.setAttribute(USUARIO, usuario);
        return "redirect:/";
    }

    // Cerrar sesión: se borra la sesión y vuelve a pedir las credenciales.
    @GetMapping("/logout")
    public String salir(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}
