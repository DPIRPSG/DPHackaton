<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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

	<!-- Listing grid -->
	
	<strong><spring:message code="actor.name" />:</strong> <jstl:out value="${actor.name}"/><br>
	<strong><spring:message code="actor.surname" />:</strong> <jstl:out value="${actor.surname}"/><br>
	<strong><spring:message code="actor.phone" />:</strong> <jstl:out value="${actor.phone}"/><br>
	
	<jstl:if test="${actor.curriculum != null}">
		<strong><spring:message code="curriculum.statement" />:</strong> <jstl:out value="${actor.curriculum.statement}"/><br>
		<strong><spring:message code="curriculum.skills" />:</strong><br>
	 	<jstl:forEach var="skill" items="${skills}" >
	        -<jstl:out value="${skill}"/><br>
	    </jstl:forEach>
	    <strong><spring:message code="curriculum.likes" />:</strong><br>
	 	<jstl:forEach var="like" items="${likes}" >
	        -<jstl:out value="${like}"/><br>
	    </jstl:forEach>
	    <strong><spring:message code="curriculum.dislikes" />:</strong><br>
	 	<jstl:forEach var="dislike" items="${dislikes}" >
	        -<jstl:out value="${dislike}"/><br>
	    </jstl:forEach>
	    <br>
	    </jstl:if>


<!-- Action links -->

