<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editWarrant" access="hasRole('WARRANT_CREATE') or hasRole('WARRANT_EDIT') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
<form:form commandName="warrantForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="warrantDetailsFieldsetLegendLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="date" class="fieldLabel">
				<fmt:message key="dateLabel"/>
			</form:label>
			<form:input path="date" class="date"/>
			<form:errors path="date" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="addressee" class="fieldLabel">
				<fmt:message key="addressedToLabel"/>
			</form:label>
			<form:input path="addressee"/>
			<form:errors path="addressee" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="issuedBy" class="fieldLabel">
				<fmt:message key="issuedByLabel"/>
			</form:label>
			<input type="text" id="issuedByInput"/>
			<form:hidden id="issuedBy" path="issuedBy"/>
			<a id="currentIssuedBy" class="currentUserAccountLink"></a>
			<a id="clearIssuedBy" class="clearLink"></a>
			<span id="issuedByDisplay">
				<c:if test="${not empty warrantForm.issuedBy}" >
					<c:out value="${warrantForm.issuedBy.name.lastName}, ${warrantForm.issuedBy.name.firstName}"/>
				</c:if>
			</span>
			<form:errors path="issuedBy" cssClass="error"/>
		</span>
		<span class="fieldGroup">
				<form:label path="holdingJail" class="fieldLabel">
					<fmt:message key="holdingJailLabel"/>
				</form:label>
				<form:select path="holdingJail">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${jails}" var="j">
						<option value="${j.id}" ${j == warrantForm.holdingJail ? 'selected="selected"' : ''}>
							<c:out value="${j.name}"/>
						</option>
					</c:forEach>
				</form:select>
				<form:errors path="holdingJail" cssClass="error"/>
			</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="arrestFieldsetLegendLabel"/></legend>
		<!-- arrest -->
		<span class="fieldGroup">
			<form:label path="arrested" class="fieldLabel">
				<fmt:message key="arrestLabel"/>
			</form:label>
		<c:choose>
			<c:when test="${not empty warrantArrest}">
				<input type="checkbox" id="arrested" disabled checked/>
				<form:input type="hidden" path="arrested" />
			</c:when>
			<c:otherwise>
				<form:checkbox id="arrested" path="arrested" />
			</c:otherwise>
		</c:choose>
			<form:errors path="arrested" cssClass="error"/>
		</span>
		<c:set var="display" value="${warrantForm.arrested ? 'block' : 'none'}" />
		<div id="arrestedFields" style="display: ${display};">
			<span class="fieldGroup">
				<form:label path="arrestDate" class="fieldLabel">
					<fmt:message key="dateLabel"/>
				</form:label>
				<form:input path="arrestDate" class="date"/>
				<form:errors path="arrestDate" cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="arrestJail" class="fieldLabel">
					<fmt:message key="jailLabel"/>
				</form:label>
				<form:select path="arrestJail">
					<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${jails}" var="j">
						<option value="${j.id}" ${j == warrantForm.arrestJail ? 'selected="selected"' : ''}>
							<c:out value="${j.name}"/>
						</option>
					</c:forEach>
				</form:select>
				<form:errors path="arrestJail" cssClass="error"/>
			</span>
			<span class="fieldGroup hidden">
				<form:label path="determinationDeadline" class="fieldLabel">
					<fmt:message key="determinationDeadlineLabel"/>
				</form:label>
				<form:input path="determinationDeadline" class="date"/>
				<form:errors path="determinationDeadline" cssClass="error"/>
			</span>
		</div>
	</fieldset>
	<c:choose>
		<c:when test="${warrantReasonCategory eq 'AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER'}">
			<fieldset>
				<legend><fmt:message key="bondFieldsetLegendLabel"/></legend>
				<!-- Bond options -->
				<form:errors path="bondRecommended" cssClass="error"/>
				<span class="fieldGroup">
					<form:radiobutton path="bondRecommended" name="bondRecommended" value="true" class="fieldOption" />
					<label for="bondRecommended" class="fieldValueLabel">
						<fmt:message key="bondRecommendationLabel"/>
					</label>
					<form:input path="bondRecommendation"/>
					<form:errors path="bondRecommendation" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:radiobutton  path="bondRecommended" name="noBond" value="false" class="fieldOption"/>
					<label for="noBond" class="fieldValueLabel">
						<fmt:message key="noBondLabel"/>
					</label>
				</span>
			</fieldset>
		</c:when>
		<c:otherwise>
			<form:input type="hidden" path="bondRecommended" />
		</c:otherwise>
	</c:choose>
	<fieldset>
		<legend><fmt:message key="warrantCauseFieldsetLegendLabel"/></legend>
		<!-- causeviolations -->
		<span class="fieldGroup">
			<c:set var="warrantCauseViolationItems" value="${warrantForm.warrantCauseViolationItems}" scope="request"/>
			<jsp:include page="warrantCauseViolationTable.jsp"/>
		</span>
	</fieldset>
	
	
	
	
	
	<fieldset>
		<legend><fmt:message key="warrantNotesFieldsetLegendLabel"/></legend>
		<!-- notes -->
		<span class="fieldGroup">
			<c:set var="warrantNoteItems" value="${warrantForm.warrantNoteItems}" scope="request"/>
			<jsp:include page="warrantNoteTable.jsp"/>
		</span>
	</fieldset>
	
	
	<c:if test="${not empty warrant}">
		<c:set var="updatable" value="${warrant}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editWarrant}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>