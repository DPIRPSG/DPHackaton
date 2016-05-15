A continuación se detallan lo que hemos considerado aspectos importantes a tener en cuenta en nuestra implementación del proyecto y que no están reflejados en otras zonas del mismo:

Respecto a las peticiones de ingreso:
- Si isMember IS TRUE && acceptedMoment IS NOT NULL —> Aceptado
- Si isMember IS FALSE && acceptedMoment IS NULL —> Pendiente
- Si isMember IS FALSE && acceptedMoment IS NOT NULL —> Expusado del club
- Si se borra el Entered antes de pasar de Pendiente a Aceptado —> Rechazado

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


