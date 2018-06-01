<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editWarrant" access="hasRole('WARRANT_CREATE') or hasRole('WARRANT_EDIT') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
<form:form commandName="warrantReleaseForm" class="editForm">
	<fieldset>
		<span class="fieldGroup">
			<form:label path="addressee" class="fieldLabel">
				<fmt:message key="holdingAgencyFacilityLabel"/>
			</form:label>
			<form:input path="addressee"/>
			<form:errors path="addressee" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="releaseDate" class="fieldLabel">
				<fmt:message key="releaseDateLabel"/>
			</form:label>
			<form:input path="releaseDate" class="date"/>
			<form:errors path="releaseDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="instructions" class="fieldLabel">
				<fmt:message key="instructionsLabel"/>
			</form:label>
			<form:textarea path="instructions"/>
			<form:errors path="instructions" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="clearedBy" class="fieldLabel">
				<fmt:message key="clearedByLabel"/>
			</form:label>
			<input id="clearedByInput"/>
			<form:hidden id="clearedBy" path="clearedBy"/>
			<a id="currentClearedBy" class="currentUserAccountLink"></a>
			<a id="clearClearedBy" class="clearLink"></a>
			<span id="clearedByDisplay">
				<c:if test="${not empty warrantReleaseForm.clearedBy}" >
					<c:out value="${warrantReleaseForm.clearedBy.name.lastName}, ${warrantReleaseForm.clearedBy.name.firstName}"/>
				</c:if>
			</span>
			<form:errors path="clearedBy" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="clearedByDate" class="fieldLabel">
				<fmt:message key="clearedDateLabel"/>
			</form:label>
			<form:input path="clearedByDate" class="date"/>
			<form:errors path="clearedByDate" cssClass="error"/>
		</span>
	</fieldset>
	
	<c:if test="${not empty warrantRelease}">
		<c:set var="updatable" value="${warrantRelease}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editWarrant}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>