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
 - Form to edit location terms.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.locationterm.msgs.locationTerm" var="locationTermBundle"/>
<fmt:setBundle basename="omis.locationterm.msgs.locationReason" var="locationReasonBundle"/>
<form:form commandName="locationReasonTermForm" class="editForm">
	<fieldset>
		<span class="fieldGroup">
			<form:label path="locationTerm" class="fieldLabel">
				<fmt:message key="locationTermLabel" bundle="${locationTermBundle}"/></form:label>
			<form:select path="locationTerm">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<c:forEach var="locationTerm" items="${locationTerms}">
					<c:choose>
						<c:when test="${locationReasonTermForm.locationTerm eq locationTerm}">
							<form:option value="${locationTerm.id}" selected="selected">
								<c:out value="${locationTerm.location.organization.name}"/>
								<fmt:formatDate value="${locationTerm.dateRange.startDate}" pattern="MM/dd/yyyy"/>
								<c:if test="${not empty locationTerm.dateRange.endDate}">
								- <fmt:formatDate value="${locationTerm.dateRange.endDate}" pattern="MM/dd/yyyy"/>
								</c:if>
							</form:option>
						</c:when>
						<c:otherwise>
							<form:option value="${locationTerm.id}">
								<c:out value="${locationTerm.location.organization.name}"/>
								<fmt:formatDate value="${locationTerm.dateRange.startDate}" pattern="MM/dd/yyyy"/>
								<c:if test="${not empty locationTerm.dateRange.endDate}">
								- <fmt:formatDate value="${locationTerm.dateRange.endDate}" pattern="MM/dd/yyyy"/>
								</c:if>
							</form:option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</form:select>
			<form:errors path="locationTerm" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="reason" class="fieldLabel">
				<fmt:message key="reasonLabel" bundle="${locationReasonBundle}"/></form:label>
			<form:select path="reason">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${reasons}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors path="reason" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="startDate" class="fieldLabel">
				<fmt:message key="startDateLabel" bundle="${commonBundle}"/></form:label>
			<form:input path="startDate" class="date"/>
			<form:errors path="startDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="startTime" class="fieldLabel">
				<fmt:message key="startTimeLabel" bundle="${commonBundle}"/></form:label>
			<form:input path="startTime" class="time"/>
			<form:errors path="startTime" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endDate" class="fieldLabel">
				<fmt:message key="endDateLabel" bundle="${commonBundle}"/></form:label>
			<form:input path="endDate" class="date"/>
			<form:errors path="endDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="endTime" class="fieldLabel">
				<fmt:message key="endTimeLabel" bundle="${commonBundle}"/></form:label>
			<form:input path="endTime" class="time"/>
			<form:errors path="endTime" cssClass="error"/>
		</span>
	</fieldset>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
	</p>
</form:form>