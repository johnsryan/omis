<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editAssessmentCategoryOverride" access="hasRole('ASSESSMENT_CATEGORY_OVERRIDE_EDIT') or hasRole('ADMIN') or hasRole('ASSESSMENT_CATEGORY_OVERRIDE_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<form:form commandName="assessmentCategoryOverrideForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="overrideDetailsHeader"/></legend>
		<span class="fieldGroup">
			<form:label path="overrideDate" class="fieldLabel">
				<fmt:message key="overrideDateLabel"/>
			</form:label>
			<form:input path="overrideDate" class="date"/>
			<form:errors path="overrideDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="overrideRating" class="fieldLabel">
				<fmt:message key="overrideRatingLabel"/>
			</form:label>
			<form:select path="overrideRating">
				<jsp:include page="../../../includes/nullOption.jsp"/>
				<c:forEach items="${overrideRatings}" var="rating">
					<option value="${rating.id}" ${rating.id == assessmentCategoryOverrideForm.overrideRating ? 'selected="selected"' : ''}>
						<c:out value="${rating.description}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="overrideRating" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="overrideReason" class="fieldLabel">
				<fmt:message key="overrideReasonLabel"/>
			</form:label>
			<form:select path="overrideReason">
				<jsp:include page="../../../includes/nullOption.jsp"/>
				<c:forEach items="${overrideReasons}" var="reason">
					<option value="${reason.id}" ${reason.id == assessmentCategoryOverrideForm.overrideReason ? 'selected="selected"' : ''}>
						<c:out value="${reason.name}"/>
					</option>
				</c:forEach>
			</form:select>
			<form:errors path="overrideReason" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="authorizedBy" class="fieldLabel">
				<fmt:message key="authorizedByLabel"/>
			</form:label>
			<input id="authorizedByInput"/>
			<form:hidden id="authorizedBy" path="authorizedBy"/>
			<a id="authorizedByCurrent" class="currentUserAccountLink"></a>
			<a id="authorizedByClear" class="clearLink"></a>
			<span id="authorizedByDisplay">
				<c:if test="${not empty assessmentCategoryOverrideForm.authorizedBy}" >
					<c:out value="${assessmentCategoryOverrideForm.authorizedBy.staffMember.name.lastName}, ${assessmentCategoryOverrideForm.authorizedBy.staffMember.name.firstName} ${assessmentCategoryOverrideForm.authorizedBy.title.name}"/>
				</c:if>
			</span>
			<form:errors path="authorizedBy" cssClass="error"/>
		</span>
	</fieldset>
	<c:if test="${not empty assessmentCategoryOverride}">
		<c:set var="updatable" value="${assessmentCategoryOverride}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editAssessmentCategoryOverride}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>