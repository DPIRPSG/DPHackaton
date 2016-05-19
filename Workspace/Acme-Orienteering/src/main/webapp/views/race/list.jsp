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
	name="racing" requestURI="${requestURI}" id="row_Race">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<div>
				<b><a href="race/administrator/edit.do?raceId=${row_Race.id}">
						<spring:message code="race.edit" />
				</a></b>
			</div>
		</display:column>
	</security:authorize>

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

	<spring:message code="race.comments" var="commentsHeader" />
	<display:column title="${commentsHeader }">
		<a href="comment/list.do?commentedEntityId=${row_Race.id}"> <spring:message
				code="race.comments" />
		</a>
	</display:column>
	
	<security:authorize access="hasAnyRole('MANAGER, RUNNER, REFEREE')">
	<spring:message code="race.participates" var="leagueHeader" />
	<display:column title="${leagueHeader}">
		<security:authorize access="hasRole('MANAGER')">
			<acme:link href="participates/manager/list.do" code="race.participates.view"/>
		</security:authorize>
		<security:authorize access="hasRole('RUNNER')">
			<acme:link href="participates/runner/list.do" code="race.participates.view"/>
		</security:authorize>
		<security:authorize access="hasRole('REFEREE')">
			<acme:link href="participates/referee/list.do" code="race.participates.view"/>
		</security:authorize>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('REFEREE')">
	<spring:message code="race.calculateClassification" var="calculateClassificationHeader" />
	<display:column title="${calculateClassificationHeader}">
		<acme:link href="classification/referee/calculateClassification.do?raceId=${row_Race.id}" code="race.calculateClassification.view"/>
	</display:column>
	</security:authorize>

	<security:authorize access="hasRole('RUNNER')">
		<spring:message code="race.join" var="joinHeader" />
		<display:column title="${joinHeader}" sortable="true">
			<jstl:set value="true" var="puedeApuntarse" />
			<jstl:set value="false" var="haPagado" />
			<jstl:forEach items="${row_Race.participates}" var="participate">
				<jstl:if test="${participate.runner.id == runner.id}">
					<jstl:set value="false" var="puedeApuntarse" />
				</jstl:if>
			</jstl:forEach>
			<jstl:if test="${today.time gt row_Race.moment.time}">
				<jstl:set value="false" var="puedeApuntarse" />
			</jstl:if>
			<jstl:forEach items="${row_Race.league.feePayments}" var="fee">
				<jstl:if test="${fee.club.id == club.id}">
					<jstl:set value="true" var="haPagado" />
				</jstl:if>
			</jstl:forEach>
			<jstl:if test="${!haPagado}">
					<jstl:set value="false" var="puedeApuntarse" />
				</jstl:if>
			<jstl:if test="${puedeApuntarse}">
				<acme:link href="participates/runner/join.do?raceId=${row_Race.id}"
					code="race.join" />
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>


<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<b><a href="race/administrator/create.do"> <spring:message
					code="race.create" />
		</a></b>
	</div>
</security:authorize>
