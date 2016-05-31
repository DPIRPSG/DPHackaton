<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="bulletin.club"/>: <jstl:out value="${club.name}" /></h3>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="bulletins" requestURI="${requestURI}" id="row_Bulletin">
	<!-- Action links -->

	<security:authorize access="hasRole('MANAGER')">
		<display:column>
		<a href="bulletin/gerente/delete.do?bulletinId=${row_Bulletin.id}" onclick="return confirm('<spring:message code="bulletin.confirm.delete" />')" > <spring:message
				code="bulletin.delete" />
		</a>
		</display:column>
	</security:authorize>

	<!-- Attributes -->
	
	<spring:message code="bulletin.actor" var="actorHeader" />
	<acme:displayColumn title="${actorHeader}" sorteable="true" value="${row_Bulletin.actor.name} ${row_Bulletin.actor.surname} (${row_Bulletin.actor.userAccount.username})"/>
	
	<spring:message code="bulletin.title" var="titleHeader" />
	<display:column title="${titleHeader}"
		sortable="true">
		<jstl:out value="${row_Bulletin.title}"/>
	</display:column>

	<spring:message code="bulletin.description" var="descriptionHeader" />
	<display:column title="${descriptionHeader}"
		sortable="true">
		<jstl:out value="${row_Bulletin.description}"/>
	</display:column>

	<spring:message code="bulletin.creationMoment" var="creationMomentHeader" />
	<display:column title="${creationMomentHeader}" sortable="true">
		<fmt:formatDate value="${row_Bulletin.creationMoment}" pattern="dd-MM-yyyy hh:mm"/>
	</display:column>

</display:table>

<br/>
<br/>

<!-- Action links -->

<jstl:if test="${pertenece}">
	<div>
		<a href="${requestURI2}?clubId=${club.id}"> <spring:message
				code="bulletin.create" />
		</a>
	</div>
</jstl:if>

<!-- Alert -->
	
