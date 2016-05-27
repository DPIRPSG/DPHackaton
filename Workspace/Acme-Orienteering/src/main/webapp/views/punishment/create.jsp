<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access = "hasRole('REFEREE')">
	<!-- Form -->
	<form:form action="punishment/referee/create.do" modelAttribute="punishment">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:hidden path="club"/>
		
		<!-- Editable Attributes -->
		<h3><spring:message code="punishment.concerningClub" /> "<jstl:out value="${punishment.club.name}"/>"</h3>
		
		<acme:textbox code="punishment.reason" path="reason" />
<%-- 		<acme:textbox code="punishment.points" path="points" /> --%>
		<form:label path="points">
			<spring:message code="punishment.points" />: &nbsp;
		</form:label>	
		<form:input path="points" readonly="false"/>
		<br>
		
		<form:label path="league">
			<spring:message code="punishment.league" />:
		</form:label>
		<form:select name="league" path="league">
<!-- 		    <option value="">---</option> -->
		    <jstl:forEach var="league" items="${leagues}" >
		        <form:option value="${league.id}"><jstl:out value="${league.name}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="league" cssClass="error" />
		<br>
		<br/>
		
		<!-- Action buttons -->
		<acme:submit name="save" code="punishment.save"/>
		<input type="button" name="cancel"
			value="<spring:message code="punishment.cancel" />"
			onclick="javascript: relativeRedir('club/list.do');" />
		<br />
		
	</form:form>
</security:authorize>
