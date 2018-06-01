<%-- Author: Stephen Abson --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.person.msgs.person" var="personBundle"/>
<fmt:setBundle basename="omis.offender.msgs.alternativeOffenderName" var="nameBundle"/>
<fmt:setBundle basename="omis.offender.msgs.alternativeOffenderIdentity" var="identityBundle"/>
<fmt:setBundle basename="omis.offender.msgs.offenderDemographics" var="demographicsBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
	<ul>
		<li><sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('OFFENDER_EDIT') or hasRole('ADMIN')">
			<a class="personalDetailsLink" href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offender.id}">
				<fmt:message key="editPersonHeader" bundle="${personBundle}"/></a></sec:authorize></li>
		<li><sec:authorize access="hasRole('OFFENDER_ALT_NAME_LIST') or hasRole('ADMIN')">
			<a class="listLink" href="${pageContext.request.contextPath}/offender/name/alternative/list.html?offender=${offender.id}">
				<fmt:message key="listAlternativeOffenderNamesTitle" bundle="${nameBundle}"/></a></sec:authorize></li>
		<li><sec:authorize access="hasRole('OFFENDER_ALT_IDENTITY_LIST') or hasRole('ADMIN')">
			<a class="listLink" href="${pageContext.request.contextPath}/offender/identity/alternative/list.html?offender=${offender.id}">
				<fmt:message key="listAlternativeOffenderIdentitiesTitle" bundle="${identityBundle}"/></a></sec:authorize></li>
		<sec:authorize access="hasRole('OFFENDER_DEMOGRAPHICS_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/demographics/demographicsDetailsReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="offenderDemographicsDetailsReportLink"  bundle="${demographicsBundle}"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>