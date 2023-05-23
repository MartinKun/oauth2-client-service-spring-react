

<h1>Implementación de Spring Boot 2.7.1 Security con OAuth2</h1>
<br />
<p align = "center"> <img src = "https://res.cloudinary.com/djarwlymo/image/upload/v1684853010/oauth2_kib5km.png" /> </p>
<p>Este proyecto es una implementación del protocolo de autenticación y autorización OAuth2, que permite a las aplicaciones delegar la autenticación de usuarios en servicios de terceros como Google, Facebook y GitHub. Además de la integración con proveedores externos, el proyecto también ofrece un sistema de registro propio con confirmación por correo electrónico. Asimismo, se ha implementado el uso de tokens de actualización (refresh tokens) para proporcionar a los usuarios la capacidad de renovar sus tokens de acceso sin necesidad de autenticarse nuevamente. Esta combinación de características brinda a los usuarios una experiencia de autenticación segura y flexible.</p>
<br />
<h2>Funcionalidades:</h2>

- Autenticación con proveedores externos como Google, Facebook y GitHub
- Registro de usuario y login con JWT authentication
- Encriptación de contraseña utilizando BCrypt
- Envío de mails utilizando la librería de java Mail para la activación de la cuenta
<br />
<h2>Tecnologías:</h2>

- Framework: Spring Boot 2.7.11
- Seguridad: Spring Security, JSON Web Tokens (JWT), BCrypt
- Persistencia de datos: Spring Data JPA, Hibernate
- Base de datos: MySQL
- Manejo de dependencias: Maven
- Autenticación y autorización de terceros: Spring Security OAuth2 Client
- Frontend: React, Redux
- Integración de API externas: OAuth2 (Google, Facebook, GitHub)
- Correo electrónico: JavaMail API
- Plantillas de correo: Thymeleaf (Thymeleaf-Spring5 3.0.11.RELEASE)
- Validación de datos: Spring Validation
<br />
<h2>Cómo Ejecutarlo</h2>

Requerimientos: Primero necesitarás instalar lo siguiente:

- JDK 11+
- MySQL workbench

Crear una base de datos en MySQl de nombre <code>oauth2</code>

<p>Además, debes configurar las siguientes variables de entorno en tu sistema o en el archivo <code>application.yml</code>:</p>

- <code>DB_USERNAME</code>: nombre de usuario de la base de datos.
- <code>DB_PASSWORD</code>: contraseña del usuario de la base de datos.
- <code>ACCESS_SECRET</code>: clave secreta para generar y verificar los tokens de acceso.
- <code>ACCESS_EXPIRATION</code>: tiempo de expiración en horas del token de acceso.
- <code>REFRESH_SECRET</code>: clave secreta para generar y verificar los tokens de actualización.
- <code>REFRESH_EXPIRATION</code>: tiempo de expiración en horas del token de actualización.
- <code>CONFIRMATION_SECRET</code>: clave secreta para generar y verificar los tokens de confirmación.
- <code>CONFIRMATION_EXPIRATION</code>: tiempo de expiración en horas del token de confirmación.
- <code>EMAIL_USERNAME</code>: nombre de usuario del correo electrónico.
- <code>EMAIL_PASSWORD</code>: contraseña del correo electrónico.
- <code>EMAIL_HOST</code>: host del servidor de correo electrónico.

Una vez que obtengas las credenciales de los proveedores de autenticación, debes reemplazarlas en los siguientes campos:

- <code>GOOGLE_CLIENT_ID</code>, <code>GITHUB_CLIENT_ID</code>, <code>FACEBOOK_CLIENT_ID</code>: Identificadores de cliente para la integración de inicio de sesión con Google, GitHub y Facebook respectivamente.
- <code>GOOGLE_SECRET</code>, <code>GITHUB_SECRET</code>, <code>FACEBOOK_SECRET</code>: Claves secretas para la integración de inicio de sesión con Google, GitHub y Facebook respectivamente.</p>
<p>También puedes agregar otros proveedores de inicio de sesión modificando la configuración en el archivo <code>application.yml</code> y agregando las variables de entorno correspondientes.</p>

Cumplidos estos pasos, puedes clonar el proyecto y ejecutarlo en tu IDE preferido.

 
