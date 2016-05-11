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
	<div>
	<table>

	<acme:display code="user.name" value="${user.name}"/>
	<acme:display code="user.surname" value="${user.surname}"/>
	<acme:display code="user.phone" value="${user.phone}"/>
	<acme:display code="user.username" value="${user.userAccount.username}"/>
	
	<tr>
		<th><spring:message code="user.socialIdentity" /> :</th>
		<td><a href="socialIdentity/user/list.do"> 
				<spring:message code="user.display" />
			</a>
		</td>
	</tr>
	</table>
	</div>

	
	<!-- Action links -->
	<div>
		<b><a href="user/user/edit.do"> 
			<spring:message code="user.edit" />
		</a></b>
	</div>
	<br/>
	<spring:message code="user.delete"/>
