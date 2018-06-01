<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.employment.msgs.employment">
	<ul>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/employment/edit.html?employmentTerm=${employmentTerm.id}&offender=${offender.id}&employerStatus=true"><span class="visibleLinkLabel"><fmt:message key="viewEditLink"/></span></a>
			</li>
		</sec:authorize> 
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/employment/remove.html?employmentTerm=${employmentTerm.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="removeEmploymentTermLink" /></span></a>
			</li>
		</sec:authorize>	
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Employer_Notification&EMPLOYMENT_TERM_ID=${employmentTerm.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="employerNotificationLetterLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Wage_Garnishment_Amendment&EMPLOYMENT_TERM_ID=${employmentTerm.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="wageGarnishmentAmendmentReportLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Wage_Garnishment_Notification&EMPLOYMENT_TERM_ID=${employmentTerm.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="wageGarnishmentNotificationReportLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Wage_Garnishment_Termination&EMPLOYMENT_TERM_ID=${employmentTerm.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="wageGarnishmentTerminationReportLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty employmentTerm}">
				<li>
					<a href="${pageContext.request.contextPath}/employment/employmentDetailsReport.html?employmentTerm=${employmentTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="employmentDetailsReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>