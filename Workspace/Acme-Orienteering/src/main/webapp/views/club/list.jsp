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
	name="clubes" requestURI="${requestURI}" id="row_Club">
	<!-- Action links -->
	
	<display:column>
		<div>
			<b><a href="club/manager/edit.do?clubId=${row_Club.id}"> <spring:message
						code="club.edit" />
			</a></b>
		</div>
	</display:column>
	
	<!-- Attributes -->

	<spring:message code="club.name" var="nameHeader"/>
	<acme:displayColumn value="${row_Club.name }" title="${nameHeader}"/>
	
	<spring:message code="club.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_Club.description }" title="${descriptionHeader}"/>
	
	<spring:message code="club.pictures" var="pictureHeader"/>
	<display:column title="${pictureHeader}"
		sortable="false" >
		<jstl:forEach items="${row_Club.pictures}" var="picture">
		</jstl:forEach>
		<img src="${picture}" style="width:204px;"/>
	</display:column>
	
	<spring:message code="club.creationMoment" var="creationMomentHeader" />
	<display:column title="${creationMomentHeader}" sortable="true">
		<fmt:formatDate value="${row_Club.creationMoment}" pattern="dd-MM-yyyy"/>
	</display:column>

</display:table>


<!-- Action links -->

<div>
	<b><a href="club/manager/create.do"> <spring:message
				code="club.create" />
	</a></b>
</div>
