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
<form:form action="club/manager/delete.do" modelAttribute="deleteClubForm">
	<!-- Hidden Attributes -->
	<form:hidden path="clubId"/>

	<!-- Editable Attributes -->
	
	<form:label path="managerId">
			<spring:message code="club.manager" />
		</form:label>
	<form:select name="manager" path="managerId">
		<form:option value="0" label="----" />
		    <jstl:forEach var="manager" items="${managers}" >
		        <form:option value="${manager.id}"><jstl:out value="${manager.name} ${manager.surname}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="managerId" cssClass="error" />
		
	<br/>
	<!-- Action buttons -->
	
	<jstl:if test="${deleteClubForm.clubId != 0}">
		<acme:submit name="delete" code="club.delete"/>
	</jstl:if>
	
	<acme:cancel code="club.cancel" url="club/manager/list.do"/>
	
</form:form>