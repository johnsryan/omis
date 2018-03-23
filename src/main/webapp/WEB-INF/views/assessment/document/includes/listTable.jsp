<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:bundle basename="omis.assessment.msgs.assessmentDocument">
	<table class="listTable">
		<tr>
			<th class="operations"></th>
			<th><fmt:message key="titleLabel" /></th>
			<th><fmt:message key="updatedByLabel" /></th>
		</tr>
		<tbody>
			<jsp:include page="listTableBodyContent.jsp" />
		</tbody>
	</table>
</fmt:bundle>