<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<!-- Listing grid -->
	<div>
	<table>

	<acme:display code="auditor.name" value="${auditor.name}"/>
	<acme:display code="auditor.surname" value="${auditor.surname}"/>
	<acme:display code="auditor.phone" value="${auditor.phone}"/>
	<acme:display code="auditor.username" value="${auditor.userAccount.username}"/>
	
	</table>
	</div>

	
	<!-- Action links -->
	<div>
		<b><a href="auditor/auditor/edit.do"> 
			<spring:message code="auditor.edit" />
		</a></b>
	</div>
	<br/>
