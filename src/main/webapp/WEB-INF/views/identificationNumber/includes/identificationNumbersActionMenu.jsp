<%--
  - Action menu for identification numbers screen.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:setBundle basename="omis.identificationnumber.msgs.identificationNumber" var="identificationNumberBundle"/>
<ul>
	<c:if test="${not empty identificationNumber}">
		<sec:authorize access="hasRole('IDENTIFICATION_NUMBER_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/identificationNumber/edit.html?identificationNumber=${identificationNumber.id}" title="<fmt:message key='viewEditIdentificationNumberLink' bundle='${identificationNumberBundle}'/>" class="viewEditLink"><fmt:message key="viewEditIdentificationNumberLink" bundle="${identificationNumberBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty identificationNumber}">
		<sec:authorize access="hasRole('IDENTIFICATION_NUMBER_REMOVE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/identificationNumber/remove.html?identificationNumber=${identificationNumber.id}" title="<fmt:message key='removeIdentificationNumberLink' bundle='${identificationNumberBundle}'/>" class="removeLink"><fmt:message key="removeIdentificationNumberLink" bundle="${identificationNumberBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty identificationNumber}">
		<sec:authorize access="hasRole('IDENTIFICATION_NUMBER_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/identificationNumber/identificationNumberDetailsReport.html?identificationNumber=${identificationNumber.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="identificationNumberDetailsReportLinkLabel" bundle="${identificationNumberBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('IDENTIFICATION_NUMBER_CREATE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/identificationNumber/create.html?offender=${offender.id}" title="<fmt:message key='createIdentificationNumberLink' bundle='${identificationNumberBundle}'/>" class="createLink"><fmt:message key="createIdentificationNumberLink" bundle="${identificationNumberBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty offender}">
	
		<sec:authorize access="hasRole('IDENTIFICATION_NUMBER_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/identificationNumber/identificationNumberListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="identificationNumberListingReportLinkLabel" bundle="${identificationNumberBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
</ul>