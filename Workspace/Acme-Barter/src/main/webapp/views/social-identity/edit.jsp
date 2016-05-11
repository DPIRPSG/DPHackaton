<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<!-- Form -->
	<form:form action="socialIdentity/user/edit.do" modelAttribute="socialIdentity">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="user"/>
		
		
		<!-- Editable Attributes -->
		<acme:textbox code="socialIdentity.nick" path="nick" />
		<acme:textbox code="socialIdentity.name" path="name" />
		<acme:textbox code="socialIdentity.homePage" path="homePage" />
		<acme:textbox code="socialIdentity.picture" path="picture" />
		
		<br />
		
		
		
		<!-- Action buttons -->
		<acme:submit name="save" code="socialIdentity.save"/>
		&nbsp;
		<jstl:if test="${socialIdentity.id != 0}">
			<acme:submit name="delete" code="socialIdentity.delete"/>
			&nbsp;
		</jstl:if>
		<acme:cancel url="socialIdentity/user/list.do" code="socialIdentity.cancel"/>
		<br />
		
	</form:form>
