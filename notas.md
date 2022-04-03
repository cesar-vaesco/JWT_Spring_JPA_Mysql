# Getting Started

### Json Web Token (JWT)

##### 1 -->

- Agregar dependencias de JWT y actualizar archivo pom.xml
	- https://github.com/jwtk/jjwt
- Desabilitar csrf y declarar como sin estado la sesión en la clase SpringSecurityConfig
	- [clase SpringSecurityConfig](src/main/java/com/vaescode/springboot/app/SpringSecurityConfig.java)
	
- Quitar las referencias a csrf en vistas layout y login 
	- [login](src\main\resources\templates\layout\layout.html)
	
##### 2 -->

- Se elimino el acceso a la ruta del login en la clase SpringSecurityConfig, en este paso al probar nuestra aplicación, no se tienen acceso a ninguna de nuestras rutas: se genera un HTTP ERROR 403 foorbiden
	- [clase SpringSecurityConfig](src/main/java/com/vaescode/springboot/app/SpringSecurityConfig.java)