<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.courtcasecondition.msgs.courtCaseConditions">
	<ul>
		<li>
			<sec:authorize access="hasRole('COURT_CASE_CONDITION_VIEW') or hasRole('ADMIN')">
				<a class="viewEditLink" href="${pageContext.request.contextPath}/courtCaseCondition/edit.html?courtCaseAgreement=${courtCaseAgreement.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}" /></span></a>
			</sec:authorize>
		</li>
		<li>
			<sec:authorize access="hasRole('COURT_CASE_CONDITION_REMOVE') or hasRole('ADMIN')">
				<a class="removeLink" href="${pageContext.request.contextPath}/courtCaseCondition/remove.html?courtCaseAgreement=${courtCaseAgreement.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}" /></span></a>
			</sec:authorize>
		</li>
		<li>
			<sec:authorize access="hasRole('COURT_CASE_CONDITION_EDIT') or hasRole('ADMIN')">
				<a class="viewEditLink" href="${pageContext.request.contextPath}/courtCaseCondition/condition/addStandard.html?courtCaseAgreement=${courtCaseAgreement.id}"><span class="visibleLinkLabel"><fmt:message key="createEditStandardLink"/></span></a>
			</sec:authorize>
		</li>
		
		<!-- Loooooop -->
		<c:forEach items="${conditionGroups}" var="conditionGroup">
			<li>
				<sec:authorize access="hasRole('COURT_CASE_CONDITION_EDIT') or hasRole('ADMIN')">
					<a class="viewEditLink" href="${pageContext.request.contextPath}/courtCaseCondition/condition/addSpecialGroup.html?courtCaseAgreement=${courtCaseAgreement.id}&conditionGroup=${conditionGroup.id}"><span class="visibleLinkLabel"><fmt:message key="createSpecialLink"><fmt:param value="${conditionGroup.name}"/></fmt:message></span></a>
				</sec:authorize>
			</li>
		</c:forEach>
		
		
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
			<li>
				<a class="viewLink viewConditionLink" id="viewConditions"><fmt:message key="conditionListLabel"/>
					<input type="hidden" id="courtCaseAgreementId" value="${courtCaseAgreement.id}" />
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty courtCaseAgreement}">
			    <li>
				    <a href="${pageContext.request.contextPath}/courtCaseCondition/condition/conditionsPNPReport.html?courtCaseAgreement=${courtCaseAgreement.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="conditionsPNPDocketReportLinkLabel"/></a>
			    </li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty courtCaseAgreement}">
			    <li>
				    <a href="${pageContext.request.contextPath}/courtCaseCondition/condition/sentencingConditionDetailsReport.html?courtCaseAgreement=${courtCaseAgreement.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="sentencingConditionDetailsReportLinkLabel"/></a>
			    </li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>