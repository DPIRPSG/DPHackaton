<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="hasRole('USER')">

	<!-- Form -->
	<form:form action="complaint/user/create.do" modelAttribute="complaint">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="user"/>
		<form:hidden path="barter"/>
		<form:hidden path="match"/>
		
		<!-- Editable Attributes -->
		
		<acme:textbox code="complaint.text" path="text"/>
				
		<!-- Action buttons -->
		<acme:submit name="save" code="complaint.create.save"/>
		&nbsp;
		<acme:cancel code="complaint.create.cancel" url="/complaint/list.do?barterOrMatchId=${barterOrMatchId}"/>
		
	</form:form>

</security:authorize>