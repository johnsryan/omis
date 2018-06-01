<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
	<ul>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink"
				 href="${pageContext.request.contextPath}/presentenceInvestigation/request/edit.html?presentenceInvestigationRequest=${presentenceInvestigationRequest.id}&onReturn=${onReturn}"><span class="visibleLinkLabel"><fmt:message key="editPresentenceInvestigationRequestLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="homeLink" href="${pageContext.request.contextPath}/presentenceInvestigation/home.html?presentenceInvestigationRequest=${presentenceInvestigationRequest.id}"><span class="visibleLinkLabel"><fmt:message key="presentenceInvestigationHomeLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/remove.html?presentenceInvestigationRequest=${presentenceInvestigationRequest.id}&onReturn=${onReturn}"><span class="visibleLinkLabel"><fmt:message key="removePresentenceInvestigationRequestLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_LIST') or hasRole('ADMIN')">
			<li>
				<omis:reportPro reportPath="/Legal/PSI/Letter_of_Noncompliance&PSI_REQ_ID=${presentenceInvestigationRequest.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="psiNoncomplianceReportLinkLabel"/></omis:reportPro>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_LIST') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/psiQuestionnaireReport.html?person=${presentenceInvestigationRequest.docket.person.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="pSIQuestionnaireReportLinkLabel"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_LIST') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/investigationRequestDetailsReport.html?presentenceInvestigationRequest=${presentenceInvestigationRequest.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="investigationRequestDetailsReportLinkLabel"/></a>
			</li>
		</sec:authorize>				
	</ul>
</fmt:bundle>