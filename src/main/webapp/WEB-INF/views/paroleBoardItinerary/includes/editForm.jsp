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
 - Version: 0.1.1 (Apr 18, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
<form:form commandName="paroleBoardItineraryForm" class="editForm">
<fieldset>
	<legend><fmt:message key="paroleBoardItineraryDetailsTitle"/></legend>
	
	<span class="fieldGroup">
		<form:label path="startDate" class="fieldLabel">
			<fmt:message key="startDateLabel"/></form:label>
		<form:input path="startDate" class="date"/> 
		<form:errors path="startDate" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="endDate" class="fieldLabel">
			<fmt:message key="endDateLabel"/></form:label>
		<form:input path="endDate" class="date"/> 
		<form:errors path="endDate" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="paroleBoardLocation" class="fieldLabel">
			<fmt:message key="inmateMeetingLocationLabel"/></form:label>
		<form:select path="paroleBoardLocation">
			<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
			<form:options items="${paroleBoardLocations}" itemLabel="location.organization.name" itemValue="id"/>
		</form:select>
		<form:errors path="paroleBoardLocation" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="onsite" class="fieldLabel">
			<fmt:message key="boardAttendanceLabel"/></form:label>
		<form:select path="onsite">
			<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
			<c:forEach items="${onsiteCategories}" var="onsite">
				<option value="${onsite}" ${onsite == paroleBoardItineraryForm.onsite ? 'selected="selected"' : ''}>
					<fmt:message key="onsiteCategoryLabel.${onsite}"/>
				</option>
			</c:forEach>
		</form:select>
		<form:errors path="paroleBoardLocation" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardMember1" class="fieldLabel">
			<fmt:message key="boardMember1Label"/></form:label>
		<c:set var="boardMember" value="${paroleBoardItineraryForm.boardMember1}" scope="request"/>
		<form:select path="boardMember1">
			<jsp:include page="/WEB-INF/views/paroleBoardItinerary/includes/boardMemberOptions.jsp"/>
		</form:select>
		<form:errors path="boardMember1" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardMember2" class="fieldLabel">
			<fmt:message key="boardMember2Label"/></form:label>
		<c:set var="boardMember" value="${paroleBoardItineraryForm.boardMember2}" scope="request"/>
		<form:select path="boardMember2">
			<jsp:include page="/WEB-INF/views/paroleBoardItinerary/includes/boardMemberOptions.jsp"/>
		</form:select>
		<form:errors path="boardMember2" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardMember3" class="fieldLabel">
			<fmt:message key="boardMember3Label"/></form:label>
		<c:set var="boardMember" value="${paroleBoardItineraryForm.boardMember3}" scope="request"/>
		<form:select path="boardMember3">
			<jsp:include page="/WEB-INF/views/paroleBoardItinerary/includes/boardMemberOptions.jsp"/>
		</form:select>
		<form:errors path="boardMember3" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="boardMemberAlternate" class="fieldLabel">
			<fmt:message key="boardMemberAlternateLabel"/></form:label>
		<c:set var="boardMember" value="${paroleBoardItineraryForm.boardMemberAlternate}" scope="request"/>
		<form:select path="boardMemberAlternate">
			<jsp:include page="/WEB-INF/views/paroleBoardItinerary/includes/boardMemberOptions.jsp"/>
		</form:select>
		<form:errors path="boardMemberAlternate" cssClass="error"/>
	</span>
</fieldset>
<fieldset id="boardMeetingSitesHolder">
	<legend><fmt:message key="boardMeetingSitesTitle"/></legend>
	<c:set var="boardMeetingSiteUnits" value="${boardMeetingSiteUnits}" scope="request"/>
	<jsp:include page="boardMeetingSitesTable.jsp"/>
	<form:errors path="boardMeetingSiteItems" cssClass="error"/>
</fieldset>
<fieldset id="boardItineraryNotesHolder">
	<legend><fmt:message key="boardItineraryNotesTitle"/></legend>
	<jsp:include page="boardItineraryNotesTable.jsp"/>
	<form:errors path="boardItineraryNoteItems" cssClass="error"/>
</fieldset>
<c:if test="${not empty paroleBoardItinerary}">
<c:set var="updatable" value="${paroleBoardItinerary}" scope="request"/>
<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
</c:if>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>