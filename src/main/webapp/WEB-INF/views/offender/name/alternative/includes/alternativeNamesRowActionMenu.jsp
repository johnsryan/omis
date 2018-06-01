<%-- Author: Joel Norris --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.offender.msgs.alternativeOffenderName">
	<ul>
		<li>
			<sec:authorize access="hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')">
				<a class="viewEditLink" href="${pageContext.request.contextPath}/offender/name/alternative/edit.html?alternativeNameAssociation=${alternativeNameAssociation.id}" title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</sec:authorize>
		</li>
		<li>
			<sec:authorize access="hasRole('OFFENDER_ALT_NAME_REMOVE') or hasRole('ADMIN')">
				<a class="removeLink" href="${pageContext.request.contextPath}/offender/name/alternative/remove.html?alternativeNameAssociation=${alternativeNameAssociation.id}" title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</sec:authorize>
		</li>
		<sec:authorize access="hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty alternativeNameAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/name/alternative/altNameDetailsReport.html?alternativeNameAssociation=${alternativeNameAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altNameDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>