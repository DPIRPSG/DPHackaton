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
	name="entereds" requestURI="${requestURI}" id="row_Entered">
	<!-- Action links -->


	<!-- Attributes -->

	<security:authorize access="hasRole('RUNNER')">
	<spring:message code="entered.club" var="clubHeader"/>
	<acme:displayColumn value="${row_Entered.club.name}" title="${clubHeader}"/>
	</security:authorize>
	
	<security:authorize access="hasRole('MANAGER')">
	<spring:message code="entered.runner" var="runnerHeader"/>
	<acme:displayColumn value="${row_Entered.runner.name}" title="${runnerHeader}"/>
	</security:authorize>
	
	<spring:message code="entered.report" var="reportHeader"/>
	<acme:displayColumn value="${row_Entered.report}" title="${reportHeader}"/>
	
	<spring:message code="entered.isMember" var="isMemberHeader"/>
	<acme:displayColumn value="${row_Entered.isMember}" title="${isMemberHeader}"/>
	
	<spring:message code="entered.registerMoment" var="registerMomentHeader"/>
	<acme:displayColumn value="${row_Entered.registerMoment}" title="${registerMomentHeader}"/>
	
	<spring:message code="entered.acceptedMoment" var="acceptedMomentHeader"/>
	<acme:displayColumn value="${row_Entered.acceptedMoment}" title="${acceptedMomentHeader}"/>

</display:table>


<!-- Action links -->


