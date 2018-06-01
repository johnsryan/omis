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
<sec:authorize var="editHearing" access="hasRole('HEARING_EDIT') or hasRole('ADMIN') or hasRole('HEARING_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<form:form commandName="hearingForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="hearingTitle"/></legend>
		<span class="fieldGroup">
			<form:label path="category" class="fieldLabel">
				<fmt:message key="hearingTypeLabel"/>
			</form:label>
			<c:choose>
				<c:when test="${not empty hearingForm.infractionItems}">
					<form:input type="hidden" path="category" />
					<input type="text" value="<fmt:message key="${hearingForm.category}CategoryLabel"/>"
					 readonly="readonly" />
				</c:when>
				<c:otherwise>
					<form:select path="category">
						<jsp:include page="../../includes/nullOption.jsp"/>
						<c:forEach items="${categories}" var="cat">
							<option value="${cat}" ${cat == hearingForm.category ? 'selected="selected"' : ''}>
								<fmt:message key="${cat}CategoryLabel"/>
							</option>
						</c:forEach>
					</form:select>
				</c:otherwise>
			</c:choose>
			<form:errors path="category" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="locationType" class="fieldLabel">
				<fmt:message key="locationTypeLabel"/>
			</form:label>
			<form:select path="locationType">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${locationTypes}" var="locType">
					<option value="${locType}" ${locType == hearingForm.locationType ? 'selected="selected"' : ''}>
						<fmt:message key="${locType}LocationTypeLabel"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="locationType" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="location" class="fieldLabel">
				<fmt:message key="locationLabel"/>
			</form:label>
			<form:select path="location">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${locations}" var="loc">
					<option value="${loc.id}" ${loc == hearingForm.location ? 'selected="selected"' : ''} >
						<c:out value="${loc.organization.name}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="location" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="date" class="fieldLabel">
				<fmt:message key="dateLabel"/>
			</form:label>
			<form:input path="date" cssClass="date" />
			<form:errors path="date" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="time" class="fieldLabel">
				<fmt:message key="timeLabel"/>
			</form:label>
			<form:input path="time" cssClass="time" />
			<form:errors path="time" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="status" class="fieldLabel">
				<fmt:message key="statusLabel"/>
			</form:label>
			<form:select path="status">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${statuses}" var="stat">
					<option value="${stat}" ${stat == hearingForm.status ? 'selected="selected"' : ''}>
						<fmt:message key="${stat}StatusLabel"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="status" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<form:label path="officer" class="fieldLabel">
				<fmt:message key="hearingOfficerLabel"/>
			</form:label>
			<input id="officerInput"/>
				<form:hidden id="officer" path="officer"/>
				<a id="currentOfficerUser" class="currentUserAccountLink"></a>
				<a id="clearOfficer" class="clearLink"></a>
			<span id="officerDisplay">
				<c:if test="${not empty hearingForm.officer}" >
					<c:out value="${hearingForm.officer.user.name.lastName}, ${hearingForm.officer.user.name.firstName} (${hearingForm.officer.username})"/>
				</c:if>
			</span>
			<form:errors path="officer" cssClass="error"/>
		</span>
	</fieldset>
	
	<fieldset>
		<legend>
			<fmt:message key="infractionsLabel" />
		</legend>
		<span class="fieldGroup">
			<c:set var="infractionItems" value="${hearingForm.infractionItems}" scope="request"/>
			<jsp:include page="infractionItemTable.jsp"/>
		</span>
		
	</fieldset>
	
	
	<!-- Hearing Note Items -->
	
	<fieldset>
		<legend>
			<fmt:message key="notesLabel" />
		</legend>
		<c:set var="hearingNoteItems" value="${hearingForm.hearingNoteItems}" scope="request"/>
		<jsp:include page="hearingNoteTable.jsp"/>
	</fieldset>
		
	
	<c:if test="${not empty hearing}">
		<c:set var="updatable" value="${hearing}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editHearing}">
		<p class="buttons">
			<input id="saveAndAdjudicate" name="adjudicate" type="submit" value="<fmt:message key='saveAndAdjudicateLabel'/>"/>
			<input id="saveAndSelectViolations" name="selectViolations" type="submit" value="<fmt:message key='saveAndSelectViolationsLabel'/>"/>
			<input id="save" name="save" type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
	
	
</form:form>
</fmt:bundle>