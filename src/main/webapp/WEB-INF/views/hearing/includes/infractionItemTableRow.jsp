<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	
	<c:if test="${not empty infractionItem.disciplinaryCodeViolation}">
		<tr id="infractionItemRow${infractionItemIndex}" class="infractionItemRow">
			<td>
				<!-- <a class="removeLink" id="removeInfractionLink${infractionItemIndex}" href="${pageContext.request.contextPath}/hearing/removeInfraction.html?hearing=${hearing.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a> -->
				<input type="hidden" id="infractionId${infractionItemIndex}" name="infractionItems[${infractionItemIndex}].disciplinaryCodeViolation" value="${infractionItem.disciplinaryCodeViolation.id}"/>
				<form:errors path="infractionItems[${infractionItemIndex}].disciplinaryCodeViolation" cssClass="error"/>
			</td>
			<td>
				<fmt:formatDate var="eventDate" value="${infractionItem.disciplinaryCodeViolation.violationEvent.event.date}" pattern="MM/dd/yyyy"/>
				<span class="violationDescriptionNoOverflow">
					<c:out value="${eventDate} - ${infractionItem.disciplinaryCodeViolation.violationEvent.event.details}" />
					<span class="hideOverflow"></span>
				</span>
				<span class="showOverflow"></span>
			</td>
			<td>
				<c:out value="${infractionItem.disciplinaryCodeViolation.disciplinaryCode.value}" /> - <c:out value="${infractionItem.disciplinaryCodeViolation.disciplinaryCode.description}" />
			</td>
		</tr>
	</c:if>
	
	<c:if test="${not empty infractionItem.conditionViolation}">
		<tr id="infractionItemRow${infractionItemIndex}" class="infractionItemRow">
			<td>
				<!-- <a class="removeLink" id="removeInfractionLink${infractionItemIndex}" href="${pageContext.request.contextPath}/hearing/removeInfraction.html?hearing=${hearing.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>-->
				<input type="hidden" id="infractionId${infractionItemIndex}" name="infractionItems[${infractionItemIndex}].conditionViolation" value="${infractionItem.conditionViolation.id}"/>
				<form:errors path="infractionItems[${infractionItemIndex}].conditionViolation" cssClass="error"/>
			</td>
			<td>
				<fmt:formatDate var="eventDate" value="${infractionItem.conditionViolation.violationEvent.event.date}" pattern="MM/dd/yyyy"/>
				<span class="violationDescriptionNoOverflow">
					<c:out value="${eventDate} - ${infractionItem.conditionViolation.violationEvent.event.details}" />
					<span class="hideOverflow"></span>
				</span>
				<span class="showOverflow"></span>
			</td>
			<td>
				<c:out value="${infractionItem.conditionViolation.condition.conditionClause.conditionTitle.title}" /> - <c:out value="${infractionItem.conditionViolation.condition.clause}" />
			</td>
		</tr>
	</c:if>
</fmt:bundle>