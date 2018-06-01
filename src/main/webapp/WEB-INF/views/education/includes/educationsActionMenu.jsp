<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.education.msgs.education">
	<ul>
		<sec:authorize access="hasRole('EDUCATION_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/education/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createEducationTermLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('EDUCATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/education/educationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="educationListingReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
		
	</ul>
</fmt:bundle>