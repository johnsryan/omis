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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editViolationEvent" access="hasRole('VIOLATION_EVENT_EDIT') or hasRole('ADMIN') or hasRole('VIOLATION_EVENT_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.violationevent.msgs.violationEvent">
<form:form commandName="violationEventForm" class="editForm" enctype="multipart/form-data">
	<input type="hidden" value="${offender.id}" id="offenderId"/>
	<input type="hidden" value="${category}" id="category"/>
	<fieldset>
		<legend><fmt:message key="violationEventTitle"/></legend>
		<span class="fieldGroup">
			<label class="fieldLabel">
				<fmt:message key="violationCategoryLabel"/>
			</label>
			<c:out value="${categoryName}" />
		</span>
		
		<span class="fieldGroup">
			<label for="jurisdictionFilter" class="fieldLabel">
				<fmt:message key="jurisdictionFilterLabel"/>
			</label>
			<c:choose>
				<c:when test="${category eq 'DISCIPLINARY'}">
					<form:radiobutton path="jurisdictionFilter" id="facility" value="SECURE_FACILITY"/>
					<label for="facility" class="checkboxLabel">
						<fmt:message key="secureFacilityLabel"/>
					</label>
					<form:radiobutton path="jurisdictionFilter" id="treatmentCenter" value="TREATMENT_CENTER"/>
					<label for="jurisdictionFilter.treatmentCenter" class="checkboxLabel">
						<fmt:message key="treatmentCenterLabel"/>
					</label>
					<form:radiobutton path="jurisdictionFilter" id="preReleaseCenter" value="PRERELEASE_CENTER"/>
					<label for="jurisdictionFilter.preReleaseCenter" class="checkboxLabel">
						<fmt:message key="preReleaseCenterLabel"/>
					</label>
					<form:radiobutton path="jurisdictionFilter" id="assessmentSanctionRecovationCenter" value="ASSESSMENT_SANCTION_AND_RECOVATION_CENTER"/>
					<label for="jurisdictionFilter.assessmentSanctionRevocationCenter" class="checkboxLabel">
						<fmt:message key="assessmentCenterLabel"/>
					</label>
					<form:radiobutton path="jurisdictionFilter" id="communitySupervisionOffice" value="COMMUNITY_SUPERVISION_OFFICE"/>
					<label for="jurisdictionFilter.communitySupervisionOffice" class="checkboxLabel">
						<fmt:message key="communitySupervisionOfficeLabel"/>
					</label>
				</c:when>
				<c:when test="${category eq 'SUPERVISION'}">
					<form:radiobutton path="jurisdictionFilter" id="communitySupervisionOffice" value="COMMUNITY_SUPERVISION_OFFICE" checked="checked"/>
					<label for="jurisdictionFilter.communitySupervisionOffice" class="checkboxLabel">
						<fmt:message key="communitySupervisionOfficeLabel"/>
					</label>
				</c:when>
			</c:choose>
		</span>
		
		<span class="fieldGroup">
			<form:label path="jurisdiction" class="fieldLabel">
				<fmt:message key="jurisdictionLabel"/>
			</form:label>
			<form:select path="jurisdiction">
				<c:set value="${violationEventForm.jurisdiction}" var="jurisdiction" scope="request"/>
				<jsp:include page="jurisdictionOptions.jsp"/>
			</form:select>
			<form:errors path="jurisdiction" cssClass="error"/>
		</span>
	
		<span class="fieldGroup">
			<form:label path="eventDate" class="fieldLabel">
				<fmt:message key="dateOfEventLabel"/>
			</form:label>
			<form:input path="eventDate" value="${eventDateParameter}" cssClass="date" />
			<form:errors path="eventDate" cssClass="error"/>
		</span>
			
		<span class="fieldGroup">
			<form:label path="unit" class="fieldLabel">
				<fmt:message key="unitLabel"/>
			</form:label>
			<form:select path="unit">
				<c:set value="${violationEventForm.unit}" var="unit" scope="request"/>
				<jsp:include page="unitOptions.jsp"/>
			</form:select>
			<form:errors path="unit" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="eventDetails" class="fieldLabel">
				<fmt:message key="eventDetailsLabel"/>
			</form:label>
			<form:textarea path="eventDetails" maxlength="32760"/>
			<span class="eventDetailsCounter" id="eventDetailsCharacterCounter"></span>
			<form:errors path="eventDetails" cssClass="error"/>
		</span>
		
	</fieldset>
	<fieldset>
		<legend><fmt:message key="violationsTitle"/></legend>
		<c:choose>
			<c:when test="${category eq 'DISCIPLINARY'}">
				<fieldset>
					<span class="tipText">
						<fmt:message key="infractionTipText"/>
					</span>
					<span class="fieldGroup">
						<c:set var="disciplinaryCodeViolationItems" value="${violationEventForm.disciplinaryCodeViolationItems}" scope="request"/>
						<jsp:include page="disciplinaryCodeViolationTable.jsp"/>
					</span>
				</fieldset>
			</c:when>
			
			<c:when test="${category eq 'SUPERVISION'}">
				<fieldset>
					<span class="tipText">
						<fmt:message key="conditionsTipText"/>
					</span>
					<span class="fieldGroup">
						<c:set var="conditionViolationItems" value="${violationEventForm.conditionViolationItems}" scope="request"/>
						<jsp:include page="conditionViolationTable.jsp"/>
					</span>
				</fieldset>
			</c:when>
		</c:choose>
		
	</fieldset>
	<fieldset>
		<legend><fmt:message key="notesTitle"/></legend>
		<span class="fieldGroup">
			<c:set var="violationEventNoteItems" value="${violationEventForm.violationEventNoteItems}" scope="request"/>
			<jsp:include page="violationEventNoteTable.jsp"/>
		</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="documentsTitle"/></legend>
		<span class="fieldGroup">
			<c:set var="violationEventDocumentItems" value="${violationEventForm.violationEventDocumentItems}" scope="request"/>
			<jsp:include page="violationEventDocumentItems.jsp"/>
		</span>
	</fieldset>
	
	
	<c:if test="${not empty violationEvent}">
		<c:set var="updatable" value="${violationEvent}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editViolationEvent}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>