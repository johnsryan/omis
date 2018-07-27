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
 - Author: Josh Divine
 - Version: 0.1.0 (July 17, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
<head>
	<title>
		<fmt:message key="itineraryHearingsTitle"/>
	</title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/jQueryResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/serverConfigResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/messageResolverResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/paroleBoardItinerary/style/paroleBoardItinerary.css?VERSION=1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/paroleBoardItinerary/scripts/itineraryHearings.js?VERSION=1"></script>
</head>
<body>
	<form class="editForm">
		<div id="itineraryDetails">
			<fieldset>
				<legend><fmt:message key="paroleBoardItineraryDetailsTitle"/></legend>
				<span class="fieldGroup">
					<label class="fieldLabel detailsLabel">
						<fmt:message key="itineraryDatesLabel"/>
					</label>
					<span class="detail">
						<fmt:formatDate value="${itinerarySummary.startDate}" pattern="MM/dd/yyyy"/> - 
						<fmt:formatDate value="${itinerarySummary.endDate}" pattern="MM/dd/yyyy"/>
					</span>
				</span>
				<span class="fieldGroup">
					<label class="fieldLabel detailsLabel">
						<fmt:message key="locationLabel"/>
					</label>
					<span class="detail">
						<c:out value="${itinerarySummary.locationOrganizationName}"/>
					</span>
				</span>
			</fieldset>
		</div>
	</form>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/paroleBoardItinerary/itineraryHearingsActionMenu.html?paroleBoardItinerary=${itinerarySummary.id}"></a>
		<fmt:message key="itineraryHearingsTitle"/>
	</h1>
	<c:set var="paroleEligibilitySummaries" value="${scheduledHearings}" scope="request"/>
	<jsp:include page="includes/listTableEligibilitySummaries.jsp"/>
	<h1>
		<fmt:message key="unresolvedEligibilitySummariesTitle"/>
	</h1>
	<c:set var="paroleEligibilitySummaries" value="${unresolvedEligibilitySummaries}" scope="request"/>
	<jsp:include page="includes/listTableEligibilitySummaries.jsp"/>
</body>
</fmt:bundle>
</html>