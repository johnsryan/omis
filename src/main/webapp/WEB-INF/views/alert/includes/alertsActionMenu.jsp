<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (November 25, 2014)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.alert.msgs.alert">
	<ul>
		<sec:authorize access="hasRole('ALERT_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/alert/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createAlertLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('ALERT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/alert/alertListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="alertListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('ALERT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty alert}">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/alert/edit.html?alert=${alert.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditAlertLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('ALERT_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty alert}">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/alert/remove.html?alert=${alert.id}"><span class="visibleLinkLabel"><fmt:message key="removeAlertLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
				<sec:authorize access="hasRole('ALERT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty alert}">
			<li>
				<a href="${pageContext.request.contextPath}/alert/alertDetailsReport.html?alert=${alert.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="alertDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>