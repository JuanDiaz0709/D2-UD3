# D2-UD3 · Login con contraseñas hasheadas

Web sencilla de Spring Boot con cuatro rutas: `/`, `/login`, `/pagina1` y `/pagina2`.

## Comportamiento

- Si intentas entrar en `/`, `/pagina1` o `/pagina2` sin iniciar sesión, te redirige a `/login`.
- En `/login` se piden usuario y contraseña. Si son correctos, se guarda el nombre en la `HttpSession` y se muestra la página de inicio.
- Si son incorrectos, vuelve a mostrarse el formulario con un mensaje de error.
- Si ya has iniciado sesión y pides `/login`, te lleva directamente a la página de inicio.
- El enlace *Cerrar sesión* invalida la sesión y vuelve a pedir credenciales.

## Usuarios de prueba

| Usuario | Contraseña |
|---|---|
| ana | 1234 |
| juan | abcd |
| profesor | clave |

En el código **no** están esas contraseñas, sino su hash BCrypt. Se comprueban con `BCryptPasswordEncoder.matches()`.

## Ejecutar

Necesitas JDK 21. Desde esta carpeta:

```bat
gradlew.bat bootRun
```

Abre `http://localhost:8080`.

## Estructura

- `src/main/java/org/goya/daw2/dwes/d2_ud3/AuthController.java`: mappings, sesión y comprobación del hash.
- `src/main/java/org/goya/daw2/dwes/d2_ud3/D2Ud3Application.java`: arranque de Spring Boot.
- `src/main/resources/templates/login.html`: formulario.
- `src/main/resources/templates/pagina.html`: plantilla común para inicio, página 1 y página 2.
