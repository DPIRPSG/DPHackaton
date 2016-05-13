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
<form:form action="club/manager/edit.do" modelAttribute="club">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:hidden path="crationMoment"/>
	<form:hidden path="deleted"/>
	
	<form:hidden path="manager"/>
	<form:hidden path="bulletins"/>
	<form:hidden path="classifications"/>
	<form:hidden path="entered"/>
	<form:hidden path="punishments"/>
	<form:hidden path="feePayments"/>
	
	<form:hidden path="comments"/>
	
	<!-- Editable Attributes -->
	
	<acme:textbox code="club.name" path="name"/>
	
	<acme:textarea code="club.description" path="description"/>
	
	<acme:textarea code="club.pictures" path="pictures"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="club.save"/>
	
	<jstl:if test="${club.id != 0}">
		<acme:submit name="delete" code="club.delete"/>
	</jstl:if>
	
	<acme:cancel code="club.cancel" url="club/manager/list.do"/>
	
</form:form>