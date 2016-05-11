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
<form:form action="attribute/administrator/edit.do" modelAttribute="attribute">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="attributesDescription"/>
	
	<acme:textarea code="attribute.name" path="name" />
	<br />
	
	<!-- Action buttons -->
	<acme:submit name="save" code="attribute.save"/>
	&nbsp;
	<jstl:if test="${attribute.id != 0}">
		<acme:submit_confirm name="delete" code="attribute.delete" codeConfirm="attribute.confirm.delete" />
		&nbsp;
	</jstl:if>
	<acme:cancel url="attribute/administrator/list.do" code="attribute.cancel"/>
	<br />

</form:form>