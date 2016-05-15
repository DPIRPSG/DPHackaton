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
	name="categories" requestURI="${requestURI}" id="row_Category">
	<!-- Action links -->
	
	<display:column>
		<div>
			<b><a href="category/administrator/edit.do?categoryId=${row_Category.id}"> <spring:message
						code="category.edit" />
			</a></b>
		</div>
	</display:column>

	<!-- Attributes -->

	<spring:message code="category.name" var="nameHeader"/>
	<acme:displayColumn value="${row_Category.name }" title="${nameHeader}"/>
	
	<spring:message code="category.description" var="descriptionHeader"/>
	<acme:displayColumn value="${row_Category.description }" title="${descriptionHeader}"/>

</display:table>


<!-- Action links -->

<div>
	<b><a href="category/administrator/create.do"> <spring:message
				code="category.create" />
	</a></b>
</div>

