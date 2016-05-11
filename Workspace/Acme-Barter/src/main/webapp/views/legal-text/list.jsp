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
	name="legalTexts" requestURI="${requestURI}" id="row_LegalText">
	<!-- Action links -->

	<!-- Attributes -->
	<display:column>
		<div>
			<b><a href="legal-text/administrator/edit.do?legalTextId=${row_LegalText.id}"> <spring:message
						code="legalText.edit" />
			</a></b>
		</div>
	</display:column>

	<spring:message code="legalText.text" var="textHeader" />
	<display:column title="${textHeader}"
		sortable="true">
		<jstl:out value="${row_LegalText.text}"/>
	</display:column>
</display:table>

<br/>
<br/>

<!-- Action links -->

<div>
	<b><a href="legal-text/administrator/create.do"> <spring:message
				code="legalText.create" />
	</a></b>
</div>
