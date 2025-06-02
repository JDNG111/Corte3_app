Programación móvil - 2025

¡¡ACTUALIZACIONES!!

Ahora la app cuenta con un login para el inicio de sesión y guardado de datos (el progreso del usuario en el aprendizaje de ingles) en la nube:
![image](https://github.com/user-attachments/assets/0dca6a01-f593-4090-a656-ee409748cb52)

Para dicho login, hacemos uso de "Firebase Authentication", el cual se encarga de gestionar, tanto a los nuevos usuarios, asi como a las cuentas creadas junto con su correo electronico y respectiva contraseña:
![image](https://github.com/user-attachments/assets/78ca3869-140e-494c-9770-7881c7cd9e37)

Como lo hemos mencionado, la aplicación ahora hace uso de Firebase para el almacenamiento de datos, de tal forma, hemos implementado una nueva pantalla, la cual nos permite guardar, leer, eliminar y actualizar palabras en ingles.
Dichas palabras solo son visibles para la respectiva cuenta del usuario que las guardo (he alli, la importancia de la integración y login con Firebase para la personalización y privacidad de los usuarios)
![image](https://github.com/user-attachments/assets/b037d5b8-395b-4188-a853-8e10f99c4d05)

Ademas integraremos nuevas funciones basadas en Firebase
![image](https://github.com/user-attachments/assets/ab9f85ef-3c21-4dc6-be77-576f3791a285)



En esta versión de la app integramos un foro para que nuestros usuarios puedan interactuar y hacer preguntas a la comunidad de estudiantes

![image](https://github.com/user-attachments/assets/35027915-a0aa-4685-b83b-9b66bb53549f)


Adicionalmente, implementamos un chatbot que usa un LLM para interactuar con el usuario y asi, simular conversaciones sobre temas de ingenieria, asi como tambien, es util para resolver dudas de "Writing"
![image](https://github.com/user-attachments/assets/16e4fc6c-f796-4ad4-b9c8-c5ffb42b4b8d)




Pensando en Ingenierias como la ingenieria civil, agregamos un mapa usando "Openstreetmap" que muestra a detalle la ubicación de sitios icono relacionados a la construcción e ingenieria
![image](https://github.com/user-attachments/assets/b76e39bd-cc30-47d6-aa35-48fe1f98b5d2)




________________________________________
![image](https://github.com/user-attachments/assets/2135cf55-21eb-44d4-802d-fe2c6e87fd31)
 
📌 Descripción
La aplicación “Engineering English App” está pensada para ser un método de estudio y repaso del idioma inglés con un enfoque al vocabulario técnico que usan y requieren las distintas ingenierías, de tal forma, esta app permite a los usuarios configurar su perfil y preferencias, incluyendo la carga y eliminación de una foto de perfil, administración de una cuenta personal con nombre de usuario, profesión, idioma nativo, tema (oscuro o claro) y activación o desactivación de notificaciones.

🚀 Funcionalidades
•	Carga y eliminación de foto de perfil (con persistencia en LiveData)

•	Modificación de datos personales: nombre de usuario, profesión e idioma nativo aplicable a todo el UI

•	Cambio de tema: modo claro u oscuro (persistente en el UI)

•	Activación/desactivación de notificaciones

•	Persistencia de datos 

![image](https://github.com/user-attachments/assets/004dc937-a965-44e7-a248-4240819296e9)


🛠️ Tecnologías Utilizadas
•	Kotlin (para la lógica de la aplicación)

•	Jetpack Compose (para la UI)

•	Coil (para carga de imágenes)

• Firebase (para el login de la aplicación)

📜 Explicación de Archivos Claves

ThemeViewModel.kt
Gestiona los datos del usuario y la persistencia.
•	loadUserData(): Carga los datos guardados.

•	updateProfilePicture(uri: String?): Guarda la foto de perfil.

•	saveToDataStore(key: String, value: String): Guarda datos con LiveData.

![image](https://github.com/user-attachments/assets/2c803678-8922-4c31-b58d-905fc89b7b18)
 
SettingsScreen.kt
Pantalla donde el usuario puede cambiar su configuración.

•	Muestra y permite cambiar la foto de perfil usando AsyncImage (Coil).

•	Botón para seleccionar una imagen desde la galería.

•	Botón para eliminar la foto de perfil.

•	Formularios para editar datos personales.

•	Switch para cambiar entre modo oscuro y claro.

•	Switch para activar o desactivar notificaciones.
 
🛠️ Futuras Mejoras

•	Agregar validaciones en los formularios.

•	Mejorar la experiencia de usuario con animaciones.

 ![image](https://github.com/user-attachments/assets/7359a919-e68e-44fe-afe0-fd1f31441bb2)

🙌 Instalación y Uso

Clona este repositorio:

git clone https://github.com/JDNG111/Corte3_app.git

Abre el proyecto en Android Studio.

Conéctalo a un emulador o dispositivo físico.

Ejecuta la app, crea tu cuenta y personaliza tu perfil. 🎨

________________________________________
Hecho por:
Julian David Navarro G.

Programación movil
