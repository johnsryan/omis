<%--
  - Action menu for screen to list print report.
  -
  - Author: Josh Divine
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.financial.msgs.financial">
	<ul>
		<sec:authorize access="hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/financial/offenderFinancialSummaryReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="offenderFinancialSummaryReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">					
			<li>
				<a href="${pageContext.request.contextPath}/financial/financialProfileReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="financialProfileReportLinkLabel"/></a>
			</li>			
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>