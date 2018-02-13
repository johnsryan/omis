<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
<sec:authorize var="editMedicalReview" access="hasRole('MEDICAL_REVIEW_CREATE') or hasRole('MEDICAL_REVIEW_EDIT') or hasRole('ADMIN')"/>
<form:form commandName="medicalReviewForm" class="editForm" enctype="multipart/form-data">
	<fieldset>
		<legend>
			<fmt:message key="medicalReviewTitle"/>
		</legend>
		<span class="fieldGroup">
			<form:label path="date" class="fieldLabel">
				<fmt:message key="dateLabel"/>
			</form:label>
			<form:input path="date" class="date"/>
			<form:errors path="date" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="text" class="fieldLabel">
				<fmt:message key="descriptionLabel"/>
			</form:label>
			<form:textarea path="text"/>
			<form:errors path="text" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="healthClassification" class="fieldLabel">
				<fmt:message key="observationLabel"/>
			</form:label>
			<form:select path="healthClassification">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${healthClassifications}" itemLabel="name" itemValue="id"/>
			</form:select>
			<form:errors path="healthClassification" cssClass="error"/>
		</span>
	</fieldset>
	<fieldset>
		<legend>
			<fmt:message key="notesTitle" />
		</legend>
		<span class="fieldGroup">
			<c:set var="medicalReviewNoteItems" value="${medicalReviewForm.medicalReviewNoteItems}" scope="request"/>
			<jsp:include page="medicalReviewNoteTable.jsp"/>
		</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="documentsTitle"/></legend>
		<span class="fieldGroup">
			<c:set var="medicalReviewDocumentAssociationItems" value="${medicalReviewForm.medicalReviewDocumentAssociationItems}" scope="request"/>
			<jsp:include page="medicalReviewDocumentAssociationItems.jsp"/>
		</span>
	</fieldset>
	<c:if test="${not empty medicalReview}">
		<c:set var="updatable" value="${medicalReview}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editMedicalReview}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>