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
	name="barters" requestURI="${requestURI}" id="row_Barter">
	<!-- Action links -->

	<!-- Attributes -->

	<spring:message code="barter.novedades" var="novedades" />
	<display:column title="${novedades}"
		 sortable="false">
		<a href="socialIdentity/list.do?userId=${row_Barter.user.id}">
			<jstl:out value="${row_Barter.user.name} ${row_Barter.user.surname} (${row_Barter.user.userAccount.username})"/>
		</a>
		<br/>
		<spring:message code="barter.intro"/>
		"<jstl:out value="${row_Barter.title}"/>"
		<br/>
		<spring:message code="barter.pide"/>
		<a href="item/display.do?itemId=${row_Barter.requested.id}"> <jstl:out
				value="${row_Barter.requested.name}" />
		</a>
		<spring:message code="barter.cambio"/>
		<a href="item/display.do?itemId=${row_Barter.offered.id}"> <jstl:out
				value="${row_Barter.offered.name}" />
		</a>
		<br/>
		<spring:message code="barter.date"/>
		<jstl:out value="${row_Barter.registerMoment}"/>
	</display:column>

</display:table>

<br/>
<br/>

<!-- Action links -->
