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

<jsp:useBean id="today" class="java.util.Date" />

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="participatess" requestURI="${requestURI}" id="row_Participates">
	<!-- Action links -->

	<security:authorize access="hasRole('REFEREE')">
		<jstl:if test="${today.time gt row_Participates.race.moment.time}">
		<display:column>
			<div>
				<b><acme:link href="participates/referee/edit.do?participatesId=${row_Participates.id}"
				 code="participates.edit"/></b>
			</div>
		</display:column>
		</jstl:if>
		
	</security:authorize>

	<!-- Attributes -->
	<jstl:if test="${today.time gt row_Participates.race.moment.time}">

		<spring:message code="participates.result" var="resultHeader"/>
		<acme:displayColumn value="${row_Participates.result}" title="${resultHeader}" />
	</jstl:if>
	
	
	<spring:message code="participates.runner" var="runnerHeader" />
	<display:column title="${runnerHeader}" sortable="false">
<%-- 		<a href="${requestURI}?runnerId=${row_Participates.runner.id}"> --%>
			<jstl:out value="${row_Participates.runner.name}"/>
<!-- 		</a> -->
	</display:column>
	
	<spring:message code="participates.race" var="raceHeader" />
	<display:column title="${raceHeader}" sortable="false">
<%-- 		<a href="${requestURI}?raceId=${row_Participates.race.id}"> --%>
			<jstl:out value="${row_Participates.race.name}"/>
<!-- 		</a> -->
	</display:column>
	
</display:table>


<!-- Action links -->

