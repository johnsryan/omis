<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<c:forEach var="violationSelectionItem" items="${violationSelectionItems}" varStatus="i">
<c:set var="summary" value="${violationSelectionItem.violationSummary}" /> 
<tr class="violationItemRow">
	<td>
	<c:choose>
		<c:when test="${violationCategory eq 'DISCIPLINARY'}">
			<c:set value="${summary.disciplinaryCodeViolationId}" var="violationId"/>
			<form:input type="hidden" path="violationSelectionItems[${i.index}].disciplinaryCodeViolation" value="${summary.disciplinaryCodeViolationId}"/>
		</c:when>
		<c:when test="${violationCategory eq 'SUPERVISION'}">
			<c:set value="${summaryItem.violationSummary.conditionViolationId}" var="violationId"/>
			<form:input type="hidden" path="violationSelectionItems[${i.index}].conditionViolation" value="${summary.conditionViolationId}"/>
		</c:when>
	</c:choose>
		<form:checkbox path="violationSelectionItems[${i.index}].selected" class="selectViolationCheckBox"/>
	</td>
	<td>
		<fmt:message key="${summary.violationEventCategory}CategoryLabel"/>
	</td>
	<td>
		<fmt:formatDate value="${summary.violationEventDate}" pattern="MM/dd/yyyy" />
	</td>
	<td>
		<span class="violationDescriptionNoOverflow">
			<c:out value="${summary.violationEventDetails}" />
			<span class="hideOverflow"></span>
		</span>
		<span class="showOverflow"></span>
	</td>
	<td>
		<c:choose>
			<c:when test="${violationCategory eq 'DISCIPLINARY'}">
				<c:out value="${summary.disciplinaryCodeValue}"/> - <c:out value="${summary.disciplinaryCodeDescription}"/>
			</c:when>
			<c:when test="${violationCategory eq 'SUPERVISION'}">
				<c:out value="${summary.conditionTitle}"/> - <c:out value="${summary.conditionClause}"/>
			</c:when>
		</c:choose>
	</td>
</tr>
</c:forEach>
</fmt:bundle>	