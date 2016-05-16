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
<form:form action="sponsor/administrator/edit.do" modelAttribute="sponsor">
	<!-- Hidden Attributes -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="finances"/>

	<!-- Editable Attributes -->
	
	<acme:textbox code="sponsor.name" path="name"/>
	
	<acme:textarea code="sponsor.description" path="description"/>
	
	<acme:textbox code="sponsor.logo" path="logo"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="sponsor.save"/>
	
	<jstl:if test="${sponsor.id != 0}">
		<acme:submit name="delete" code="sponsor.delete"/>
	</jstl:if>
	
	<acme:cancel code="sponsor.cancel" url="sponsor/list.do"/>
	
</form:form>
