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
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="users" requestURI="${requestURI}" id="row_User">
	<!-- Action links -->
	
	<security:authorize access="hasRole('USER')">
		<jstl:set var="contains" value="false" />
		<jstl:forEach var="user" items="${IfollowTo}">
			<jstl:if test="${user.id == row_User.id}">
				<jstl:set var="contains" value="true" />
			</jstl:if>
		</jstl:forEach>	
		
		<display:column sortable="true">
			<a href="user/user/followOrUnfollow.do?userIdOtherUser=${row_User.id}&redirectUri=${requestURI}">
				<jstl:if test="${contains == false && row_User.id != userId}">
					<spring:message code="user.follow" />
				</jstl:if>
				<jstl:if test="${contains == true && row_User.id != userId}">
					<spring:message code="user.unfollow" />
				</jstl:if>
			</a>
		</display:column>
	
	</security:authorize>
	

	<!-- Attributes -->
	<spring:message code="user.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_User.name}"/>
	</display:column>

	<spring:message code="user.surname" var="surnameHeader" />
	<display:column title="${surnameHeader}"
		sortable="true">
		<jstl:out value="${row_User.surname}"/>
	</display:column>
	
	<spring:message code="user.socialIdentities" var="socialIdentitiesHeader" />
	<display:column title="${socialIdentitiesHeader}" sortable="false">
		<a href="socialIdentity/list.do?userId=${row_User.id}"><spring:message
				code="user.socialIdentities.link" /></a>
	</display:column>
	
	<spring:message code="user.barters" var="bartersHeader" />
	<display:column title="${bartersHeader}" sortable="false">
		<a href="barter/listByUser.do?userId=${row_User.id}"><spring:message
				code="user.barters.link" /></a>
	</display:column>
	
	<spring:message code="user.matches" var="matchesHeader" />
	<display:column title="${matchesHeader}" sortable="false">
		<a href="match/list.do?userId=${row_User.id}"><spring:message
				code="user.matches.link" /></a>
	</display:column>

</display:table>

<br/>
<br/>


<!-- Action links -->


<!-- Alert -->
<jstl:if test="${messageStatus != Null && messageStatus != ''}">
	<spring:message code="${messageStatus}" var="showAlert" />
			<script>$(document).ready(function(){
		    alert("${showAlert}");
		  });
		</script>
</jstl:if>	
