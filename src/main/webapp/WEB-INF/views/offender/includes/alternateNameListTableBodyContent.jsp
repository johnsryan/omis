<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="address" uri="/WEB-INF/tld/address.tld" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offender.msgs.offenderSearch" var="offenderSearchBundle"/>
<c:forEach items="${alternateNameSummaries}" var="altNameSummary" varStatus="altNameStatus">
	<c:choose>
		<c:when test="${not empty altNameSummary.firstName}">
			<c:set var="altFirstName" value="${altNameSummary.firstName}"/>
		</c:when>
		<c:otherwise>
			<c:set var="altFirstName" value=" "/>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${not empty altNameSummary.lastName}">
			<c:set var="altLastName" value="${altNameSummary.lastName}"/>
		</c:when>
		<c:otherwise>
			<c:set var="altLastName" value=" "/>
		</c:otherwise>
	</c:choose>
	<tr>
		<td>
		</td>
		<td>
			<c:choose>
				<c:when test="${not empty altNameSummary.middleName}">
					<fmt:message key="altNameSummaryLabel" bundle="${offenderSearchBundle}">
						<fmt:param value="${altLastName}"/>
						<fmt:param value="${altFirstName}"/>
						<fmt:param value="${altNameSummary.middleName}"/>
					</fmt:message>
				</c:when>
				<c:otherwise>
					<fmt:message key="altNameSummaryNoMiddleNameLabel" bundle="${offenderSearchBundle}">
						<fmt:param value="${altLastName}"/>
						<fmt:param value="${altFirstName}"/>
					</fmt:message>
				</c:otherwise>
			</c:choose>
		</td>
		<td>
			<c:out value="${altNameSummary.categoryName}"/>
		</td>
	</tr>
</c:forEach>