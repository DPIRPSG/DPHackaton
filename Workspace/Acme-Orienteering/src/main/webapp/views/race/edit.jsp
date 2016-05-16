<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Form -->
<form:form action="race/administrator/edit.do" modelAttribute="race">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:hidden path="classifications"/>
	<form:hidden path="participates"/>
	<form:hidden path="comments"/>
	
	<!-- Editable Attributes -->
	
	<acme:textbox code="race.name" path="name"/>
	
	<acme:textarea code="race.description" path="description"/>
		
	<acme:textbox code="race.moment" path="moment"/>
		
	<acme:select items="${categories}" itemLabel="name" code="race.category" path="category"/>
	
	<acme:select items="${leagues}" itemLabel="name" code="race.league" path="league"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="race.save"/>
	
	<jstl:if test="${race.id != 0}">
		<acme:submit name="delete" code="race.delete"/>
	</jstl:if>
	
	<acme:cancel code="race.cancel" url="race/administrator/list.do"/>
	
</form:form>