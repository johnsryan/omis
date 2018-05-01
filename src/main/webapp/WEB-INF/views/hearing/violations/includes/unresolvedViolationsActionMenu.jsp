<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_CREATE') or hasRole('ADMIN')">
		<c:if test="${unresolvedDisciplinaryViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=INFORMAL&violationCategory=DISCIPLINARY"><span class="visibleLinkLabel"><fmt:message key="resolveDisciplinaryViolationsInformallyLink"/></span></a>
			</li>
		</c:if>
		<c:if test="${unresolvedConditionViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=INFORMAL&violationCategory=SUPERVISION"><span class="visibleLinkLabel"><fmt:message key="resolveSupervisionViolationsInformallyLink"/></span></a>
			</li>
		</c:if>
		<c:if test="${unresolvedDisciplinaryViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=FORMAL&violationCategory=DISCIPLINARY"><span class="visibleLinkLabel"><fmt:message key="scheduleDisciplinaryHearingLink"/></span></a>
			</li>
		</c:if>
		<c:if test="${unresolvedConditionViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=FORMAL&violationCategory=SUPERVISION"><span class="visibleLinkLabel"><fmt:message key="scheduleSupervisionHearingLink"/></span></a>
			</li>
		</c:if>
		<c:if test="${unresolvedDisciplinaryViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=DISMISSED&violationCategory=DISCIPLINARY"><span class="visibleLinkLabel"><fmt:message key="dismissDisciplinaryViolationsLink"/></span></a>
			</li>
		</c:if>
		<c:if test="${unresolvedConditionViolationsExist eq true}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?offender=${offender.id}&resolutionCategory=DISMISSED&violationCategory=SUPERVISION"><span class="visibleLinkLabel"><fmt:message key="dismissSupervisionViolationsLink"/></span></a>
			</li>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/violations/unresolvedViolationsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="unresolvedViolationsListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>								
	</ul>
</fmt:bundle>