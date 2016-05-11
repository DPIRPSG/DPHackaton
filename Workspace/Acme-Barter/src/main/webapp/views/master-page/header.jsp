<%--
 * header.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="noPrint">
<div>
	<a href=""> 
		<img src="images/logo.png" style="height:128px;" alt="ACME, Inc.  Your Barter Company" /></a>
	<br/>

</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv"><spring:message	code="master.page.barter" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="barter/user/list.do"><spring:message code="master.page.list" /></a></li>
					<li><a href="barter/user/display.do"><spring:message code="barter.novedades" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.match" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="match/user/list.do"><spring:message code="master.page.list" /></a></li>
					<li><a href="match/user/display.do"><spring:message code="barter.novedades" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="item/user/list.do"><spring:message code="master.page.items" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.users" /></a></li>			
			<li><a class="fNiv" href="user/user/followed.do"><spring:message code="master.page.followed" /></a></li>			
			<li><a class="fNiv" href="user/user/followers.do"><spring:message code="master.page.followers" /></a></li>			
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.manage" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="match/administrator/list.do"><spring:message code="master.page.match" /></a></li>
					<li><a href="barter/administrator/list.do"><spring:message code="master.page.barter" /></a></li>
					<li><a href="attribute/administrator/list.do"><spring:message code="master.page.attribute" /></a></li>
					<li><a href="legal-text/administrator/list.do"><spring:message code="master.page.list.legalTexts" /></a></li>
					<li><a href="auditor/administrator/register.do"><spring:message code="master.page.register.auditor" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.list" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="user/list.do"><spring:message code="master.page.users" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="dashboard/administrator/list.do"><spring:message code="master.page.dashboard" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('AUDITOR')">
			<li><a class="fNiv" href="barter/list.do"><spring:message code="master.page.barter" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.users" /></a></li>
			<li><a class="fNiv"><spring:message	code="master.page.manage" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="match/auditor/list-assigned.do"><spring:message code="master.page.match.listAssigned" /></a></li>
				</ul>
			</li>								
		</security:authorize>
				
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="barter/list.do"><spring:message code="master.page.barter" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.users" /></a></li>
			<li><a class="fNiv" href="user/create.do"><spring:message code="master.page.register" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="folder/actor/list.do"><spring:message code="master.page.messages" /></a>
				<ul> 
					<li class="arrow"></li>
					<li><a href="autoreply/actor/list.do"><spring:message code="master.page.autoreply" /></a></li>
				</ul>
			</li>
			<!-- <li><a href="j_spring_security_logout"><spring:message code="master.page.logout" />(<security:authentication property="principal.username" />)</a></li> -->
			<li><a class="fNiv"><security:authentication property="principal.username" /></a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="user/user/display.do"><spring:message code="master.page.user.info" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="admin/administrator/display.do"><spring:message code="master.page.user.info" /></a></li>
					</security:authorize>	
					<security:authorize access="hasRole('AUDITOR')">
						<li><a href="auditor/auditor/display.do"><spring:message code="master.page.user.info" /></a></li>
					</security:authorize>					
					<li><b><a href="j_spring_security_logout"><spring:message code="master.page.logout" /></a></b></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

<!-- OJO !!!! El script de cancel.tag está en el footer!!!! -->

</div>


