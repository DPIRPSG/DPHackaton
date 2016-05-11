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

	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag"
		name="socialIdentities" requestURI="${requestURI}" id="row_social">
		
		<spring:message code="socialIdentity.delete" var="deleteHeader" />
	<jstl:if test="${actUserId == row_social.user.id}">	
		<display:column>
		<a href="socialIdentity/user/edit.do?socialIdentityId=${row_social.id}">
			<spring:message code="socialIdentity.edit" />
		</a>
	</display:column>
	</jstl:if>
	

	<!-- Attributes -->
		<spring:message code="socialIdentity.picture" var="pictureHeader" />
		<display:column title="${pictureHeader}"
			sortable="false">
			<img src="${row_social.picture}" style="height:128px;"/>
		</display:column>
		
		<spring:message code="socialIdentity.nick" var="nickHeader" />
		<display:column title="${nickHeader}"
			sortable="true" >
			<jstl:out value="${row_social.nick}"/>
		</display:column>
			
		<spring:message code="socialIdentity.name" var="nameHeader" />
		<display:column title="${nameHeader}"
			sortable="true" >
			<jstl:out value="${row_social.name}"/>
		</display:column>
		
		<spring:message code="socialIdentity.homePage" var="homePageHeader" />
		<display:column title="${homePageHeader}"
			sortable="false" >
			<a href="${row_social.homePage}">
			<spring:message code="socialIdentity.homePage.link" />
		</a>
		</display:column>
	</display:table>
	
	<!-- Action links -->
	<jstl:if test="${(actUserId == row_social.user.id && row_social.user.id != null)
				|| isProperty}">
	<div>
		<b><a href="socialIdentity/user/edit.do"> 
			<spring:message code="socialIdentity.create" />
		</a></b>
	</div>
	</jstl:if>
