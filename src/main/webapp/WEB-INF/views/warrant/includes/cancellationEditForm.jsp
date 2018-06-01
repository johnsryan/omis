<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editWarrant" access="hasRole('WARRANT_CREATE') or hasRole('WARRANT_EDIT') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
<form:form commandName="warrantCancellationForm" class="editForm">
	<fieldset>
		<span class="fieldGroup">
			<form:label path="date" class="fieldLabel">
				<fmt:message key="dateLabel"/>
			</form:label>
			<form:input path="date" class="date"/>
			<form:errors path="date" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="comment" class="fieldLabel">
				<fmt:message key="commentsLabel"/>
			</form:label>
			<form:textarea path="comment"/>
			<form:errors path="comment" cssClass="error"/>
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
				<c:if test="${not empty warrantCancellationForm.clearedBy}" >
					<c:out value="${warrantCancellationForm.clearedBy.name.lastName}, ${warrantCancellationForm.clearedBy.name.firstName}"/>
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
	
	<c:if test="${not empty warrantCancellation}">
		<c:set var="updatable" value="${warrantCancellation}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editWarrant}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>