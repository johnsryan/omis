<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.courtcasecondition.msgs.courtCaseConditions">
	<ul>
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_CREATE') or hasRole('ADMIN')">
		<c:forEach var="category" items="${categories}">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/courtCaseCondition/create.html?offender=${offender.id}&courtCaseAgreementCategory=${category.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createConditionLink">
						<fmt:param value="${category.name}" />
					</fmt:message>
				</span></a>
			</li>
		</c:forEach>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/courtCaseCondition/condition/probationParoleConditionsReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="probationParoleConditionsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/courtCaseCondition/condition/sentencingConditionsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="sentencingConditionsListReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>