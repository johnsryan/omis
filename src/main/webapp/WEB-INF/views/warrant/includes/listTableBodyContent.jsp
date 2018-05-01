<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
<c:forEach var="summary" items="${warrantSummaries}">
<tr>
	<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/warrant/warrantsRowActionMenu.html?warrant=${summary.warrantId}&warrantRelease=${summary.warrantReleaseId}&warrantCancellation=${summary.warrantCancellationId}&warrantArrest=${summary.warrantArrestId}"></a></td>
	<td>
		<fmt:message key="categoryLabel.${summary.warrantReasonCategory}"/>
	</td>
	<td>
		<fmt:formatDate value="${summary.warrantDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<fmt:formatDate value="${summary.clearedByDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<c:set var="clearedMethod" value="${not empty summary.warrantCancellationId ? 'canceled' : not empty summary.warrantArrestId ? 'arrested' : 'none'}"/>
		<c:set var="released" value="${not empty summary.warrantArrestId && not empty summary.warrantReleaseId ? 'released' : ''}" />
		<c:if test="${clearedMethod != 'none'}">
		<fmt:message key="${clearedMethod}${released}ClearedMethodLabel"/>
		</c:if>
	</td>
</tr>
</c:forEach>
</fmt:bundle>	