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
<form:form action="${urlAction}" modelAttribute="match">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="legalText" />
	<form:hidden path="auditor" />
	<form:hidden path="creationMoment" />
	<form:hidden path="offerSignsDate" />
	<form:hidden path="requestSignsDate" />
	<form:hidden path="cancelled" />
	<form:hidden path="creatorBarter" />
	<form:hidden path="receiverBarter" />
	
	<acme:textarea code="match.report" path="report"/>
	<br />
	
	<!-- Action buttons -->
	<acme:submit name="save" code="match.save"/>
	&nbsp;
	<acme:cancel url="match/auditor/list-assigned.do" code="match.cancel"/>
	<br />

</form:form>