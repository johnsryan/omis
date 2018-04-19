<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<c:forEach var="summary" items="${resolvedViolationSummaries}">
<tr class="violationItemRow">
	<td>
		<a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/hearing/violations/resolvedViolationsRowActionMenu.html?infraction=${summary.infractionId}"></a>
	</td>
	<td>
		<fmt:message key="${summary.violationEventCategory}CategoryLabel"/>
	</td>
	<td>
		<fmt:formatDate value="${summary.violationEventDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<c:choose>
			<c:when test="${not empty summary.disciplinaryCodeDescription}">
				<c:out value="${summary.disciplinaryCodeValue}"/> - <c:out value="${summary.disciplinaryCodeDescription}"/>
			</c:when>
			<c:when test="${not empty summary.conditionClause}">
				<c:out value="${summary.conditionTitle}"/> - <c:out value="${summary.conditionClause}"/>
			</c:when>
		</c:choose>
	</td>
	<td>
		<c:out value="${summary.decision}${not empty summary.decision and not empty summary.decisionReason ? ' - ' : ''}${summary.decisionReason}"/>
	</td>
	<td>
		<c:if test="${not empty summary.dispositionCategory}">
			<fmt:message key="${summary.dispositionCategory}DispositionLabel"/>
		</c:if>
	</td>
	<td>
		<fmt:message key="${summary.resolutionCategory}ResolutionLabel"/>
	</td>
	<td>
		<fmt:formatDate value="${summary.appealDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<c:out value="${summary.sanctionDescription}"/>
	</td>
</tr>
</c:forEach>
</fmt:bundle>	