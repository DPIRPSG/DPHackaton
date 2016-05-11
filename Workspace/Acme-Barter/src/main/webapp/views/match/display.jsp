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
	name="matches" requestURI="${requestURI}" id="row_Match">
	<!-- Action links -->

	<!-- Attributes -->

	<spring:message code="match.novedades" var="novedades" />
	<display:column title="${novedades}"
		 sortable="false">
		<a href="socialIdentity/list.do?userId=${row_Match.creatorBarter.user.id}">
			<jstl:out value="${row_Match.creatorBarter.user.name} ${row_Match.creatorBarter.user.surname} (${row_Match.creatorBarter.user.userAccount.username})"/>
		</a>
		<spring:message code="match.acuerdo"/>
		<a href="socialIdentity/list.do?userId=${row_Match.receiverBarter.user.id}">
			<jstl:out value="${row_Match.receiverBarter.user.name} ${row_Match.receiverBarter.user.surname} (${row_Match.receiverBarter.user.userAccount.username})"/>
		</a>
		<br/>
		<spring:message code="match.barters"/>
		"<jstl:out value="${row_Match.creatorBarter.title}"/>"
		<br/>
		<spring:message code="match.items"/>
		<a href="item/display.do?itemId=${row_Match.creatorBarter.offered.id}"> <jstl:out value="${row_Match.creatorBarter.offered.name}" /></a>
		<spring:message code="match.and"/>
		<a href="item/display.do?itemId=${row_Match.creatorBarter.requested.id}"> <jstl:out value="${row_Match.creatorBarter.requested.name}" /></a>
		<br/>
		<spring:message code="match.and"/>
		"<jstl:out value="${row_Match.receiverBarter.title}"/>"
		<br/>
		<spring:message code="match.items"/>
		<a href="item/display.do?itemId=${row_Match.receiverBarter.offered.id}"> <jstl:out value="${row_Match.receiverBarter.offered.name}" /></a>
		<spring:message code="match.and"/>
		<a href="item/display.do?itemId=${row_Match.receiverBarter.requested.id}"> <jstl:out value="${row_Match.receiverBarter.requested.name}" /></a>
		<br/>
		<jstl:if test="${(row_Match.report != null) && (row_Match.report != '')}">
			<spring:message code="match.report"/>: &nbsp<jstl:out value="${row_Match.report}" />
			<br/>
		</jstl:if>
		<spring:message code="match.date"/>
		<jstl:out value="${row_Match.creationMoment}"/>
	</display:column>

</display:table>

<br/>
<br/>

<!-- Action links -->
