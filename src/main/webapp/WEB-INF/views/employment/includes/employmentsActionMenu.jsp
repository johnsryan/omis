<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (Feb 9, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.employment.msgs.employment">
	<ul>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/employment/search.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="createEmployment"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="(hasRole('EMPLOYMENT_TERM_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Wage_Garnishment_Statement&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="wageGarnishmentStatementReportLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<li><omis:reportPro reportPath="/CaseManagement/Employment/Wage_Garnishment_Statement_Redacted&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="wageGarnishmentStatementRedactedReportLinkLabel"/></omis:reportPro></li>
		</sec:authorize>
		<sec:authorize access="hasRole('EMPLOYMENT_TERM_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/employment/employmentListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="employmentListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>