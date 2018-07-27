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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.hearing.msgs.hearing">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearing/scripts/includes/jquery.omis.resolution.js?VERSION=1"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/hearing/scripts/resolution.js?VERSION=2"></script>
	<script type="text/javascript">
		var currentUserAttendanceItemIndex = ${userAttendanceItemIndex};
	</script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/hearing/style/hearing.css" />
	<title>
		<fmt:message key="${resolutionCategory}ResolutionHeader" />
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<c:choose>
		<c:when test="${resolutionCategory eq 'FORMAL'}">
		<div id="hearingHeader" class="foregroundRegular">
			<fieldset>
				<span class="oneline">
					<span class="onelineHalf">
						<label class="fieldLabel detailsLabel">
							<fmt:message key="hearingDateLabel"/>
						</label>
						<span class="detail">
							<fmt:formatDate value="${hearingSummary.hearingDate}" pattern="MM/dd/yyyy"/>
						</span>
					</span>
					<span class="onelineHalf">
						<label class="fieldLabel detailsLabel">
							<fmt:message key="hearingTypeLabel"/>
						</label>
						<span class="detail">
							<fmt:message key="${hearingSummary.category}CategoryLabel" />
						</span>
					</span>
				</span>
				<span class="oneline">
					<label class="fieldLabel detailsLabel">
						<fmt:message key="locationLabel"/>
					</label>
					<span class="detail">
						<c:out value="${hearingSummary.locationName}"/>
					</span>
				</span>
				<span class="oneline">
					<span class="onelineHalf">
						<label class="fieldLabel detailsLabel">
							<fmt:message key="statusLabel"/>
						</label>
						<span class="detail">
							<fmt:message key="${hearingSummary.hearingStatusCategory}StatusLabel"/>
						</span>
					</span>
					<span class="onelineHalf">
						<label class="fieldLabel detailsLabel">
							<fmt:message key="statusDateLabel"/>
						</label>
						<span class="detail">
							<fmt:formatDate value="${hearingSummary.hearingStatusDate}" pattern="MM/dd/yyyy"/>
						</span>
					</span>
				</span>
			</fieldset>
			</div>
		</c:when>
	</c:choose>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/hearing/resolution/resolutionActionMenu.html?offender=${offender.id}"></a>
		<fmt:message key="${resolutionCategory}ResolutionHeader" />
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>