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
  - Screen to display board home content.
  -
  - Author: Josh Divine
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:bundle basename="omis.bopp.msgs.boardHome">
	<div id="boppModuleGroup" class="moduleGroupLinkContainer">
		<a href="${pageContext.request.contextPath}/paroleBoardItinerary/list.html"><fmt:message key="itineraryLink"/></a>
		<a href="${pageContext.request.contextPath}/paroleEligibility/listUnresolved.html"><fmt:message key="unresolvedEligibilitiesLink"/></a>
		<a href="${pageContext.request.contextPath}/paroleEligibility/listUnscheduled.html"><fmt:message key="unscheduledEligibilitiesLink"/></a>
		<a href="${pageContext.request.contextPath}/paroleBoardMember/list.html"><fmt:message key="paroleBoardMembersLink"/></a>
		<a href="${pageContext.request.contextPath}/paroleBoardLocation/list.html"><fmt:message key="paroleBoardLocationsLink"/></a>
	</div>
</fmt:bundle>