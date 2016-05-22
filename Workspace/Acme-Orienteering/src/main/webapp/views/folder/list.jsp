<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="false"
		name="folders" requestURI="${requestURI}" id="row_folder">

		<spring:message code="folder.editHeader" var="editHeader" />
		<jstl:if test="${!row_folder.isSystem}">
			<display:column title="${editHeader}">
				<acme:link href="folder/actor/edit.do?folderId=${row_folder.id}" code="folder.edit"/>
			</display:column>
		</jstl:if>
		<jstl:if test="${row_folder.isSystem}">
			<display:column title="${editHeader}" />
		</jstl:if>


		<!-- Attributes -->
				<spring:message code="folder.name" var="nameHeader" />
		
		<acme:displayColumn value="${row_folder.name}" title="${nameHeader}" sorteable="true"/>

		<spring:message code="folder.messages" var="messageHeader" />
		<display:column>
			<acme:link href="message/actor/list.do?folderId=${row_folder.id}" code="folder.messages"/>
		</display:column>

	</display:table>
	
	<!-- Action links -->
	<div>
		<b><acme:link href="folder/actor/create.do" code="folder.create"/></b>
	</div>
	<div>
		<b><acme:link href="message/actor/create.do" code="message.create"/></b>
	</div>

