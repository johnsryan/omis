<%--
 - Table body content of STG affiliations.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<c:forEach var="affiliationSummary" items="${affiliationSummaries}"
	varStatus="status">
	<tr class="<c:if test='${affiliationSummary.active}'>activeRecord</c:if>">
		<td>
			<a class="actionMenuItem" href="${pageContext.request.contextPath}/stg/affiliationsActionMenu.html?affiliation=${affiliationSummary.id}"
			id="affiliations${status.index}"></a>
		</td>
		<td><c:out value="${affiliationSummary.groupName}"/></td>
		<td><c:out value="${affiliationSummary.chapterName}"/></td>
		<td><c:out value="${affiliationSummary.moniker}"/></td>
		<td><c:out value="${affiliationSummary.rankName}"/></td>
		<td><c:out value="${affiliationSummary.cityName}"/></td>
		<td><c:out value="${affiliationSummary.stateName}"/></td>
		<td><fmt:formatDate value="${affiliationSummary.startDate}" pattern="MM/dd/yyyy"/></td>
		<td><fmt:formatDate value="${affiliationSummary.endDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
</c:forEach>
