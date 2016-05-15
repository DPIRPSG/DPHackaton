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
<form:form action="league/administrator/edit.do" modelAttribute="leagueForm">
	<!-- Hidden Attributes -->
	<form:hidden path="leagueId" />
	
	<!-- Editable Attributes -->
	
	<acme:textbox code="league.name" path="name"/>
	
	<acme:textarea code="league.description" path="description"/>
	
	<acme:textarea code="league.pictures" path="pictures"/>
	
	<acme:textbox code="league.startedMoment" path="startedMoment"/>
	
	<acme:textbox code="league.amount" path="amount"/>
	
	<form:label path="referee">
			<spring:message code="league.referee" />
		</form:label>
	<form:select name="referee" path="referee">
		    <jstl:forEach var="referee" items="${referees}" >
		        <form:option value="${referee.id}"><jstl:out value="${referee.name} ${referee.surname}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="referee" cssClass="error" />

	<!-- Action buttons -->
	<acme:submit name="save" code="league.save"/>
	
	<jstl:if test="${leagueId != 0}">
		<acme:submit name="delete" code="league.delete"/>
	</jstl:if>
	
	<acme:cancel code="league.cancel" url="league/administrator/list.do"/>
	
</form:form>