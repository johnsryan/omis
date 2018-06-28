<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Multi-screen index.
 -
 - Author: Stephen Abson
 - Author: Ryan Johns
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.general" var="generalBundle"/>
<fmt:setBundle basename="omis.offender.msgs.offender" var="offenderBundle"/>
<fmt:bundle basename="omis.msgs.index">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
  <head>
    	<title>
    		<fmt:message key="productName" bundle="${generalBundle}"/>
    		 -
    		<fmt:message key="${hostingEnvPropertyHolder.propertyValue}HostingEnvLabel" bundle="${generalBundle}"/>
    	</title>
		<jsp:include page="common/includes/headerMetas.jsp"/>
		<jsp:include page="common/includes/headerGeneralResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/formResources.jsp"/>
		<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/index.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/multi.css?VERSION=5"/>	
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/applicationToolbar.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/user/admin/style/userAdmin.css"/>
		<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/common/images/appIcon.ico" />
		<jsp:include page="common/includes/searchResources.jsp"/>
		<jsp:include page="common/includes/tabsResources.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/index/scripts/multi.js?VERSION=6"></script>
  </head>
  <body class="containerPage">
		<div id="contentFrameContainer">
			<div id="tabContainer" class="tabContainer">
				<div id="offenderTabs" class="tabbed_area perm">
 					<div class="tabIcons">
						<a id="multiMenu" href="${pageContext.request.contextPath}/multi/actionMenu.html" class="actionMenuItem"></a>
						<a id="homeLink" class="homeLink iconLink" href="${pageContext.request.contextPath}/home.html" title="<fmt:message key='homeLink'/>"></a>
						<sec:authorize access="hasRole('ADMIN') or hasRole('NOCR_HOME_VIEW')">
							<a id="reportHomeLink" class="reportHomeLink iconLink" href='${omis:getSystemProperty("jasperServerNocr")}' title="<fmt:message key='reportHomeLink'/>"></a>
						</sec:authorize>
						<sec:authorize access="hasRole('ADMIN') or hasRole('TASK_LIST')">
							<a id="taskListLink" class="taskListLink iconLink" href="${pageContext.request.contextPath}/task/list.html" title="<fmt:message key='taskLink'/>"></a>
						</sec:authorize>
						<c:if test="${not empty helpLinkPropertyHolder.propertyValue}">
							<a id="applicationHelpLink" class="applicationHelpLink" target="_blank" href="${helpLinkPropertyHolder.propertyValue}" title="<fmt:message key='helpLink'/>"></a>
						</c:if>
						<input id="offenderSearch" autocomplete="off" type="text" title="${searchTitle}"/>
						<div id="offenderSearchResults" class="foregroundLight hide"></div>
					</div>
					<ul id="offenderTabs_tabLinks" class="tabs">	
					</ul>
					<div id="offenderTabs_tabSet" class="tabSet"></div>
				</div>
			</div>
		</div>
	</body>
</html>
</fmt:bundle>
