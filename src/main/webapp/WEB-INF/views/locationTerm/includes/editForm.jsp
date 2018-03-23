<%--
 - Edit for for location term.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.location.msgs.location" var="locationBundle"/>
<fmt:setBundle basename="omis.locationterm.msgs.locationTerm" var="locationTermBundle"/>
<fmt:setBundle basename="omis.locationterm.msgs.locationReasonTerm" var="locationReasonTermBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.region.msgs.state" var="stateBundle"/>
<form:form commandName="locationTermForm" class="editForm">
	<fieldset>
		<c:if test="${not empty locationTerm and locationTerm.locked}">
			<p class="message"><fmt:message key="locationTermLockedMessage" bundle="${locationTermBundle}"/></p>
		</c:if>
		<c:if test="${empty locationTerm and empty placementTerm}">
			<span class="message" id="offenderNotPlacedMessage">
				<fmt:message key="locationTermNotAllowedOffenderNotPlacedOnStartDateMessage" bundle="${locationTermBundle}"/>
				<a href="${pageContext.request.contextPath}/supervision/placementTerm/create.html?offender=${offenderSummary.id}&amp;allowCorrectionalStatusChange=true"><fmt:message key="clickHereToPlaceOffenderLink" bundle="${locationTermBundle}"/></a>
			</span>
		</c:if>
		<form:hidden path="allowState"/>
		<c:if test="${locationTermForm.allowState}">
			<span class="fieldGroup">
				<form:label path="state" class="fieldLabel">
					<fmt:message key="stateLabel" bundle="${stateBundle}"/></form:label>
				<form:select path="state">
					<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
					<form:options itemValue="id" itemLabel="name" items="${states}"/>
				</form:select>
				<form:errors path="state" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowLocation"/>
		<c:choose>
			<c:when test="${locationTermForm.allowLocation}">
				<span class="fieldGroup">
					<form:label path="location" class="fieldLabel">
						<fmt:message key="locationLabel" bundle="${locationBundle}"/></form:label>
					<form:select path="location">
						<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>			
						<form:options itemValue="id" itemLabel="organization.name" items="${locations}"/>
					</form:select>
					<form:errors path="location" cssClass="error"/>
				</span>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty locationTerm}">
					<span class="fieldGroup">
						<form:label path="location" class="fieldLabel">
							<fmt:message key="locationLabel" bundle="${locationBundle}"/></form:label>
						<span class="fieldValueLabel"><c:out value="${locationTerm.location.organization.name}"/></span>
					</span>
					<form:hidden path="location"/>
				</c:if>
			</c:otherwise>
		</c:choose>
		<form:hidden path="allowMultipleReasonTerms"/>
		<form:hidden path="allowSingleReasonTerm"/>
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
		<c:if test="${locationTermForm.allowSingleReasonTerm}">
			<span class="fieldGroup">
				<form:label path="reason" class="fieldLabel">
					<fmt:message key="locationReasonLabel" bundle="${locationReasonTermBundle}"/></form:label>
				<form:select path="reason" disabled="${locationTermForm.associateMultipleReasonTerms}">
					<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
					<form:options itemValue="id" itemLabel="name" items="${reasons}"/>
				</form:select>
				<form:errors path="reason" cssClass="error"/>
				<c:if test="${locationTermForm.allowMultipleReasonTerms}">
					<form:label path="associateMultipleReasonTerms">
						<fmt:message key="associateMultipleReasonTermsLabel" bundle="${locationTermBundle}"/>
					</form:label>
					<form:checkbox path="associateMultipleReasonTerms" id="associateMultipleReasonTerms"/>
				</c:if>
			</span>
		</c:if>
		<c:if test="${not locationTermForm.allowMultipleReasonTerms or not locationTermForm.allowSingleReasonTerm}">
			<form:hidden path="associateMultipleReasonTerms"/>
		</c:if>
		<c:if test="${locationTermForm.allowMultipleReasonTerms}">
			<c:choose>
				<c:when test="${locationTermForm.associateMultipleReasonTerms}">
					<c:set var="reasonTermsContainerClass" value=""/>
				</c:when>
				<c:otherwise>
					<c:set var="reasonTermsContainerClass" value="hidden"/>
				</c:otherwise>
			</c:choose>
			<div id="reasonTermsContainer" class="${reasonTermsContainerClass}">
				<table id="reasonTermsBody" class="editTable">
					<thead>
						<tr>
							<th class="operations"><a href="${pageContext.request.contextPath}/locationTerm/reasonTermsActionMenu.html" class="actionMenuItem" id="locationReasonTermsActionMenuLink"></a></th>
							<th><fmt:message key="locationReasonLabel" bundle="${locationReasonTermBundle}"/></th>
							<th><fmt:message key="startDateLabel" bundle="${commonBundle}"/></th>
							<th><fmt:message key="startTimeLabel" bundle="${commonBundle}"/></th>
							<th><fmt:message key="endDateLabel" bundle="${commonBundle}"/></th>
							<th><fmt:message key="endTimeLabel" bundle="${commonBundle}"/></th>
						</tr>
					</thead>
					<tbody id="locationReasonTerms">
						<c:forEach var="locationReasonTermItem" items="${locationTermForm.reasonTermItems}" varStatus="status">
							<c:if test="${not empty locationReasonTermItem.operation}">
								<c:set var="locationReasonTermItem" value="${locationReasonTermItem}" scope="request"/>
								<c:set var="locationReasonTermItemIndex" value="${status.index}" scope="request"/>
								<jsp:include page="reasonTermEditTableRow.jsp"/>
							</c:if>
						</c:forEach>
						<form:errors path="reasonTermItems" cssClass="error"/>
					</tbody>
				</table>
			</div>
		</c:if>
	</fieldset>
	<c:if test="${not empty locationTerm}">
	<c:set var="updatable" value="${locationTerm}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${empty locationTerm or not locationTerm.locked}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>