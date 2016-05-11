<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

 	<h3><spring:message code="administrator.getTotalNumberOfUsersRegistered"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getTotalNumberOfUsersRegistered == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getTotalNumberOfUsersRegistered}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getTotalNumberOfBarterRegistered"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getTotalNumberOfBarterRegistered == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getTotalNumberOfBarterRegistered}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getTotalNumberOfBarterCancelled"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getTotalNumberOfBarterCancelled == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getTotalNumberOfBarterCancelled}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getUsersAbovePencentile90"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersAbovePencentile90" requestURI="${requestURI}" id="row1">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row1.name}"/>
  		</display:column>
  	</display:table>
  	
	<h3><spring:message code="administrator.getUsersWithNoBarterThisMonth"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWithNoBarterThisMonth" requestURI="${requestURI}" id="row2">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row2.name}"/>
  		</display:column>
  	</display:table>
  	
  	<h3><spring:message code="administrator.getTotalNumberOfComplaintsCreated"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getTotalNumberOfComplaintsCreated == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getTotalNumberOfComplaintsCreated}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getAverageOfComplaintsPerBarter"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getAverageOfComplaintsPerBarter == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getAverageOfComplaintsPerBarter}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getAverageOfComplaintsPerMatch"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${getAverageOfComplaintsPerMatch == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${getAverageOfComplaintsPerMatch}" />
		</jstl:otherwise>
	</jstl:choose>
	
  	<h3><spring:message code="administrator.getUsersWhoHaveCreatedMoreComplaintsThatAverage"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWhoHaveCreatedMoreComplaintsThatAverage" requestURI="${requestURI}" id="row8">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row8.name}"/>
  		</display:column>
  	</display:table>
  	
  	
  	<h3><spring:message code="administrator.minumumNumberBarterPerUser"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${minumumNumberBarterPerUser == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${minumumNumberBarterPerUser}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.maximumNumberBarterPerUser"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${maximumNumberBarterPerUser == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${maximumNumberBarterPerUser}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.averageNumberBarterPerUser"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberBarterPerUser == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberBarterPerUser}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.ratioBarterNotRelatedToAnyOtherBarter"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${ratioBarterNotRelatedToAnyOtherBarter == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${ratioBarterNotRelatedToAnyOtherBarter}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.getUsersWithMoreBarters"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWithMoreBarters" requestURI="${requestURI}" id="row3">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row3.name}"/>
  		</display:column>
  	</display:table>
  	
	<h3><spring:message code="administrator.getUsersWithMoreBartersCancelled"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWithMoreBartersCancelled" requestURI="${requestURI}" id="row4">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row4.name}"/>
  		</display:column>
  	</display:table>
  	
	<h3><spring:message code="administrator.getUsersWithMoreMatches"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWithMoreMatches" requestURI="${requestURI}" id="row5">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row5.name}"/>
  		</display:column>
  	</display:table>
  	
	<h3><spring:message code="administrator.getAuditorsWithMoreMatches"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getAuditorsWithMoreMatches" requestURI="${requestURI}" id="row6">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row6.name}"/>
  		</display:column>
  	</display:table>
  	
 	<h3><spring:message code="administrator.getUsersWithMoreMatchesAudited"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
  		name="getUsersWithMoreMatchesAudited" requestURI="${requestURI}" id="row7">
		<!-- Attributes -->
		<spring:message code="user.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
  			sortable="false" >
 			<jstl:out value="${row7.name}"/>
  		</display:column>
  	</display:table>
  	
</security:authorize>