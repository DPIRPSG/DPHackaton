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

<spring:message code="complaint.barterOrMatchConcerning"/>: <jstl:out value="${barterOrMatchName}" /><br>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="complaints" requestURI="${requestURI}" id="row_Complaint">
	
	<!-- Attributes -->
	<spring:message code="complaint.user" var="userHeader" />
	<acme:displayColumn title="${userHeader}" sorteable="true" value="${row_Complaint.user.userAccount.username}" />
	
	<spring:message code="complaint.text" var="textHeader" />
	<acme:displayColumn title="${textHeader}" sorteable="false" value="${row_Complaint.text}" />

</display:table>
<br>
	
<security:authorize access="hasRole('USER')">

	<jstl:if test="${ row_Complaint.barter != null }">
		<jstl:if test="${ row_Complaint.barter.user.id != userId }">
			<jstl:if test="${ row_Complaint.barter.closed == false }">
				<a href="complaint/user/create.do?barterOrMatchId=${barterOrMatchId}"><spring:message code="complaint.create"/></a>
			</jstl:if>
		</jstl:if>
	</jstl:if>
	<jstl:if test="${ row_Complaint.match != null }">
		<jstl:if test="${ row_Complaint.match.creatorBarter.user.id == userId || row_Complaint.match.receiverBarter.user.id == userId }">
			<jstl:if test="${ row_Complaint.match.closed == false }">
				<a href="complaint/user/create.do?barterOrMatchId=${barterOrMatchId}"><spring:message code="complaint.create"/></a>
			</jstl:if>
		</jstl:if>
	</jstl:if>
	<jstl:if test="${ row_Complaint == null }">
		<a href="complaint/user/create.do?barterOrMatchId=${barterOrMatchId}"><spring:message code="complaint.create"/></a>
	</jstl:if>
	<jstl:if test="${ row_Complaint.barter.closed == true }">
		<spring:message code="complaint.closedBarter" />
	</jstl:if>
	<jstl:if test="${ row_Complaint.match.closed == true }">
		<spring:message code="complaint.closedMatch" />
	</jstl:if>
	
</security:authorize>
	