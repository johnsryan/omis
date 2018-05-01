<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/violations/resolvedViolationsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="resolvedViolationsListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>