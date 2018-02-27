<?xml version="1.0" encoding="UTF-8"?>
<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%--
 - Administration index page.
 -
 - Author: Stephen Abson
 - Author: Josh Divine
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.msgs.adminIndex">
<head>
	<title><fmt:message key="adminIndexHeader">
			<fmt:param value="${offenderNumber}"/>
		</fmt:message></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerMetas.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerGeneralResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/user/admin/style/userAdmin.css"/>
</head>
<body>
	<h1><fmt:message key="adminIndexHeader"/></h1>
	<ul class="taskLinks">
		<li class="header"><h2><fmt:message key="adminTasksHeader"/></h2></li>
	 	<sec:authorize access="hasRole('ADMIN') or hasRole('CONTENT_LIST')">
  		<li>
  			<a class="listLink" href="${pageContext.request.contextPath}/content/list.html">
  				<fmt:message key="contentListingLink"/></a>
  		</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('ADMIN') or hasRole('SESSION_MANAGER_LIST')">
   		<li>
   			<a class="listLink" href="${pageContext.request.contextPath}/sessionManager/list.html">
   				<fmt:message key="sessionManagerListingLink"/></a>
   		</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('ADMIN') or hasRole('USER_ADMIN_INDEX_VIEW')">
   		<li>
   			<a class="userAdminLink" href="${pageContext.request.contextPath}/user/admin/index.html">
   				<fmt:message key="userAdminLink"/></a>
   		</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('ADMIN') or hasRole('CACHE_MANAGER')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/cache/manage.html">
   				<fmt:message key="manageCacheLink"/></a>
   		</li>
  	</sec:authorize>
  </ul>
  <ul class="taskLinks">
  	<li class="header"><h2><fmt:message key="moduleAdministrationTasksHeader"/></h2></li>
  	<sec:authorize access="hasRole('ADDRESS_MODULE') or hasRole('ADMIN')">
    	<li>
    		<a class="configLink" href="${pageContext.request.contextPath}/address/list.html">
    			<fmt:message key="manageAddressesLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('APP_DEV')">
    	<li>
    		<a class="configLink" href="${pageContext.request.contextPath}/location/list.html">
    			<fmt:message key="manageLocationsLink"/></a>
    	</li>
    </sec:authorize>
    
    <sec:authorize access="hasRole('APP_DEV')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/disciplinaryCode/supervisoryOrganizationSelection.html">
   				<fmt:message key="manageDisciplinaryCodesLink"/></a>
   		</li>
  	</sec:authorize>
  	
  	<sec:authorize access="hasRole('APP_DEV')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/questionnaire/questionnaireTypeList.html">
   				<fmt:message key="manageQuestionnairesLink"/></a>
   		</li>
  	</sec:authorize>
  	
  	<sec:authorize access="hasRole('APP_DEV')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/offenderFlagCategory/list.html">
   				<fmt:message key="offenderFlagCategoriesAdminLink"/></a>
   		</li>
  	</sec:authorize>
    
    <sec:authorize access="hasRole('APP_DEV')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/facility/list.html">
   				<fmt:message key="facilityAdminLink"/></a>
   		</li>
  	</sec:authorize>
  	
  	<sec:authorize access="hasRole('APP_DEV')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/identificationNumber/category/list.html">
   				<fmt:message key="manageIdentificationNumberCategoriesLink"/></a>
   		</li>
  	</sec:authorize>
  	
  	<sec:authorize access="hasRole('ADMIN') or hasRole('OFFENSE_ADMIN')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/offense/list.html">
   				<fmt:message key="offenseAdminLink"/></a>
   		</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('ADMIN') or hasRole('STAFF_MODULE')">
		<li>
			<a class="configLink" href="${pageContext.request.contextPath}/staff/index.html">
				<fmt:message key="staffModuleLink"/>
			</a>
		</li>
	</sec:authorize>
	
	<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_LIST') or hasRole('ADMIN')">
   		<li>
   			<a class="configLink" href="${pageContext.request.contextPath}/paroleBoardLocation/list.html">
   				<fmt:message key="manageParoleBoardLocationsLink"/></a>
   		</li>
  	</sec:authorize>
	</ul>
</body>
</fmt:bundle>
</html>