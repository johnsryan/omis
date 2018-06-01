<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.violationevent.msgs.violationEvent">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/violationEvent/edit.html?violationEvent=${violationEvent.id}&category=${violationEvent.category}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/violationEvent/remove.html?violationEvent=${violationEvent.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty violationEvent}">
			<li>
				<a href="${pageContext.request.contextPath}/violationEvent/violationEventDetailsReport.html?violationEvent=${violationEvent.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="violationEventDetailsReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>