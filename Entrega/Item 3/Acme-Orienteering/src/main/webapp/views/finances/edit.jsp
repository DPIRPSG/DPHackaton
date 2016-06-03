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
<form:form action="finances/administrator/edit.do" modelAttribute="finances">
	<!-- Hidden Attributes -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<!-- Editable Attributes -->
	
	<acme:select items="${sponsors}" itemLabel="name" code="finances.sponsor" path="sponsor"/>
	<acme:select items="${leagues}" itemLabel="name" code="finances.league" path="league"/>
	
	<acme:textbox code="finances.amount" path="amount"/>
	<acme:textbox code="finances.paymentMoment" path="paymentMoment"/>
	
	<!-- Action buttons -->
	<acme:submit name="save" code="finances.save"/>
	
	<jstl:if test="${finances.id != 0}">
		<acme:submit name="delete" code="finances.delete"/>
	</jstl:if>
	
	<acme:cancel code="finances.cancel" url="finances/list.do"/>
	
</form:form>
