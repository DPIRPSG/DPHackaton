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

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href=""><img src="images/logo.png" alt="Acme Orienteering Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.manage" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="league/administrator/list.do"><spring:message code="master.page.administrator.league" /></a></li>
					<li><a href="race/administrator/list.do"><spring:message code="master.page.administrator.race" /></a></li>
					<li><a href="sponsor/list.do"><spring:message code="master.page.administrator.sponsor" /></a></li>
					<li><a href="category/administrator/list.do"><spring:message code="master.page.administrator.category" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="club/list.do"><spring:message code="master.page.club" /></a></li>
			<li><a class="fNiv" href="punishment/list.do"><spring:message code="master.page.punishment" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('RUNNER')">
			<li><a class="fNiv" href="club/list.do"><spring:message code="master.page.club" /></a></li>
			<li><a class="fNiv" href="club/runner/list.do"><spring:message code="master.page.manager.myClub" /></a></li>
			<li><a class="fNiv" href="league/list.do"><spring:message code="master.page.administrator.league" /></a></li>
			<li><a class="fNiv" href="race/list.do"><spring:message code="master.page.administrator.race" /></a></li>
			<li><a class="fNiv" href="punishment/list.do"><spring:message code="master.page.punishment" /></a></li>
			<li><a class="fNiv" href="sponsor/list.do"><spring:message code="master.page.administrator.sponsor" /></a></li>
			<li><a class="fNiv" href="entered/runner/list.do"><spring:message code="master.page.runner.entered" /></a></li>
			<li><a class="fNiv" href="curriculum/actor/list.do"><spring:message code="master.page.actor.curriculum" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message	code="master.page.manager.myClub" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="club/manager/list.do"><spring:message code="master.page.manager.myClub" /></a></li>
					<li><a href="entered/manager/list.do"><spring:message code="master.page.manager.entered" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="club/list.do"><spring:message code="master.page.club" /></a></li>
			<li><a class="fNiv" href="league/list.do"><spring:message code="master.page.administrator.league" /></a></li>
			<li><a class="fNiv" href="race/list.do"><spring:message code="master.page.administrator.race" /></a></li>
			<li><a class="fNiv" href="punishment/list.do"><spring:message code="master.page.punishment" /></a></li>
			<li><a class="fNiv" href="sponsor/list.do"><spring:message code="master.page.administrator.sponsor" /></a></li>			
			<li><a class="fNiv" href="curriculum/actor/list.do"><spring:message code="master.page.actor.curriculum" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('REFEREE')">
			<li><a class="fNiv" href="club/list.do"><spring:message code="master.page.club" /></a></li>
			<li><a class="fNiv" href="league/list.do"><spring:message code="master.page.administrator.league" /></a></li>
			<li><a class="fNiv" href="race/list.do"><spring:message code="master.page.administrator.race" /></a></li>
			<li><a class="fNiv" href="punishment/list.do"><spring:message code="master.page.punishment" /></a></li>
			<li><a class="fNiv" href="sponsor/list.do"><spring:message code="master.page.administrator.sponsor" /></a></li>
			<li><a class="fNiv" href="curriculum/actor/list.do"><spring:message code="master.page.actor.curriculum" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="club/list.do"><spring:message code="master.page.club" /></a></li>
			<li><a class="fNiv" href="league/list.do"><spring:message code="master.page.administrator.league" /></a></li>
			<li><a class="fNiv" href="race/list.do"><spring:message code="master.page.administrator.race" /></a></li>
			<li><a class="fNiv" href="punishment/list.do"><spring:message code="master.page.punishment" /></a></li>
			<li><a class="fNiv" href="sponsor/list.do"><spring:message code="master.page.administrator.sponsor" /></a></li>
			<li><a class="fNiv" href="runner/create.do"><spring:message code="master.page.register" /></a></li>		
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="folder/actor/list.do"><spring:message code="master.page.messages" /></a></li>

			<li>
				<a class="fNiv" href="actor/actor/display.do"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>			
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

