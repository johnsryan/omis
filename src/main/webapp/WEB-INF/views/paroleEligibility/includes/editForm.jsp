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
 - Author: Trevor Isles
 - Author: Annie Wahl
 - Date: May 23, 2018
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleeligibility.msgs.paroleEligibility">
<form:form commandName="paroleEligibilityForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="paroleEligibilityLegend"/></legend>
		
		<span class="fieldGroup">
			<form:label path="hearingEligibilityDate" class="fieldLabel">
				<fmt:message key="hearingEligibilityDateLabel"/></form:label>
			<form:input path="hearingEligibilityDate" class="date"/>
			<form:errors path="hearingEligibilityDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="appearanceCategory" class="fieldLabel">
				<fmt:message key="appearanceCategoryLabel"/></form:label>
			<form:select path="appearanceCategory">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${appearanceCategories}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors path="appearanceCategory" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="eligibilityStatusCategory" class="fieldLabel">
				<fmt:message key="paroleEligibilityStatusLabel"/></form:label>
			<form:select path="eligibilityStatusCategory">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${eligibilityStatuses}" var="eligibilityStatus">
					<form:option value="${eligibilityStatus}">
						<fmt:message key="paroleEligibilityStatusLabel.${eligibilityStatus.name}"/>
					</form:option>
				</c:forEach>
			</form:select>
			<form:errors path="eligibilityStatusCategory" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="paroleEligibilityStatusDate" class="fieldLabel">
				<fmt:message key="paroleEligibilityStatusDateLabel"/></form:label>
			<form:input path="paroleEligibilityStatusDate" class="date"/>
			<form:errors path="paroleEligibilityStatusDate" cssClass="error"/>
		</span>
	<c:if test="${!paroleEligibilityForm.showStatusFields}">
		<c:set var="statusFieldsClassList" value="hidden" />	
	</c:if>
	<form:input path="showStatusFields" type="hidden"/>
	
	<div id="statusFields" class="${statusFieldsClassList}">
		<span class="fieldGroup">
			<form:label path="eligibilityStatusReason" class="fieldLabel">
				<fmt:message key="eligibilityStatusReasonLabel"/></form:label>
			<form:select path="eligibilityStatusReason">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${statusReasons}" itemValue="id" itemLabel="name"/>
			</form:select>
			<form:errors path="eligibilityStatusReason" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="statusReasonComment" class="fieldLabel">
				<fmt:message key="statusReasonCommentLabel"/></form:label>
			<form:textarea path="statusReasonComment"/>
			<form:errors path="statusReasonComment" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="reviewDate" class="fieldLabel">
				<fmt:message key="reviewDateLabel"/></form:label>
			<form:input path="reviewDate" class="date"/>
			<form:errors path="reviewDate" cssClass="error"/>
		</span>
	</div>
	
	</fieldset>
	<fieldset>
		<legend><fmt:message key="offenderEligibilityNotesLabel"/></legend>
			<c:set var="paroleEligibilityNoteItems" value="${paroleEligibilityForm.paroleEligibilityNoteItems}" scope="request"/>
			<jsp:include page="paroleEligibilityNotesTable.jsp"/>
	</fieldset>
	<c:if test="${not empty paroleEligibility}">
		<c:set var="updatable" value="${paroleEligibility}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
	</p>
</form:form>
</fmt:bundle>