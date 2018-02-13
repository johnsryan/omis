<%-- Author: Ryan Johns
   - Version: 0.1.0 (Jul 28, 2016)
   - Since: OMIS 3.0 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.placement.msgs.summary">
<c:if test="${not empty locationTermSummary}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="correctionalStatusNameLabel"/></span>
		<a href="${pageContext.request.contextPath}/supervision/placementTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.correctionalStatusName}"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="correctionalStatusReasonNameLabel"/></span>
		<a href="${pageContext.request.contextPath}/supervision/placementTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.correctionalStatusReasonName}"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="correctionalStatusStartDateLabel"/></span>
		<a href="${pageContext.request.contextPath}/supervision/placementTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><fmt:formatDate value="${locationTermSummary.correctionalStatusStartDate}" pattern="M/d/YYYY"/></span>
		</a>
	</div>
</div>
<c:if test="${not empty locationTermSummary.chimesId}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="chimesIdLabel"/></span>
		<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.chimesId}"/></span>
	</div>
</div>
</c:if>
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="currentLocationNameLabel"/></span>
		<a href="${pageContext.request.contextPath}/locationTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.currentLocationName}"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="currentLocationReasonNameLabel"/></span>
		<a href="${pageContext.request.contextPath}/locationTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.currentLocationReasonName}"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="currentLocationStartDateLabel"/></span>
		<a href="${pageContext.request.contextPath}/locationTerm/list.html?offender=${offenderSummary.id}" >
			<span class="offenderHeaderFieldValue"><fmt:formatDate value="${locationTermSummary.currentLocationStartDate}" pattern="M/d/YYYY"/></span>
		</a>
	</div>
	
	<%-- Remove this when placement is migrated - location term update screens should be used instead [Stephen Abson] --%>
	<div class="headerCell">
		<sec:authorize access="hasRole('LOCATION_TERM_EDIT') or hasRole('ADMIN')">
	    	<c:if test="${not empty locationTermSummary.currentLocationName}">
	    		<a href="${pageContext.request.contextPath}/locationTerm/endLocationTerm.html?offender=${offenderSummary.id}"><fmt:message key="endActiveLocationTermLink"/></a>
	    	</c:if>
    	</sec:authorize>
	</div>
</div>
<c:if test="${not empty locationTermSummary.confidentialOffender}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="confidentialOffenderLabel"/></span>
		<span class="offenderHeaderFieldValue warningMessage"><c:out value="${locationTermSummary.confidentialOffender}"/></span>
	</div>
</div>
</c:if>
<c:if test="${not empty locationTermSummary.supervisingOfficer}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="supervisingOfficerLabel"/></span>
		<span class="offenderHeaderFieldValue"><c:out value="${locationTermSummary.supervisingOfficer}"/></span>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="supervisionStartDateLabel"/></span>
		<span class="offenderHeaderFieldValue"><fmt:formatDate value="${locationTermSummary.supervisionStartDate}" pattern="M/d/YYYY"/></span>
	</div>
</div>

</c:if>
</c:if>
</fmt:bundle>