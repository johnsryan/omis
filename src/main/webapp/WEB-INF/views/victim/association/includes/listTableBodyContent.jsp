<%--
  - Table body content for victim associations.
  -
  - Author: Stephen Abson
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.victim.msgs.victim" var="victimBundle"/>
<fmt:setBundle basename="omis.offender.msgs.offender" var="offenderBundle"/>
<c:forEach var="victimAssociationSummary" items="${victimAssociationSummaries}">
	<tr>
		<td>
			<a class="actionMenuItem" href="${pageContext.request.contextPath}//victim/association/associationsActionMenu.html?victimAssociation=${victimAssociationSummary.associationId}&amp;redirectTarget=${redirectTarget.name}"></a>
		</td>
		<c:if test="${empty victim}">
		<td>
			<c:out value="${victimAssociationSummary.lastName}"/>,
			<c:out value="${victimAssociationSummary.firstName}"/>
			<c:if test="${not empty victimAssociationSummary.middleName}">
				<c:out value="${fn:substring(victimAssociationSummary.middleName, 0, 1)}"/>
			</c:if>
			<c:if test="${not empty victimAssociationSummary.suffix}">
				<c:out value="${victimAssociationSummary.suffix}"/>
			</c:if>
			<c:if test="${victimAssociationSummary.victimOffender}">
				<c:out value="#${victimAssociationSummary.victimOffenderNumber}"/>
			</c:if>
		</td>
		</c:if>
		<c:if test="${empty offender}">
		<td>
			<c:set var="offenderDetails">${victimAssociationSummary.offenderLastName}, ${victimAssociationSummary.offenderFirstName}<c:if test="${not empty victimAssociationSummary.offenderMiddleName}"> ${fn:substring(victimAssociationSummary.offenderMiddleName, 0, 1)}</c:if> #${victimAssociationSummary.offenderNumber}</c:set>
			<sec:authorize access="hasRole('ADMIN') or hasRole('OFFENDER_PROFILE_VIEW')" var="canViewOffenderProfile"/>
			<c:choose>
				<c:when test="${canViewOffenderProfile}"><a title="<fmt:message key='viewOffenderProfileLink' bundle='${offenderBundle}'/>" href="${pageContext.request.contextPath}/offender/profile.html?offender=${victimAssociationSummary.offenderId}"><c:out value="${offenderDetails}"/></a></c:when>
				<c:otherwise><c:out value="${offenderDetails}"/></c:otherwise>
			</c:choose>
		</td>
		</c:if>
		<td>
			<c:choose>
				<c:when test="${victimAssociationSummary.docRegistered}">
					<fmt:message key="yesLabel" bundle="${commonBundle}"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="noLabel" bundle="${commonBundle}"/>
				</c:otherwise>
			</c:choose>
		</td>
		<td><fmt:formatDate pattern="MM/dd/yyyy" value="${victimAssociationSummary.registerDate}"/></td>
		<td>
			<c:choose>
				<c:when test="${victimAssociationSummary.victim}">
					<fmt:message key="yesLabel" bundle="${commonBundle}"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="noLabel" bundle="${commonBundle}"/>
				</c:otherwise>
			</c:choose>
		</td>
		<c:if test="${empty victim}">
		<td>
			<c:if test="${victimAssociationSummary.address}">
				<c:set var="addressSummarizable" value="${victimAssociationSummary}" scope="request"/>
				<jsp:include page="/WEB-INF/views/address/includes/addressSummary.jsp"/>
			</c:if>
			<c:if test="${victimAssociationSummary.telephoneNumber}">
				<c:set var="telephoneNumberSummarizable" value="${victimAssociationSummary}" scope="request"/>
				<jsp:include page="/WEB-INF/views/contact/includes/telephoneNumberSummary.jsp"/>
			</c:if>
		</td>
		</c:if>
	</tr>
</c:forEach>