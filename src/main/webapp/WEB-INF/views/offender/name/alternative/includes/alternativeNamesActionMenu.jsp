<%-- Author: Stephen Abson --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:setBundle basename="omis.person.msgs.person" var="personBundle"/>
<fmt:bundle basename="omis.offender.msgs.alternativeOffenderName">
<ul>
	<li><sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('OFFENDER_EDIT') or hasRole('ADMIN')">
		<a class="personalDetailsLink" href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offender.id}">
			<fmt:message key="editPersonHeader" bundle="${personBundle}"/></a></sec:authorize></li>
	<li><sec:authorize access="hasRole('OFFENDER_ALT_NAME_CREATE') or hasRole('ADMIN')">
		<a class="createLink" href="${pageContext.request.contextPath}/offender/name/alternative/create.html?offender=${offender.id}">
			<fmt:message key="createAlterntaiveOffenderNameTitle"/></a></sec:authorize></li>
	<sec:authorize access="hasRole('OFFENDER_ALT_NAME_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/name/alternative/altNameListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="altNameListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>	
</ul>
</fmt:bundle>