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


<security:authorize access="hasRole('USER')">

	<!-- Form -->
	<form:form action="match/user/create.do" modelAttribute="match">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="auditor"/>
		<form:hidden path="cancelled"/>
		<form:hidden path="closed"/>
		<form:hidden path="creationMoment"/>
		<form:hidden path="offerSignsDate"/>
		<form:hidden path="requestSignsDate"/>
		<form:hidden path="report"/>
		
		<!-- Editable Attributes -->
		
		<form:label path="legalText">
			<spring:message code="match.legalText" />:
		</form:label>
		<form:select name="legalText" path="legalText">
<!-- 		    <option value="">---</option> -->
		    <jstl:forEach var="legalText" items="${legalTexts}" >
		        <form:option value="${legalText.id}"><jstl:out value="${legalText.text}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="legalText" cssClass="error" />
		<br>
		
		<form:label path="creatorBarter">
			<spring:message code="match.creatorBarter" />:
		</form:label>
		<form:select name="creatorBarter" path="creatorBarter">
<!-- 		    <option value="">---</option> -->
		    <jstl:forEach var="creatorBarter" items="${creatorBarters}" >
		        <form:option value="${creatorBarter.id}"><jstl:out value="${creatorBarter.title}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="creatorBarter" cssClass="error" />
		<br>
		
		<form:label path="receiverBarter">
			<spring:message code="match.receiverBarter" />:
		</form:label>
		<form:select name="receiverBarter" path="receiverBarter">
<!-- 		    <option value="">---</option> -->
		    <jstl:forEach var="receiverBarter" items="${receiverBarters}" >
		        <form:option value="${receiverBarter.id}"><jstl:out value="${receiverBarter.title}"/></form:option>
		    </jstl:forEach>
		</form:select>
		<form:errors path="receiverBarter" cssClass="error" />
		<br>
				
		<!-- Action buttons -->
		<acme:submit name="save" code="match.create.save"/>
		&nbsp;
		<acme:cancel code="match.create.cancel" url="/match/user/list.do"/>
		
	</form:form>

</security:authorize>