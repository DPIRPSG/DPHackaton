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
	name="sponsors" requestURI="${requestURI}" id="row_Sponsor">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<div>
				<b><acme:link href="sponsor/administrator/edit.do?sponsorId=${row_Sponsor.id}" code="sponsor.edit"/></b>
			</div>
		</display:column>
	</security:authorize>

	<!-- Attributes -->

	<spring:message code="sponsor.logo" var="pictureHeader"/>
	<display:column title="${pictureHeader}"
		sortable="false" >
		<img src="${row_Sponsor.logo}" style="width:100px;"/>
	</display:column>

	<spring:message code="sponsor.name" var="nameHeader"/>
	<acme:displayColumn value="${row_Sponsor.name}" title="${nameHeader}"/>
	
	<spring:message code="sponsor.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_Sponsor.description }" title="${descriptionHeader}"/>
	
	<spring:message code="sponsor.finances" var="financesHeader" />
	<display:column title="${financesHeader}" sortable="false">
		<acme:link href="finances/list.do?sponsorId=${row_Sponsor.id}" code="sponsor.finances.view"/>
	</display:column>
	
</display:table>


<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<b><acme:link href="sponsor/administrator/create.do" code="sponsor.create"/></b>
	</div>
</security:authorize>
