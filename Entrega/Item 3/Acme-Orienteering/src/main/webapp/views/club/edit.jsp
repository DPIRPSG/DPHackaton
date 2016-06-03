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
<form:form action="club/gerente/edit.do" modelAttribute="clubForm">
	<!-- Hidden Attributes -->
	<form:hidden path="clubId"/>

	<!-- Editable Attributes -->
	
	<acme:textbox code="club.name" path="name"/>
	
	<acme:textarea code="club.description" path="description"/>
	
	<acme:textarea code="club.pictures" path="pictures"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="club.save"/>
	
	<acme:cancel code="club.cancel" url="club/gerente/list.do"/>
	
</form:form>