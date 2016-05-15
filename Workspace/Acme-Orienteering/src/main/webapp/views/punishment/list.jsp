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
	name="punishments" requestURI="${requestURI}" id="row_Punishment">
	<!-- Action links -->

	<!-- Attributes -->

	<spring:message code="punishment.reason" var="reasonHeader"/>
	<acme:displayColumn value="${row_Punishment.reason }" title="${reasonHeader}"/>
	
	<spring:message code="punishment.points" var="pointsHeader"/>
	<acme:displayColumn value="${row_Punishment.points }" title="${pointsHeader}"/>
	
	<spring:message code="punishment.club" var="clubHeader"/>
	<acme:displayColumn value="${row_Punishment.club.name }" title="${clubHeader}"/>
	
	<spring:message code="punishment.league" var="leagueHeader"/>
	<acme:displayColumn value="${row_Punishment.league.name }" title="${leagueHeader}"/>

</display:table>


<!-- Action links -->

