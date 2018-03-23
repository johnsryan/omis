<%-- 
  - Author: Ryan Johns
  - Author: Annie Jacques
  - Version: 0.1.2 (May 17, 2017)
  - Since: OMIS 3.0 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<c:forEach var="summary" items="${summaries}">
<tr>
	<c:choose>
	<c:when test="${empty offender}">
		<c:set var="onReturn" value="byUser" />
	</c:when>
	<c:when test="${not empty offender}">
		<c:set var="onReturn" value="byOffender" />
	</c:when>
	</c:choose>
	<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/presentenceInvestigation/request/presentenceInvestigationRequestsRowActionMenu.html?presentenceInvestigationRequest=${summary.presentenceInvestigationRequestId}&onReturn=${onReturn}"></a></td>
	<td><c:out value="${summary.docketValue}"/></td>
	<td><fmt:formatDate value="${summary.requestDate}" pattern="MM/dd/yyyy" /></td>
	<td><fmt:formatDate value="${summary.expectedCompletionDate}" pattern="MM/dd/yyyy"/></td>
	<td>
	<c:choose>
		<c:when test="${summary.completedTaskCount == summary.totalTaskCount}">
		<fmt:message key="completedLabel"/>
		</c:when>
		<c:otherwise>
		<fmt:message key="completionLabel">
			<fmt:param value="${summary.completedTaskCount}"/>
			<fmt:param value="${summary.totalTaskCount}"/>
		</fmt:message>
		</c:otherwise>
	</c:choose>
	</td>
	<td><fmt:formatDate value="${summary.submissionDate}" pattern="MM/dd/yyyy"/></td>
	<td><c:out value="${summary.category}"/></td>
	<c:choose>
		<c:when test="${empty offender}">
			<td>
			<c:if test="${not empty summary.offenderId}">
				<c:out value="${summary.offenderFirstName} ${summary.offenderLastName} #${summary.offenderNumber}"/>
			</c:if>
			</td>
		</c:when>
		<c:when test="${not empty offender}">
			<td><c:out value="${summary.assignedUserFirstName} ${summary.assignedUserLastName}"/></td>
		</c:when>
	</c:choose>
	<td><fmt:formatDate value="${summary.sentenceDate}" pattern="MM/dd/yyyy" /></td>
</tr>
</c:forEach>
</fmt:bundle>