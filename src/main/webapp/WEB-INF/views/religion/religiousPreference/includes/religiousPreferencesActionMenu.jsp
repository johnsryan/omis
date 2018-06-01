<%-- Author: Stephen Abson --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.religion.msgs.religion">
	<ul>
	<sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_CREATE') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/religion/religiousPreference/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel"><fmt:message key="createReligiousPreferenceLink"/></span></a>
		</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
		<li>
			<a href="${pageContext.request.contextPath}/religion/religiousPreference/religiousPreferenceListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="religiousPreferenceListingReportLinkLabel"/></a>
		</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty preference}">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/religion/religiousPreference/edit.html?preference=${preference.id}">
				<span class="visibleLinkLabel"><fmt:message key="viewEditReligiousPreferenceLink"/></span></a>
		</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_REMOVE') or hasRole('ADMIN')">
		<c:if test="${not empty preference}">
			<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/religion/religiousPreference/remove.html?preference=${preference.id}">
				<span class="visibleLinkLabel"><fmt:message key="removeReligiousPreferenceLink"/></span></a>
			</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty preference}">
		<li>
			<a href="${pageContext.request.contextPath}/religion/religiousPreference/religiousPreferenceDetailsReport.html?preference=${preference.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="religiousPreferenceDetailsReportLinkLabel"/></a>
		</li>
		</c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>