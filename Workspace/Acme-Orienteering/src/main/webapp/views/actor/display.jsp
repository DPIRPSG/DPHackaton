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

		<acme:display code="actor.name" value="${actor.name}" />
		<acme:display code="actor.surname" value="${actor.surname}" />
		<acme:display code="actor.phone" value="${actor.phone}" />
		<acme:display code="actor.nif" value="${actor.nif}" />
		<acme:display code="actor.username"
			value="${actor.userAccount.username}" />

	</table>
</div>


<!-- Action links -->
	<div>
		<b><acme:link href="actor/actor/edit.do" code="actor.edit"/></b>
	</div>
