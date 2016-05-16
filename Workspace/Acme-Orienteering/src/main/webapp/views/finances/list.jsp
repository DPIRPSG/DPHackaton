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
	name="financess" requestURI="${requestURI}" id="row_Finances">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<div>
				<b><acme:link href="finances/administrator/edit.do?financesId=${row_Finances.id}" code="finances.edit"/></b>
			</div>
		</display:column>
	</security:authorize>

	<!-- Attributes -->
	
	<spring:message code="finances.sponsor" var="sponsorHeader"/>
	<display:column title="${sponsorHeader}">
		<img src="${row_Finances.sponsor.logo}" style="height:40px;"/> &nbsp;
		<a href="finances/list.do?sponsorId=${row_Finances.sponsor.id}">
			<jstl:out value="${row_Finances.sponsor.name}"/>
		</a>
	</display:column>
	
	<spring:message code="finances.league" var="leagueHeader"/>
	<display:column title="${leagueHeader}">
		<a href="finances/list.do?leagueId=${row_Finances.league.id}">
			<jstl:out value="${row_Finances.league.name}"/>
		</a>
	</display:column>

	<spring:message code="finances.amount" var="amountHeader"/>
	<acme:displayColumn value="${row_Finances.amount}" title="${amountHeader}" sorteable="true"/>
	
	<spring:message code="finances.paymentMoment" var="momentHeader" />
	<display:column title="${momentHeader}" sortable="true">
		<fmt:formatDate value="${row_Finances.paymentMoment}" pattern="dd-MM-yyyy"/>
	</display:column>
	
	
</display:table>


<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<b><acme:link href="finances/administrator/create.do?${params}" code="finances.create"/></b>
	</div>
</security:authorize>
