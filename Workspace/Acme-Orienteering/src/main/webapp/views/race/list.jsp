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
	name="racing" requestURI="${requestURI}" id="row_Race">
	<!-- Action links -->
	
	<display:column>
		<div>
			<b><a href="race/administrator/edit.do?raceId=${row_Race.id}"> <spring:message
						code="race.edit" />
			</a></b>
		</div>
	</display:column>

	<!-- Attributes -->

	<spring:message code="race.name" var="nameHeader"/>
	<acme:displayColumn value="${row_Race.name }" title="${nameHeader}"/>
	
	<spring:message code="race.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_Race.description }" title="${descriptionHeader}"/>
	
	<spring:message code="race.moment" var="momentHeader" />
	<display:column title="${momentHeader}" sortable="true">
		<fmt:formatDate value="${row_Race.moment}" pattern="dd-MM-yyyy hh:mm"/>
	</display:column>
	
	<spring:message code="race.category" var="categoryHeader" />
	<display:column title="${categoryHeader}" sortable="true">
		<jstl:out value="${row_Race.category.name}"></jstl:out>
		<br/>
		<jstl:out value="${row_Race.category.description}"></jstl:out>
	</display:column>
	
	<spring:message code="race.league" var="leagueHeader" />
	<display:column title="${leagueHeader}" sortable="true">
		<jstl:out value="${row_Race.league.name}"></jstl:out>
		<br/>
		<jstl:out value="${row_Race.league.description}"></jstl:out>
	</display:column>
	
</display:table>


<!-- Action links -->

<div>
	<b><a href="race/administrator/create.do"> <spring:message
				code="race.create" />
	</a></b>
</div>

