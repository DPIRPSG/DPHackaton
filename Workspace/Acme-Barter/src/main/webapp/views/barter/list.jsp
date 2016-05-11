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

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="barter.cancelled" var="cancelledHeader" />
		<display:column title="${cancelledHeader}" sortable="true">
			<jstl:if test="${row_Barter.cancelled == false}">
				<a href="barter/administrator/cancel.do?barterId=${row_Barter.id}"> <spring:message
					code="barter.cancel"/>
				</a>
			</jstl:if>
			<jstl:if test="${row_Barter.cancelled == true}">
				<jstl:out value="${row_Barter.cancelled}" />
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
		<spring:message code="barter.cancel" var="cancelledHeader" />
		<display:column title="${cancelledHeader}" sortable="true">
			<jstl:if test="${row_Barter.cancelled == false && userId == row_Barter.user.id}">
				<a href="barter/user/cancel.do?barterId=${row_Barter.id}"> <spring:message
					code="barter.cancel"/>
				</a>
			</jstl:if>
			<jstl:if test="${row_Barter.cancelled == true}">
				<jstl:out value="${row_Barter.cancelled}" />
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="barter.edit" var="editHeader" />
		<display:column title="${editHeader}" sortable="true">
			<jstl:if test="${row_Barter.cancelled == false}">
				<a href="barter/administrator/edit.do?barterId=${row_Barter.id}"> <spring:message
						code="barter.edit"/>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>

	<spring:message code="barter.title" var="titleHeader" />
	<display:column title="${titleHeader}"
		sortable="true">
		<jstl:out value="${row_Barter.title}"/>
	</display:column>
	
	<spring:message code="barter.registerMoment" var="registerMomentHeader" />
	<display:column title="${registerMomentHeader}"
		sortable="true">
		<jstl:out value="${row_Barter.registerMoment}"/>
	</display:column>

	<spring:message code="barter.offered" var="offeredHeader" />
	<display:column title="${offeredHeader}" sortable="true">
		<a href="item/display.do?itemId=${row_Barter.offered.id}">
			<jstl:out value="${row_Barter.offered.name}"/>
		</a>
	</display:column>
	
	<spring:message code="barter.requested" var="requestedHeader" />
	<display:column title="${requestedHeader}" sortable="true">
		<a href="item/display.do?itemId=${row_Barter.requested.id}">
			<jstl:out value="${row_Barter.requested.name}"/>
		</a>
	</display:column>
	
	<spring:message code="barter.closed" var="closedHeader" />
	<display:column title="${closedHeader}" sortable="true">
		<jstl:if test="${row_Barter.closed == true}">
			<spring:message code="barter.yes" />
		</jstl:if>
		<jstl:if test="${row_Barter.closed == false}">
			<spring:message code="barter.no" />
		</jstl:if>
	</display:column>
	
	<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${userIdListing != null}">
			<spring:message code="barter.close" var="closeHeader" />
			<display:column title="${closeHeader}" sortable="true">
				<jstl:if test="${row_Barter.closed == false}">
						<a href="barter/administrator/close.do?barterId=${row_Barter.id}&userId=${userIdListing}"> <spring:message code="barter.close"/></a>
				</jstl:if>
			</display:column>
		</jstl:if>
	</security:authorize>
	
	<spring:message code="barter.relatedBarter" var="relatedBarterHeader" />
	<display:column title="${relatedBarterHeader}" sortable="false">
		<a href="barter/list2.do?barterId=${row_Barter.id}"> <spring:message
				code="barter.relatedBarter"/>
		</a>
	</display:column>
	
	<spring:message code="barter.complaint" var="complaintHeader" />
	<display:column title="${complaintHeader}" sortable="false">
		<a href="complaint/list.do?barterOrMatchId=${row_Barter.id}"> <spring:message
				code="barter.complaint"/>
		</a>
	</display:column>


</display:table>

<br/>
<br/>

<form action="${requestURI}">
	<input type="text" name="keyword"> <input type="submit"
		value="<spring:message code="barter.search" />" />&nbsp;
</form>

<!-- Action links -->
<security:authorize access="hasRole('USER')">
	<br/>
	<div>
		<a href="barter/user/create.do"> <spring:message
				code="barter.create" />
		</a>
	</div>
</security:authorize>


