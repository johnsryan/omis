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
 - Version: 0.1.0 (May 31, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editBoardHearing" access="hasRole('BOARD_CONSIDERATION_EDIT') or hasRole('ADMIN') or hasRole('BOARD_CONSIDERATION_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.boardconsideration.msgs.boardConsideration">
<form:form commandName="boardConsiderationForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="eligibilityDetailsTitle"/></legend>
		<span class="fieldGroup">
			<label class="fieldLabel"><fmt:message key="hearingEligibleDateLabel"/></label>
			<span id="hearingEligibilityDate">
				<fmt:formatDate value="${hearingAnalysis.eligibility.hearingEligibilityDate}" pattern="MM/dd/yyyy"/>
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel"><fmt:message key="nextReviewDateLabel"/></label>
			<span id="nextReviewDate">
				<fmt:formatDate value="${hearingAnalysis.eligibility.reviewDate}" pattern="MM/dd/yyyy"/>
			</span>
		</span>
	</fieldset>
	<fieldset>
		<legend>
			<fmt:message key="boardConsiderationsTitle" />
		</legend>
		<span class="fieldGroup">
			<c:set var="boardConsiderationItems" value="${boardConsiderationForm.boardConsiderationItems}" scope="request"/>
			<jsp:include page="boardConsiderationTable.jsp"/>
		</span>
	</fieldset>
	<fieldset>
		<legend>
			<fmt:message key="notesTitle" />
		</legend>
		<span class="fieldGroup">
			<c:set var="boardConsiderationNoteItems" value="${boardConsiderationForm.boardConsiderationNoteItems}" scope="request"/>
			<jsp:include page="boardConsiderationNoteTable.jsp"/>
		</span>
	</fieldset>
	<c:if test="${editBoardHearing}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>