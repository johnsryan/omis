<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearing/violations/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/violationEvent/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationEventsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/hearing/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createHearingLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/hearingListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="hearingListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>