<%-- Author: Stephen Abson --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.stg.msgs.stg">
<ul>
	<sec:authorize access="hasRole('STG_AFFILIATION_CREATE') or hasRole('ADMIN')">
		<c:if test="${not empty offender}"><li><a class="createLink" href="${pageContext.request.contextPath}/stg/affiliation/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createStgAffiliationLink"/></span></a></li>	
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_ACTIVITY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/stg/stgAffiliationActivityListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="stgAffiliationActivityListingReportLinkLabel"/></a>
			</li>
			</c:if>
	</sec:authorize>	
	<sec:authorize access="hasRole('STG_AFFILIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/stg/stgListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="stgListingReportLinkLabel"/></a>
			</li>
			</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_AFFILIATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty affiliation}"><li><a class="viewEditLink" href="${pageContext.request.contextPath}/stg/affiliation/edit.html?affiliation=${affiliation.id}"><span class="visibleLinkLabel"><fmt:message key="viewStgAffiliationEditLink"/></span></a></li></c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_AFFILIATION_REMOVE') or hasRole('ADMIN')">
		<c:if test="${not empty affiliation}"><li><a class="removeLink" href="${pageContext.request.contextPath}/stg/affiliation/remove.html?affiliation=${affiliation.id}"><span class="visibleLinkLabel"><fmt:message key="removeStgAffiliationLink"/></span></a></li></c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_AFFILIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty affiliation}">
			<li>
				<a href="${pageContext.request.contextPath}/stg/stgDetailsReport.html?affiliation=${affiliation.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="stgDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
	</sec:authorize>
</ul>
</fmt:bundle>