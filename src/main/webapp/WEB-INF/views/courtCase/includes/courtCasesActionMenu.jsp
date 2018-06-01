<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (November 14, 2014)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.courtcase.msgs.courtCase">
	<ul>
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/courtCase/create.html?defendant=${defendant.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createCourtCaseLink"/>
				</span>
			</a>
		</li>
		<sec:authorize access="hasRole('COMMIT_STATUS_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/commitStatus/list.html?offender=${offender.id}" class="listLink"><fmt:message key="commitStatusLink"/></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/courtCase/courtCaseListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="courtCaseListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/courtCase/courtCaseDetailListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="courtCaseDetailListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>