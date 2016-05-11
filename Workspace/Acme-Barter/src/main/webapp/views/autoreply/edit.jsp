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
<form:form action="autoreply/actor/edit.do" modelAttribute="autoreply">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="actor" />
	
	<spring:message code="autoreply.keyWords.set" />
	<acme:textbox code="autoreply.keyWords" path="keyWords"/>
	<acme:textarea code="autoreply.text" path="text"/>
	
	<br />

	<!-- Action buttons -->
	<acme:submit name="save" code="autoreply.save"/>
	&nbsp; 
	<acme:cancel url="autoreply/actor/list.do" code="autoreply.cancel"/>
	&nbsp;
	<acme:submit_confirm name="delete" code="autoreply.delete" codeConfirm="autoreply.delete.confirm"/>
	<br />

</form:form>