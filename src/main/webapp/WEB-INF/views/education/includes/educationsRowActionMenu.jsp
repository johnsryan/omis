<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.education.msgs.education">
	<ul>
		<sec:authorize access="hasRole('EDUCATION_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/education/edit.html?educationTerm=${educationTerm.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="editEducationTermLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('EDUCATION_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/education/remove.html?educationTerm=${educationTerm.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="removeEducationTermLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('EDUCATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty educationTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/education/transcriptRequestReleaseForm.html?educationTerm=${educationTerm.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="transcriptRequestReleaseFormLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('EDUCATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty educationTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/education/transcriptRequestReleaseRedactedForm.html?educationTerm=${educationTerm.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="transcriptRequestReleaseRedactedFormLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="(hasRole('EDUCATION_VIEW') AND hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty educationTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/education/educationDetailsReport.html?educationTerm=${educationTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="educationDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>