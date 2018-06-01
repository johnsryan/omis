<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.supervisionfee.msgs.supervisionFee">
	<ul>
		<sec:authorize access="hasRole('SUPERVISION_FEE_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/supervisionFee/edit.html?supervisionFeeSummaries=${monthlySupervisionFee.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SUPERVISION_FEE_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/supervisionFee/remove.html?supervisionFeeSummaries=${monthlySupervisionFee.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SUPERVISION_FEE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty monthlySupervisionFee}">
			<li>
				<a href="${pageContext.request.contextPath}/supervisionFee/supervisionFeeModificationsReport.html?monthlySupervisionFee=${monthlySupervisionFee.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="supervisionFeeModificationsLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/supervisionFee/supervisionFeeWaiverRequestReport.html?monthlySupervisionFee=${monthlySupervisionFee.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="supervisionFeeWaiverRequestLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/supervisionFee/supervisionFeeOffenderRightsResponsibilitiesReport.html?monthlySupervisionFee=${monthlySupervisionFee.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="supervisionFeeOffenderRightsResponsibilitiesLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/supervisionFee/supervisionFeeDetailsReport.html?monthlySupervisionFee=${monthlySupervisionFee.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="supervisionFeeDetailsReportLinkLabel"/></a>
			</li>			
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>