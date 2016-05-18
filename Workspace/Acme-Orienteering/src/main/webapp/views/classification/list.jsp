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


<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="classifications" requestURI="${requestURI}" id="row_Classification">
	<!-- Action links -->

<%-- 	<security:authorize access="hasRole('REFEREE')"> --%>
<%-- 		<display:column> --%>
<!-- 			<div> -->
<%-- 				<b><acme:link href="participates/referee/edit.do?participatesId=${row_Classification.id}" --%>
<%-- 				 code="classification.edit"/></b> --%>
<!-- 			</div> -->
<%-- 		</display:column> --%>
<%-- 	</security:authorize> --%>

	<!-- Attributes -->
	
	<spring:message code="classification.position" var="positionHeader"/>
	<acme:displayColumn value="${row_Classification.position}" title="${positionHeader}" />
	
	<spring:message code="classification.points" var="pointsHeader"/>
	<acme:displayColumn value="${row_Classification.points}" title="${pointsHeader}" />
	
	<spring:message code="classification.club" var="clubHeader" />
	<display:column title="${clubHeader}" sortable="false">
<%-- 		<a href="${requestURI}?clubId=${row_Classification.club.id}"> --%>
			<jstl:out value="${row_Classification.club.name}"/>
<!-- 		</a> -->
	</display:column>
	
	<spring:message code="classification.race" var="raceHeader" />
	<display:column title="${raceHeader}" sortable="false">
<%-- 		<a href="${requestURI}?raceId=${row_Classification.race.id}"> --%>
			<jstl:out value="${row_Classification.race.name}"/>
<!-- 		</a> -->
	</display:column>
	
</display:table>


<!-- Action links -->

