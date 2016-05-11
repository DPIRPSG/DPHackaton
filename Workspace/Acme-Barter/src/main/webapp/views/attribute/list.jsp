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
	name="attributes" requestURI="${requestURI}" id="row_Attribute">
	<!-- Action links -->

	<!-- Attributes -->
	<display:column>
		<div>
			<b><a href="attribute/administrator/edit.do?attributeId=${row_Attribute.id}"> <spring:message
						code="attribute.edit" />
			</a></b>
		</div>
	</display:column>

	<spring:message code="attribute.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_Attribute.name}"/>
	</display:column>
</display:table>

<br/>
<br/>

<!-- Action links -->

<div>
	<b><a href="attribute/administrator/create.do"> <spring:message
				code="attribute.create" />
	</a></b>
</div>
