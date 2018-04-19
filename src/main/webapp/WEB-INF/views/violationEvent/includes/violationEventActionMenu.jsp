<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.hearing.msgs.hearing" var="hearingBundle" />
<fmt:bundle basename="omis.violationevent.msgs.violationEvent">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_EVENT_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/violationEvent/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationEventsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearing/violations/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationsLink" bundle="${hearingBundle}" /></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>