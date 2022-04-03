# Getting Started

### Json Web Token (JWT)

- Agregar dependencias de JWT y actualizar archivo pom.xml
	- https://github.com/jwtk/jjwt
- Desabilitar csrf y declarar como sin estado la sesión en la clase SpringSecurityConfig
	- [clase SpringSecurityConfig](src/main/java/com/vaescode/springboot/app/SpringSecurityConfig.java)
	
- Quitar las referencias a csrf en vistas layout y login 
	- [login](src\main\resources\templates\layout\layout.html)