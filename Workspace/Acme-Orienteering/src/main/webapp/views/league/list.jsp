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
	name="leagues" requestURI="${requestURI}" id="row_League">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<div>
				<b><a
					href="league/administrator/edit.do?leagueId=${row_League.id}">
						<spring:message code="league.edit" />
				</a></b>
			</div>
		</display:column>
	</security:authorize>

	<!-- Attributes -->

	<spring:message code="league.name" var="nameHeader"/>
	<acme:displayColumn value="${row_League.name }" title="${nameHeader}"/>
	
	<spring:message code="league.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_League.description }" title="${descriptionHeader}"/>

	<spring:message code="league.pictures" var="pictureHeader"/>
	<display:column title="${pictureHeader}"
		sortable="false" >
		<jstl:forEach items="${row_League.pictures}" var="picture">
			<img src="${picture}" style="width:204px;"/>
		</jstl:forEach>
	</display:column>
	
	<spring:message code="league.creationMoment" var="creationMomentHeader" />
	<display:column title="${creationMomentHeader}" sortable="true">
		<fmt:formatDate value="${row_League.creationMoment}" pattern="dd-MM-yyyy"/>
	</display:column>
	
	<spring:message code="league.startedMoment" var="startedMomentHeader" />
	<display:column title="${startedMomentHeader}" sortable="true">
		<fmt:formatDate value="${row_League.startedMoment}" pattern="dd-MM-yyyy"/>
	</display:column>
	
	<spring:message code="league.amount" var="amountHeader"/>
	<acme:displayColumn value="${row_League.amount}" title="${amountHeader}" sorteable="true"/>
	
	<spring:message code="league.referee" var="refereeHeader"/>
	<display:column title="${refereeHeader }" sortable="true">
			<a href="actor/list.do?actorId=${row_League.referee.id}"> <jstl:out
					value="${row_League.referee.name} ${row_League.referee.surname} (${row_League.referee.userAccount.username})" />
			</a>
		</display:column>

	<spring:message code="league.classification" var="classificationHeader" />
	<display:column title="${classificationHeader}" sortable="false">
		<acme:link href="club/list.do?leagueId=${row_League.id}&classification=true" code="league.club.view"/>
	</display:column>

	<spring:message code="league.racing" var="racingHeader" />
	<display:column title="${racingHeader}" sortable="false">
		<acme:link href="race/list.do?leagueId=${row_League.id}" code="league.racing.view"/>
	</display:column>
	
	<spring:message code="league.clubs" var="clubesHeader" />
	<display:column title="${clubesHeader}" sortable="false">
		<acme:link href="club/list.do?leagueId=${row_League.id}" code="league.club.view"/>
	</display:column>
	
	<spring:message code="league.financess" var="financesHeader" />
	<display:column title="${financesHeader}" sortable="false">
		<acme:link href="finances/list.do?leagueId=${row_League.id}" code="league.finances.view"/>
	</display:column>

	<display:column>
		<a href="comment/list.do?commentedEntityId=${row_League.id}"> <spring:message
				code="league.comments" />
		</a>
	</display:column>
	
</display:table>


<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<b><a href="league/administrator/create.do"> <spring:message
					code="league.create" />
		</a></b>
	</div>
</security:authorize>

