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
 - Version: 0.1.1 (Feb 20, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysis">
<form:form commandName="hearingAnalysisForm" class="editForm">
<fieldset>
	<legend><fmt:message key="eligibilityDetailsTitle"/></legend>
	<span class="fieldGroup">
		<label class="fieldLabel"><fmt:message key="hearingEligibleDateLabel"/></label>
		<span id="hearingEligibilityDate">
			<fmt:formatDate value="${eligibility.hearingEligibilityDate}" pattern="MM/dd/yyyy"/>
		</span>
	</span>
	<span class="fieldGroup">
		<label class="fieldLabel"><fmt:message key="nextReviewDateLabel"/></label>
		<span id="nextReviewDate">
			<fmt:formatDate value="${eligibility.reviewDate}" pattern="MM/dd/yyyy"/>
		</span>
	</span>
</fieldset>
<fieldset>
	<legend><fmt:message key="hearingAnalysisDetailsTitle"/></legend>
	
	<span class="fieldGroup">
		<form:label path="category" class="fieldLabel">
		<fmt:message key="categoryLabel"/></form:label>
		<form:select path="category">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="category" items="${categories}">
				<c:choose>
					<c:when test="${hearingAnalysisForm.category eq category}">
						<option value="${category.id}" selected="selected"><c:out value="${category.name}"/></option>
					</c:when>
					<c:otherwise>
						<option value="${category.id}"><c:out value="${category.name}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select>
		<form:errors path="category" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardItinerary" class="fieldLabel">
		<fmt:message key="boardItineraryLabel"/></form:label>
		<form:select path="boardItinerary">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="itinerary" items="${itineraries}">
				<c:choose>
					<c:when test="${hearingAnalysisForm.boardItinerary eq itinerary}">
						<option value="${itinerary.id}" selected="selected"><c:out value="${itinerary.paroleBoardLocation.location.organization.name}"/> (<fmt:formatDate value="${itinerary.dateRange.startDate}" pattern="MM/dd/yyyy"/>-<fmt:formatDate value="${itinerary.dateRange.endDate}" pattern="MM/dd/yyyy"/>)</option>
					</c:when>
					<c:otherwise>
						<option value="${itinerary.id}"><c:out value="${itinerary.paroleBoardLocation.location.organization.name}"/> (<fmt:formatDate value="${itinerary.dateRange.startDate}" pattern="MM/dd/yyyy"/>-<fmt:formatDate value="${itinerary.dateRange.endDate}" pattern="MM/dd/yyyy"/>)</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select>
		<form:errors path="boardItinerary" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardMeetingSite" class="fieldLabel">
			<fmt:message key="boardMeetingSiteLabel"/></form:label>
		<c:set var="meetingSite" value="${hearingAnalysisForm.boardMeetingSite}" scope="request"/>
		<form:select path="boardMeetingSite">
			<jsp:include page="/WEB-INF/views/hearingAnalysis/includes/boardMeetingSiteOptions.jsp"/>
		</form:select>
		<form:errors path="boardMeetingSite" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="analyst" class="fieldLabel">
			<fmt:message key="analystLabel"/></form:label>
		<c:set var="analyst" value="${hearingAnalysisForm.analyst}" scope="request"/>
		<form:select path="analyst">
			<jsp:include page="/WEB-INF/views/hearingAnalysis/includes/boardAttendeeOptions.jsp"/>
		</form:select>
		<form:errors path="analyst" cssClass="error"/>
	</span>
</fieldset>
<fieldset id="hearingAnalysisNotesHolder">
	<legend><fmt:message key="hearingAnalysisNotesTitle"/></legend>
	<c:set var="hearingAnalysisNoteItems" value="${hearingAnalysisForm.hearingAnalysisNoteItems}" scope="request"/>
	<jsp:include page="hearingAnalysisNotesTable.jsp"/>
	<form:errors path="hearingAnalysisNoteItems" cssClass="error"/>
</fieldset>
<c:if test="${not empty hearingAnalysis}">
<c:set var="updatable" value="${hearingAnalysis}" scope="request"/>
<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
</c:if>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>