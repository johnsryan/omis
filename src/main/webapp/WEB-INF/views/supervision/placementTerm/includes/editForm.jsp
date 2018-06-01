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
 - Form to edit placement terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.region.msgs.state" var="stateBundle"/>
<fmt:bundle basename="omis.supervision.msgs.placementTerm">
<form:form commandName="placementTermForm" class="editForm">
	<fieldset>
		<c:if test="${not empty placementTerm and placementTerm.locked}">
			<p class="message"><fmt:message key="placementTermLockedMessage"/></p>
		</c:if>
		<form:hidden path="allowCorrectionalStatus"/>
		<span class="fieldGroup">
			<form:label path="correctionalStatus" class="fieldLabel">
				<fmt:message key="correctionalStatusLabel"/></form:label>
			<c:choose>
				<c:when test="${placementTermForm.allowCorrectionalStatus}">
					<form:select id="correctionalStatus" path="correctionalStatus">
						<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
						<form:options itemValue="id" itemLabel="name" items="${correctionalStatuses}"/>
					</form:select>
				</c:when>
				<c:otherwise>
					<form:hidden path="correctionalStatus"/>
					<span><c:out value="${placementTermForm.correctionalStatus.name}"/></span>
				</c:otherwise>
			</c:choose>
			<form:errors path="correctionalStatus" cssClass="error"/>
		</span>
		<form:hidden path="allowState"/>
		<c:if test="${placementTermForm.allowState or not empty placementTermForm.state}">
			<span class="fieldGroup">
				<form:label path="state" class="fieldLabel">
					<fmt:message key="stateLabel" bundle="${stateBundle}"/></form:label>
				<c:choose>
					<c:when test="${placementTermForm.allowState}">
						<form:select id="state" path="state">
							<form:option value=""><fmt:message key="allStatesLabel" bundle="${stateBundle}"/></form:option>
							<form:options itemValue="id" itemLabel="name" items="${states}"/>
						</form:select>
					</c:when>
					<c:otherwise>
						<form:hidden path="state"/>
						<span><c:out value="${placementTermForm.state.name}"/></span>
					</c:otherwise>
				</c:choose>
				<form:errors path="state" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowSupervisoryOrganization"/>
		<c:if test="${placementTermForm.allowSupervisoryOrganization or not empty placementTermForm.supervisoryOrganization}">
			<span class="fieldGroup">
				<form:label path="supervisoryOrganization" class="fieldLabel">
					<fmt:message key="supervisoryOrganizationLabel"/></form:label>
				<c:choose>
					<c:when test="${placementTermForm.allowSupervisoryOrganization}">
						<form:select id="supervisoryOrganization" path="supervisoryOrganization">
							<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
							<form:options itemValue="id" itemLabel="name" items="${supervisoryOrganizations}"/>
						</form:select>
					</c:when>
					<c:otherwise>
						<form:hidden path="supervisoryOrganization"/>
						<span><c:out value="${placementTermForm.supervisoryOrganization.name}"/></span>
					</c:otherwise>
				</c:choose>		
				<form:errors path="supervisoryOrganization" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowStartDate"/>
		<span class="fieldGroup">
			<form:label path="startDate" class="fieldLabel">
				<fmt:message key="startDateLabel"/></form:label>
			<c:choose>
				<c:when test="${placementTermForm.allowStartDate}">
					<form:input path="startDate" class="date"/>
				</c:when>
				<c:otherwise>
					<form:hidden path="startDate"/>
					<span><fmt:formatDate value="${placementTermForm.startDate}" pattern="MM/dd/yyyy"/></span>
				</c:otherwise>
			</c:choose>
			<form:errors path="startDate" cssClass="error"/>
		</span>
		<form:hidden path="allowStartTime"/>
		<span class="fieldGroup">
			<form:label path="startTime" class="fieldLabel">
				<fmt:message key="startTimeLabel" bundle="${commonBundle}"/></form:label>
			<c:choose>
				<c:when test="${placementTermForm.allowStartTime}">
					<form:input path="startTime" class="time"/>
				</c:when>
				<c:otherwise>
					<form:hidden path="startTime"/>
					<span><fmt:formatDate value="${placementTermForm.startTime}" pattern="h:mm a"/></span>
				</c:otherwise>
			</c:choose>
			<form:errors path="startTime" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="startChangeReason" class="fieldLabel">
				<fmt:message key="startChangeReasonLabel"/></form:label>
			<form:select path="startChangeReason">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${startChangeReasons}" itemLabel="name" itemValue="id"/>
			</form:select>
			<form:errors path="startChangeReason" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endDate" class="fieldLabel">
				<fmt:message key="endDateLabel"/></form:label>
			<form:input path="endDate" class="date"/>
			<form:errors path="endDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endTime" class="fieldLabel">
				<fmt:message key="endTimeLabel" bundle="${commonBundle}"/></form:label>
			<form:input path="endTime" class="time"/>
			<form:errors path="endTime" cssClass="error"/>
		</span>
		<form:hidden path="allowEndChangeReason"/>
		<c:if test="${placementTermForm.allowEndChangeReason}">
			<span class="fieldGroup">
				<form:label path="endChangeReason" class="fieldLabel">
					<fmt:message key="endChangeReasonLabel"/></form:label>
				<form:select path="endChangeReason">
					<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
					<form:options items="${endChangeReasons}" itemLabel="name" itemValue="id"/>
				</form:select>
				<form:errors path="endChangeReason" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowStatusFields"/>
		<c:if test="${placementTermForm.allowStatusFields}">
			<span class="fieldGroup">
				<form:label path="status" class="fieldLabel">
					<fmt:message key="statusLabel"/></form:label>
				<form:select path="status" id="status">
					<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
					<c:forEach var="placementStatus" items="${placementStatuses}">
						<form:option value="${placementStatus.name}"><fmt:message key="placementStatusLabel.${placementStatus.name}"/></form:option>
					</c:forEach>
				</form:select>
				<form:errors path="status" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="statusDate" class="fieldLabel">
					<fmt:message key="statusDateLabel"/></form:label>
				<form:input path="statusDate" id="statusDate" class="date" disabled="${empty placementTermForm.status or placementTermForm.status.name eq 'PLACED'}"/>
				<form:errors path="statusDate" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="statusTime" class="fieldLabel">
					<fmt:message key="statusTimeLabel"/></form:label>
				<form:input path="statusTime" id="statusTime" class="time" disabled="${empty placementTermForm.status or placementTermForm.status eq 'PLACED'}"/>
				<form:errors path="statusTime" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="returned" class="fieldLabel">
					<fmt:message key="returnedLabel"/></form:label>
				<form:checkbox path="returned" id="returned" disabled="${empty placementTermForm.status or placementTermForm.status.name eq 'PLACED'}"/>
				<form:errors path="returned" cssClass="returned"/>
			</span>
				<span class="fieldGroup">
				<form:label path="statusReturnedDate" class="fieldLabel">
					<fmt:message key="statusReturnedDateLabel"/></form:label>
				<form:input path="statusReturnedDate" id="statusReturnedDate" class="date" disabled="${not placementTermForm.returned}"/>
				<form:errors path="statusReturnedDate" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="statusReturnedTime" class="fieldLabel">
					<fmt:message key="statusReturnedTimeLabel"/></form:label>
				<form:input path="statusReturnedTime" id="statusReturnedTime" class="time" disabled="${not placementTermForm.returned}"/>
				<form:errors path="statusReturnedTime" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowSendToLocation"/>
		<c:if test="${placementTermForm.allowSendToLocation}">
			<c:choose>
				<c:when test="${placementTermForm.correctionalStatus.locationRequired}">
					<c:set var="sendToLocationGroupClass" value="fieldGroup"/>
				</c:when>
				<c:otherwise>
					<c:set var="sendToLocationGroupClass" value="fieldGroup hidden"/>
				</c:otherwise>
			</c:choose>
			<span id="sendToLocationGroup" class="${sendToLocationGroupClass}">
				<form:label path="sendToLocation" class="fieldLabel">
					<fmt:message key="sendToLocationLabel"/></form:label>
				<form:checkbox path="sendToLocation"/>
				<form:errors path="sendToLocation" cssClass="error"/>
			</span>
		</c:if>
	</fieldset>
	<c:if test="${not empty placementTerm}">
	<c:set var="updatable" value="${placementTerm}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<fieldset>
		<legend><fmt:message key="placementTermNotesLabel"/></legend>
		<table class="editTable">
			<thead>
				<tr>
					<th class="operations"><a id="placementTermNoteItemsActionMenu" class="actionMenuItem" href="${pageContext.request.contextPath}/supervision/placementTerm/placementTermNoteItemsActionMenu.html"></a></th>
					<th><fmt:message key="placementTermNoteDateLabel"/></th>
					<th><fmt:message key="placementTermNoteValueLabel"/></th>
					<th><fmt:message key="placementTermNoteUpdateSignatureLabel"/></th>
				</tr>
			</thead>
			<tbody id="placementTermNotesBody">
				<c:forEach var="placementTermNoteItem" items="${placementTermForm.noteItems}" varStatus="status">
					<c:set var="placementTermNoteIndex" value="${status.index}" scope="request"/>
					<c:set var="placementTermNoteItem" value="${placementTermNoteItem}" scope="request"/>
					<jsp:include page="placementTermNoteTableRow.jsp"/>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<c:if test="${empty placementTerm or not placementTerm.locked}">
		<p class="buttons">
			<input id="saveButton" type="submit" value="<fmt:message key='saveLabel' bundle="${commonBundle}"/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>