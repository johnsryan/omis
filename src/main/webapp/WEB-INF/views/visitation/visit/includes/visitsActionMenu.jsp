<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.visitation.msgs.visitation">
	<ul>
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/visitation/visit/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createVisitLinkLabel"/></span></a>
		</li>
		<sec:authorize access="hasRole('VISIT_VIEW') or hasRole('ADMIN')">
		<li>
			<omis:reportPro reportPath="/Relationships/Visitation/Visitation_Listing&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="visitationModuleListingReportLinkLabel"/></omis:reportPro>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VISIT_VIEW') or hasRole('ADMIN')">
		<li>
			<omis:reportPro reportPath="/Relationships/Visitation/Visitor_Log&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="visitationModuleVisitorLogReportLinkLabel"/></omis:reportPro>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VISIT_VIEW') or hasRole('ADMIN')">
		<li>
			 <a href="${pageContext.request.contextPath}/visitation/visitorLogLegacyReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="visitorLogLegacyReportLinkLabel"/></a>
		</li>
		</sec:authorize>
	</ul>
</fmt:bundle>