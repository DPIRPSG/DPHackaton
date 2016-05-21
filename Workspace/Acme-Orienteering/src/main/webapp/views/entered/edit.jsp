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
<form:form action="entered/manager/edit.do" modelAttribute="entered">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:hidden path="isMember" />
	<form:hidden path="isDenied" />
	<form:hidden path="registerMoment" />
	<form:hidden path="acceptedMoment" />
	<form:hidden path="runner" />
	<form:hidden path="club" />

	<!-- Editable Attributes -->
	<acme:textarea code="entered.report" path="report"/>
	
	<acme:submit name="save" code="entered.save"/>
	<acme:cancel code="entered.cancel" url="entered/runner/list.do"/>
	
</form:form>