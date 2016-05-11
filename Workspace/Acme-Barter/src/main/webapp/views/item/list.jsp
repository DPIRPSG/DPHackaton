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
	name="items" requestURI="${requestURI}" id="row_Item">
	<!-- Action links -->

	<!-- Attributes -->

	<spring:message code="item.name" var="nameHeader" />
	<acme:displayColumn value="${row_Item.name}" title="${nameHeader}"
		sorteable="true" />

	<spring:message code="item.description" var="descriptionHeader" />
	<acme:displayColumn value="${row_Item.description}"
		title="${descriptionHeader}" sorteable="false" />

	<spring:message code="item.pictures" var="picturesHeader" />
	<display:column title="${picturesHeader}" sortable="false">
		<jstl:forEach items="${row_Item.pictures}" var="picture">
			<img src="${picture}" style="width: 204px;" />
		</jstl:forEach>
	</display:column>

	<display:column>
			<a href="attribute-description/user/list.do?itemId=${row_Item.id}"> <spring:message
					code="item.info" />
			</a>
	</display:column>

</display:table>

<br/>
<br/>

<!-- Action links -->

