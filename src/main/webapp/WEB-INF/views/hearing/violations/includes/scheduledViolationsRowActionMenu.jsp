<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/edit.html?hearing=${hearing.id}"><span class="visibleLinkLabel"><fmt:message key="editHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?hearing=${hearing.id}&offender=${hearing.subject.offender.id}&resolutionCategory=FORMAL&violationCategory=${hearing.category}&goToOption=VIOLATIONS_LIST"><span class="visibleLinkLabel"><fmt:message key="selectViolationsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/resolution/create.html?hearing=${hearing.id}&offender=${hearing.subject.offender.id}&resolutionCategory=FORMAL&violationCategory=${hearing.category}"><span class="visibleLinkLabel"><fmt:message key="adjudicateHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty hearing}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/violations/hearingDetailsReport.html?hearing=${hearing.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="hearingDetailsReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>