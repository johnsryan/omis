<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/edit.html?hearing=${hearing.id}"><span class="visibleLinkLabel"><fmt:message key="editHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/violations/select.html?hearing=${hearing.id}&offender=${hearing.subject.offender.id}&resolutionCategory=FORMAL&violationCategory=${hearing.category}&goToOption=HEARINGS_LIST"><span class="visibleLinkLabel"><fmt:message key="selectViolationsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearing/resolution/create.html?hearing=${hearing.id}&offender=${hearing.subject.offender.id}&resolutionCategory=FORMAL&violationCategory=${hearing.category}"><span class="visibleLinkLabel"><fmt:message key="adjudicateHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/hearing/remove.html?hearing=${hearing.id}"><span class="visibleLinkLabel"><fmt:message key="removeHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty hearing}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/adjudicateHearingReport.html?hearing=${hearing.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="adjudicateHearingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>	
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty hearing}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/summaryOfHearingReport.html?hearing=${hearing.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="summaryDisciplinaryHearingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>		
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty hearing}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/hearingDetailsReport.html?hearing=${hearing.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="hearingDetailsReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>					
	</ul>
</fmt:bundle>