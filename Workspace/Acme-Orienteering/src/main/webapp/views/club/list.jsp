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
	name="clubes" requestURI="${requestURI}" id="row_Club">
	<!-- Action links -->

	<security:authorize access="hasRole('MANAGER')">
		<jstl:if test="${manager.id == row_Club.manager.id }">
			<display:column>
				<div>
					<b><a href="club/manager/edit.do?clubId=${row_Club.id}"> <spring:message
								code="club.edit" />
					</a></b>
				</div>
			</display:column>
		</jstl:if>
	</security:authorize>

	<!-- Attributes -->
	<display:column>
		<jstl:forEach var="possition" items="${ranking }">
			<jstl:if test="${possition[0] == row_Club.id}">
				<jstl:out value="${possition[1]}" />
			</jstl:if>
		</jstl:forEach>
	</display:column>

	<spring:message code="club.name" var="nameHeader"/>
	<acme:displayColumn value="${row_Club.name }" title="${nameHeader}"/>
	
	<spring:message code="club.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_Club.description }" title="${descriptionHeader}"/>
	
	<spring:message code="club.pictures" var="pictureHeader"/>
	<display:column title="${pictureHeader}"
		sortable="false" >
		<jstl:forEach items="${row_Club.pictures}" var="picture">
			<img src="${picture}" style="width:204px;"/>
		</jstl:forEach>
	</display:column>
	
	<spring:message code="club.creationMoment" var="creationMomentHeader" />
	<display:column title="${creationMomentHeader}" sortable="true">
		<fmt:formatDate value="${row_Club.creationMoment}" pattern="dd-MM-yyyy"/>
	</display:column>
	
	<spring:message code="club.manager" var="managerHeader"/>
	<acme:displayColumn value="${row_Club.manager.name} ${row_Club.manager.surname }" title="${managerHeader}"/>

	<spring:message code="club.runners" var="runnersHeader" />
	<display:column title="${runnersHeader}" sortable="false">
		<a href="runner/list.do?clubId=${row_Club.id}"> <spring:message
				code="club.runners"/>
		</a>
	</display:column>
	
	<spring:message code="club.leagues" var="leaguesHeader" />
	<display:column title="${leaguesHeader}" sortable="false">
		<a href="league/list.do?clubId=${row_Club.id}"> <spring:message
				code="club.leagues"/>
		</a>
	</display:column>
	
	<spring:message code="club.racing" var="racingHeader" />
	<display:column title="${racingHeader}" sortable="false">
		<a href="race/list.do?clubId=${row_Club.id}"> <spring:message
				code="club.racing"/>
		</a>
	</display:column>
	
	<spring:message code="club.punishments" var="punishmentsHeader" />
	<display:column title="${punishmentsHeader}" sortable="false">
		<a href="punishment/list.do?clubId=${row_Club.id}"> <spring:message
				code="club.punishments"/>
		</a>
	</display:column>

	<jstl:if test="${manager.id == row_Club.manager.id || pertenece}">
		<spring:message code="club.bulletins" var="bulletinsHeader" />
		<display:column title="${bulletinsHeader}" sortable="false">
			<a href="${requestURI2}?clubId=${row_Club.id}"> <spring:message
					code="club.bulletins" />
			</a>
		</display:column>
	</jstl:if>

	<display:column>
		<a href="comment/list.do?commentedEntityId=${row_Club.id}"> <spring:message
				code="club.comments" />
		</a>
	</display:column>

</display:table>


<!-- Action links -->

<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${clubes == null }">
		<div>
			<b><a href="club/manager/create.do"> <spring:message
						code="club.create" />
			</a></b>
		</div>
	</jstl:if>
</security:authorize>
