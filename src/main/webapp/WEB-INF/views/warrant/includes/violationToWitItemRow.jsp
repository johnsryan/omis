<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<tr id="warrantCauseViolationItemRow${violationToWitItemIndex}" class="warrantCauseViolationItemRow">
		<td>
			<a class="removeLink" id="removeWarrantCauseViolationLink${violationToWitItemIndex}" href="${pageContext.request.contextPath}/warrant/removeWarrantCauseViolation.html?warrant=${warrant.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="warrantCauseViolationId${violationToWitItemIndex}" name="warrantCauseViolationItems[${violationToWitItemIndex}].warrantCauseViolation" value="${warrantCauseViolationItem.warrantCauseViolation.id}"/>
			<form:errors path="warrantCauseViolationItems[${violationToWitItemIndex}].warrantCauseViolation" cssClass="error"/>
			
			<input type="hidden" id="warrantCauseViolationOperation${violationToWitItemIndex}" name="warrantCauseViolationItems[${violationToWitItemIndex}].itemOperation" value="${warrantCauseViolationItem.itemOperation}"/>
			<form:errors path="warrantCauseViolationItems[${violationToWitItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<!-- Condition Clausess Select -->
			<select id="warrantConditionClause${violationToWitItemIndex}" name="warrantCauseViolationItems[${violationToWitItemIndex}].conditionClause">
				<jsp:include page="../../includes/nullOption.jsp"/>
				<c:forEach items="${conditionClauses}" var="conditionClause">
					<option value="${conditionClause.id}" ${conditionClause eq warrantCauseViolationItem.conditionClause ? 'selected="selected"' : ''} >
						<c:out value="${conditionClause.conditionTitle.title}"/>
					</option>
				</c:forEach>
			</select>
			<form:errors path="warrantCauseViolationItems[${violationToWitItemIndex}].conditionClause" cssClass="error"/>
		</td>
		<td id="warrantConditionClause${violationToWitItemIndex}Dockets">
			<c:set var="dockets" value="${clauseToDocketsMap[warrantCauseViolationItem.conditionClause.conditionTitle.title]}" scope="request"/>
			<jsp:include page="conditionClauseDockets.jsp"/>
		</td>
		<td>
			<textarea rows="4" name="warrantCauseViolationItems[${violationToWitItemIndex}].description" id="warrantCauseViolationItems[${violationToWitItemIndex}].description" style="width: 95%"><c:out value="${warrantCauseViolationItem.description}"/></textarea>
			<form:errors path="warrantCauseViolationItems[${violationToWitItemIndex}].description" cssClass="error"/>
		</td>
	</tr>
</fmt:bundle> 