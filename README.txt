A continuación se detallan lo que hemos considerado aspectos importantes a tener en cuenta en nuestra implementación del proyecto y que no están reflejados en otras zonas del mismo:

Respecto a las peticiones de ingreso:

- Si la petición de acceso al club está pendiente consideraremos isMember IS FALSE && isDenied IS FALSE && acceptedMoment IS NULL

- Si la petición de acceso al club está aceptada consideraremos isMember IS TRUE && isDenied IS FALSE && acceptedMoment IS NOT NULL

- Si la petición de acceso al clubb está rechazada consideraremos isMember IS FALSE && is Denied IS TRUE && acceptedMoment IS NULL

- Si la petición de acceso al club está como expulsada consideraremos isMember IS FALSE && isDenied IS FALSE && acceptedMoment IS NOT NULL

=====

Respecto a la gestión de las financiaciones:

En el caso de gestionar las financiaciones de una liga, hemos visto interesante que se puedan editar tras su realización. Hemos tomado esta decisión de diseño con el fin de evitar que se introduzcan valores erróneos sin posibilidad de corregirlo.

=====

Respecto a la clasificación:

En el caso de que se dé una situación en la que varios equipos tengan la misma puntuación dentro de una liga, los clubes se ordenaran siguiendo el orden natural alfabético. Hemos tomado esta decisión basándonos en el sistema de clasificación de otras organizaciones deportivas como puede ser la Liga de Fútbol Profesional. Con esto nos aseguramos una homogeneidad en el sistema de puntos de nuestra organización.


=====

Respecto al despliegue:
- Al desplegar el proyecto puede aparecer el error "java.lang.OutOfMemoryError: PermGen space" lo que provoca:
  + Que el servidor se estanque y no se pueda cargar el .war.
  + Que se muestre un mensaje al montar un .war tras hacer Undeploy de otro: FAIL - Deployed application at context path / but context failed to start.
  + Que se muestre un mensaje al montar un .war tras hacer Undeploy de otro: FAIL - Encountered exception javax.management.RuntimeErrorException: Error invoking method check.
- Se puede poner en otra ruta mientras otra instancia está corriendo sin hacer Undeploy.
- Para solucionar este error se pueden tomar dos opciones:
  + Opción 1: Vaciar cache
    - Hacer Undeploy de todas las aplicaciones de Tomcat (excepto "/manager"). En caso de que no se pueda se reinicia la maquina virtual y se hace Undeploy.
    - Después de hacer Undeploy en todas, es necesario reiniciar la máquina virtual.
    - Una vez reiniciada se puede volver a montar el .war.
  + Opción 2: Aumentar caché
    - Entrar en la configuración de Tomcat desde la bandeja de notificaciones
    - Ir a la pestaña "Java"
    - Al final de Java Options añadir las siguientes líneas:

        -Xms128m
        -Xmx1024m
        -XX:PermSize=64m
        -XX:MaxPermSize=256m

     PermSize refleja la capacidad del PermGen inicial, MaxPermSize refleja la cantidad máxima de PermGen, es decir, cuanto mayor sea, más proyectos vamos a poder cargar y descargar sin necesidad de reiniciar la máquina virtual para vaciar la cache.


