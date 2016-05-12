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
<form:form action="barter/administrator/edit.do" modelAttribute="category">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<!-- Editable Attributes -->
	
	<acme:textbox code="category.name" path="name"/>
	
	<acme:textarea code="category.description" path="description"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="category.save"/>
	
	<jstl:if test="${category.id != 0}">
		<acme:submit name="delete" code="category.delete"/>
	</jstl:if>
	
	<acme:cancel code="category.cancel" url="category/administrator/list.do"/>
	
</form:form>