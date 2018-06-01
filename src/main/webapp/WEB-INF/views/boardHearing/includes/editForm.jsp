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
 - Author: Annie Wahl
 - Author: Josh Divine
 - Version: 0.1.2 (Apr 18, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editBoardHearing" access="hasRole('BOARD_HEARING_EDIT') or hasRole('ADMIN') or hasRole('BOARD_HEARING_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.boardhearing.msgs.boardHearing">
<form:form commandName="boardHearingForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="boardHearingTitle"/></legend>
		<span class="fieldGroup">
			<form:label path="paroleBoardItinerary" class="fieldLabel">
				<fmt:message key="paroleBoardLocationLabel"/>
			</form:label>
			<form:select path="paroleBoardItinerary">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${paroleBoardItineraries}" var="itinerary">
					<c:set var="startDate">
						<fmt:formatDate value="${itinerary.dateRange.startDate}" pattern="MM/dd/yyyy" />
					</c:set>
					<c:set var="endDate">
						<fmt:formatDate value="${itinerary.dateRange.endDate}" pattern="MM/dd/yyyy" />
					</c:set>
					<option value="${itinerary.id}" ${itinerary == boardHearingForm.paroleBoardItinerary ? 'selected="selected"' : ''}>
						<c:out value="${itinerary.paroleBoardLocation.location.organization.name} (${startDate} - ${endDate})" />
					</option>
				</c:forEach>
			</form:select>
			
			<c:forEach items="${paroleBoardItineraries}" var="itinerary">
				<input type="hidden" value="${itinerary.paroleBoardLocation.videoConferenceCapable}" id="videoConference${itinerary.id}" />
			</c:forEach>
			<form:errors path="paroleBoardItinerary" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="hearingDate" class="fieldLabel">
				<fmt:message key="hearingDateLabel"/>
			</form:label>
			<form:input path="hearingDate" class="date"/>
			<form:errors path="hearingDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="boardHearingCategory" class="fieldLabel">
				<fmt:message key="categoryLabel"/>
			</form:label>
			<form:select path="boardHearingCategory">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${categories}" var="category">
					<option value="${category.id}" ${category == boardHearingForm.boardHearingCategory ? 'selected="selected"' : ''}>
						<c:out value="${category.name}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="boardHearingCategory" cssClass="error"/>
		</span>
		<c:if test="${not boardHearingForm.videoConferenceCapable}">
			<c:set var="isHidden" value="hidden" />
		</c:if>
		<span class="fieldGroup ${isHidden}" id="videoConferenceFields">
			<span class="fieldGroup">
				<form:label path="videoConference" class="fieldLabel">
					<fmt:message key="videoConferenceLabel"/>
				</form:label>
				<form:checkbox path="videoConference" />
				<form:errors path="videoConference" cssClass="error"/>
			</span>
		</span>
		<span id="boardMembers">
			<span class="fieldGroup">
				<form:label path="boardMember1" class="fieldLabel">
					<fmt:message key="boardMember1Label"/>
				</form:label>
				<form:select path="boardMember1">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${boardAttendees}" var="attendee">
						<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
						<option value="${attendee.boardMember.id}" ${attendee.boardMember == boardHearingForm.boardMember1 ? 'selected="selected"' : ''}>
						<c:out value="${name.lastName}, ${name.firstName}"/>
					</option>
					</c:forEach>
				</form:select>
				<form:errors path="boardMember1" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="boardMember2" class="fieldLabel">
					<fmt:message key="boardMember2Label"/>
				</form:label>
				<form:select path="boardMember2">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${boardAttendees}" var="attendee">
						<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
						<option value="${attendee.boardMember.id}" ${attendee.boardMember == boardHearingForm.boardMember2 ? 'selected="selected"' : ''}>
						<c:out value="${name.lastName}, ${name.firstName}"/>
					</option>
					</c:forEach>
				</form:select>
				<form:errors path="boardMember2" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="boardMember3" class="fieldLabel">
					<fmt:message key="boardMember3Label"/>
				</form:label>
				<form:select path="boardMember3">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${boardAttendees}" var="attendee">
						<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
						<option value="${attendee.boardMember.id}" ${attendee.boardMember == boardHearingForm.boardMember3 ? 'selected="selected"' : ''}>
						<c:out value="${name.lastName}, ${name.firstName}"/>
					</option>
					</c:forEach>
				</form:select>
				<form:errors path="boardMember3" cssClass="error"/>
			</span>
		</span>
		<form:input path="boardHearingParticipant1" type="hidden" />
		<form:input path="boardHearingParticipant2" type="hidden" />
		<form:input path="boardHearingParticipant3" type="hidden" />
		<%--<span class="fieldGroup">
			<form:label path="cancelled" class="fieldLabel">
				<fmt:message key="cancelLabel"/>
			</form:label>
			<form:checkbox path="cancelled" />
			<form:errors path="cancelled" cssClass="error"/>
		</span>
		<c:if test="${not boardHearingForm.cancelled}">
			<c:set var="isHidden" value="hidden" />
		</c:if>
		<span class="fieldGroup ${isHidden}" id="reasonFields">
			<form:label path="reason" class="fieldLabel">
				<fmt:message key="reasonLabel"/>
			</form:label>
			<form:select path="reason">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${cancellationCategories}" var="cancellation">
					<option value="${cancellation}" ${cancellation == boardHearingForm.reason ? 'selected="selected"' : ''}>
						<fmt:message key="${cancellation}CategoryLabel"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="reason" cssClass="error"/>
		</span>  --%>
	</fieldset>
	<fieldset>
		<legend>
			<fmt:message key="notesLabel" />
		</legend>
		<span class="fieldGroup">
			<c:set var="boardHearingNoteItems" value="${boardHearingForm.boardHearingNoteItems}" scope="request"/>
			<jsp:include page="boardHearingNoteTable.jsp"/>
		</span>
	</fieldset>
	<c:if test="${not empty boardHearing}">
		<c:set var="updatable" value="${boardHearing}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editBoardHearing}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>