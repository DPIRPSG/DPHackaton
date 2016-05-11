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
<form:form action="barter/user/create.do" modelAttribute="barterForm">
	<!-- Hidden Attributes -->
	
	<!-- Editable Attributes -->
	<acme:textbox code="barter.title" path="title"/>
	<br/>
	<spring:message code="barter.offered" />
	
	<acme:textbox code="item.name" path="offeredName"/>
	<acme:textarea code="item.description" path="offeredDescription"/>
	<acme:textarea code="item.pictures" path="offeredPictures"/>
	<br/>
	<spring:message code="barter.requested" />
	
	<acme:textbox code="item.name" path="requestedName"/>
	<acme:textarea code="item.description" path="requestedDescription"/>
	<acme:textarea code="item.pictures" path="requestedPictures"/>
	
	<!-- Action buttons -->
	<acme:submit name="save" code="barter.save"/>
	
	<acme:cancel code="barter.cancel" url="barter/user/list.do"/>
	
</form:form>