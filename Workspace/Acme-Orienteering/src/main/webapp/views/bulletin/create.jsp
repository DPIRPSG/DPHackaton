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
<form:form action="bulletin/manager/create.do" modelAttribute="bulletin">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="creationMoment"/>
	
	<form:hidden path="club"/>
	
	<!-- Editable Attributes -->
	

	<acme:textbox code="bulletin.title" path="title" />

	<acme:textarea code="bulletin.description" path="description" />
		
	<!-- Action buttons -->

	
	<acme:submit name="save" code="bulletin.save"/>
	
	<acme:cancel code="bulletin.cancel" url="bulletin/manager/list.do?clubId=${bulletin.club.id}"/>

</form:form>