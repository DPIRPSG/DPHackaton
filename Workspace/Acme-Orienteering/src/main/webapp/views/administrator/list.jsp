<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<h3><spring:message code="administrator.findAllWhoHaveWonMoreLeagues"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
    		name="findAllWhoHaveWonMoreLeagues" requestURI="${requestURI}" id="row1">
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
    			sortable="false" >
  			<jstl:out value="${row1.name}"/>
    		</display:column>
    	</display:table>
   	
   	<h3><spring:message code="administrator.findAllWhoHaveWonMoreRaces"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
    		name="findAllWhoHaveWonMoreRaces" requestURI="${requestURI}" id="row2">
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
    			sortable="false" >
  			<jstl:out value="${row2.name}"/>
    		</display:column>
    	</display:table>
   	
   	<h3><spring:message code="administrator.findAllWhoHaveMoreDeniedEntered"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
    		name="findAllWhoHaveMoreDeniedEntered" requestURI="${requestURI}" id="row3"> --%>
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
    			sortable="false" > --%>
  			<jstl:out value="${row3.name}"/>
    		</display:column>
    	</display:table>
   	
   	<h3><spring:message code="administrator.findAllWhoHaveMorePunishments"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
    		name="findAllWhoHaveMorePunishments" requestURI="${requestURI}" id="row4"> --%>
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
	   			sortable="false" >
  			<jstl:out value="${row4.name}"/>
    		</display:column>
    	</display:table>
 	
 	<h3><spring:message code="administrator.ratioOfClubsByLeague"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${ratioOfClubsByLeague == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${ratioOfClubsByLeague}" />
		</jstl:otherwise>
	</jstl:choose>
 	
   	<h3><spring:message code="administrator.findAllWhoHaveMoreRaces"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
     		name="findAllWhoHaveMoreRaces" requestURI="${requestURI}" id="row6"> --%>
		<!-- Attributes -->
		<spring:message code="league.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
     			sortable="false" >
   			<jstl:out value="${row6.name}"/>
     		</display:column>
     </display:table>
 	
 	<h3><spring:message code="administrator.ratioOfRacesByLeague"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${ratioOfRacesByLeague == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${ratioOfRacesByLeague}" />
		</jstl:otherwise>
	</jstl:choose>
 	
   	<h3><spring:message code="administrator.findAllWhoHaveMoreClubs"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
     		name="findAllWhoHaveMoreClubs" requestURI="${requestURI}" id="row8">
		<!-- Attributes -->
		<spring:message code="league.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
     			sortable="false" >
   			<jstl:out value="${row8.name}"/>
     		</display:column> 
     </display:table> 

   	<h3><spring:message code="administrator.findAllMostFrequentInRaces"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
     		name="findAllMostFrequentInRaces" requestURI="${requestURI}" id="row9">
		<!-- Attributes -->
		<spring:message code="category.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
     			sortable="false" >
   			<jstl:out value="${row9.name}"/>
     		</display:column> 
     </display:table> 	

   	<h3><spring:message code="administrator.findAllWithMorePoints"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
   		name="findAllWithMorePoints" requestURI="${requestURI}" id="row10">
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
     			sortable="false" >
  			<jstl:out value="${row10.name}"/>
     		</display:column>
     </display:table> 	

   	<h3><spring:message code="administrator.findAllWithLessPoint"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
   		name="findAllWithLessPoint" requestURI="${requestURI}" id="row11">
		<!-- Attributes -->
		<spring:message code="club.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
     			sortable="false" >
   			<jstl:out value="${row11.name}"/>
     		</display:column> 
     </display:table> 	 
 	 	
</security:authorize>