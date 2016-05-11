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
<form:form action="barter/administrator/edit.do" modelAttribute="barter">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:hidden path="title"/>
	<form:hidden path="cancelled"/>
	<form:hidden path="closed"/>
	<form:hidden path="registerMoment"/>
	
	<form:hidden path="user"/>
	<form:hidden path="offered"/>
	<form:hidden path="requested"/>
	<form:hidden path="createdMatch"/>
	<form:hidden path="receivedMatch"/>
	
	<!-- Editable Attributes -->
	<acme:selectMult items="${allBarters}" itemLabel="title" code="barter.relatedBarter" path="relatedBarter"/>

	<!-- Action buttons -->
	<acme:submit name="save" code="barter.save"/>
	
	<acme:cancel code="barter.cancel" url="barter/administrator/list.do"/>
	
</form:form>