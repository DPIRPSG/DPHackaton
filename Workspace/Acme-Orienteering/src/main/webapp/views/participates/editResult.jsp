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
<form:form action="participates/referee/edit.do" modelAttribute="participates">
	<!-- Hidden Attributes -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="runner"/>
	<form:hidden path="race"/>

	<!-- Editable Attributes -->
	
	<acme:textbox code="participates.result" path="result"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="participates.save"/>
	
	<jstl:if test="${sponsor.id != 0}">
		<acme:submit name="delete" code="participates.delete"/>
	</jstl:if>
	
	<acme:cancel code="participates.cancel" url="participates/referee/list.do"/>
	
</form:form>
