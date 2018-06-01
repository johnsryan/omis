<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.violationevent.msgs.violationEvent">
	<ul>
		<sec:authorize access="hasRole('VIOLATION_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearing/violations/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_CREATE') or hasRole('ADMIN')">
			<c:forEach items="${categories}" var="category">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/violationEvent/create.html?offender=${offender.id}&category=${category}">
						<span class="visibleLinkLabel">
							<fmt:message key="create${category}ViolationEventLink" />
						</span>
					</a>
				</li>
			</c:forEach>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/violationEvent/violationEventListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="violationEventListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>