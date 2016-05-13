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
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="runners" requestURI="${requestURI}" id="row_Runner">
	<!-- Action links -->
	

	<!-- Attributes -->
	<spring:message code="runner.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.name}"/>
	</display:column>

	<spring:message code="runner.surname" var="surnameHeader" />
	<display:column title="${surnameHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.surname}"/>
	</display:column>
	
	<spring:message code="runner.phone" var="phoneHeader" />
	<display:column title="${phoneHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.phone}"/>
	</display:column>
	
	<spring:message code="runner.nif" var="nifHeader" />
	<display:column title="${nifHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.nif}"/>
	</display:column>
	
	<spring:message code="runner.curriculum.statement" var="statementHeader" />
	<display:column title="${statementHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.curriculum.statement}"/>
	</display:column>
	
	<spring:message code="runner.curriculum.skills" var="skillsHeader" />
	<display:column title="${skillsHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.curriculum.skills}"/>
	</display:column>
	
	<spring:message code="runner.curriculum.likes" var="likesHeader" />
	<display:column title="${likesHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.curriculum.likes}"/>
	</display:column>
	
	<spring:message code="runner.curriculum.dislikes" var="dislikesHeader" />
	<display:column title="${dislikesHeader}"
		sortable="true">
		<jstl:out value="${row_Runner.curriculum.dislikes}"/>
	</display:column>
	

</display:table>

<br/>
<br/>


<!-- Action links -->


<!-- Alert -->

