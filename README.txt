A continuaci�n se detallan lo que hemos considerado aspectos importantes a tener en cuenta en nuestra implementaci�n del proyecto y que no est�n reflejados en otras zonas del mismo:

Respecto a las peticiones de ingreso:

- Si la petici�n de acceso al club est� pendiente consideraremos isMember IS FALSE && isDenied IS FALSE && acceptedMoment IS NULL

- Si la petici�n de acceso al club est� aceptada consideraremos isMember IS TRUE && isDenied IS FALSE && acceptedMoment IS NOT NULL

- Si la petici�n de acceso al clubb est� rechazada consideraremos isMember IS FALSE && is Denied IS TRUE && acceptedMoment IS NULL

- Si la petici�n de acceso al club est� como expulsada consideraremos isMember IS FALSE && isDenied IS FALSE && acceptedMoment IS NOT NULL

=====

Respecto a la gesti�n de las financiaciones:

En el caso de gestionar las financiaciones de una liga, hemos visto interesante que se puedan editar tras su realizaci�n. Hemos tomado esta decisi�n de dise�o con el fin de evitar que se introduzcan valores err�neos sin posibilidad de corregirlo.

=====

Respecto a la clasificaci�n:

En el caso de que se d� una situaci�n en la que varios equipos tengan la misma puntuaci�n dentro de una liga, los clubes se ordenaran siguiendo el orden natural alfab�tico. Hemos tomado esta decisi�n bas�ndonos en el sistema de clasificaci�n de otras organizaciones deportivas como puede ser la Liga de F�tbol Profesional. Con esto nos aseguramos una homogeneidad en el sistema de puntos de nuestra organizaci�n.


=====

Respecto al despliegue:
- Al desplegar el proyecto puede aparecer el error "java.lang.OutOfMemoryError: PermGen space" lo que provoca:
  + Que el servidor se estanque y no se pueda cargar el .war.
  + Que se muestre un mensaje al montar un .war tras hacer Undeploy de otro: FAIL - Deployed application at context path / but context failed to start.
  + Que se muestre un mensaje al montar un .war tras hacer Undeploy de otro: FAIL - Encountered exception javax.management.RuntimeErrorException: Error invoking method check.
- Se puede poner en otra ruta mientras otra instancia est� corriendo sin hacer Undeploy.
- Para solucionar este error se pueden tomar dos opciones:
  + Opci�n 1: Vaciar cache
    - Hacer Undeploy de todas las aplicaciones de Tomcat (excepto "/manager"). En caso de que no se pueda se reinicia la maquina virtual y se hace Undeploy.
    - Despu�s de hacer Undeploy en todas, es necesario reiniciar la m�quina virtual.
    - Una vez reiniciada se puede volver a montar el .war.
  + Opci�n 2: Aumentar cach�
    - Entrar en la configuraci�n de Tomcat desde la bandeja de notificaciones
    - Ir a la pesta�a "Java"
    - Al final de Java Options a�adir las siguientes l�neas:

        -Xms128m
        -Xmx1024m
        -XX:PermSize=64m
        -XX:MaxPermSize=256m

     PermSize refleja la capacidad del PermGen inicial, MaxPermSize refleja la cantidad m�xima de PermGen, es decir, cuanto mayor sea, m�s proyectos vamos a poder cargar y descargar sin necesidad de reiniciar la m�quina virtual para vaciar la cache.


