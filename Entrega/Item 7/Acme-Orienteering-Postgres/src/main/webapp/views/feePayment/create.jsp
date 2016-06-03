<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access = "hasRole('MANAGER')">
	<!-- Form -->
	<form:form action="feePayment/gerente/create.do" modelAttribute="feePaymentForm">
		<!-- Hidden Attributes -->
		
		<form:hidden path="paymentMoment"/>

		<form:hidden path="club"/>
		<form:hidden path="league"/>
		
		<!-- Editable Attributes -->
		<h3><spring:message code="feePayment.club" /> "<jstl:out value="${feePaymentForm.club.name}"/>" <spring:message code="feePayment.league" /> "<jstl:out value="${feePaymentForm.league.name}"/>"</h3>
				
		<fieldset>
			<legend align="left">
				<spring:message code = "feePayment.creditCard"/>
			</legend>
		
			<acme:textbox code="feePayment.holderName" path="holderName" />
			<acme:textbox code="feePayment.brandName" path="brandName" />
			<acme:textbox code="feePayment.number" path="number" />
			<acme:textbox code="feePayment.expirationMonth" path="expirationMonth" />
			<acme:textbox code="feePayment.expirationYear" path="expirationYear" />
			<acme:textbox code="feePayment.cvvCode" path="cvvCode" />
		
		</fieldset>
		<br/>
		
		<!-- Action buttons -->
		<acme:submit name="save" code="feePayment.save"/>
		<input type="button" name="cancel"
			value="<spring:message code="feePayment.cancel" />"
			onclick="javascript: relativeRedir('league/list.do');" />
		<br />
		
	</form:form>
</security:authorize>
