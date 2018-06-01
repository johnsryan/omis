<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.residence.msgs.residence">
	<tr id="nonResidenceTermRow${nonResidenceTermIndex}" class="nonResidenceTermRow">
		<td>
			<input type="hidden" name="nonResidenceTerms[${nonResidenceTermIndex}]" value="${nonResidenceTerm.id}"/>
			<fmt:formatDate value="${nonResidenceTerm.dateRange.startDate}" pattern="MM/DD/yyyy"/>
		</td>
		<td>
			<fmt:formatDate value="${nonResidenceTerm.dateRange.endDate}" pattern="MM/DD/yyyy"/>
		</td>
		<td>
		</td>
		<td>
			<fmt:message key="nonResidenceTerm.location.label">
				<fmt:param value="${nonResidenceTerm.location.organization.name}"/>
			</fmt:message>
		</td>
		<td>
			<fmt:message key="nonResidenceTerm.status.label">
				<fmt:param value="${nonResidenceTerm.status.name}"/>
			</fmt:message>
		</td>
	</tr>
</fmt:bundle>