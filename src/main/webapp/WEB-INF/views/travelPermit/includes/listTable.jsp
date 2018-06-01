<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.travelpermit.msgs.travelPermit" var="travelPermitBundle"/>
<table id="travelPermitTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"/>
			<th class="date"><fmt:message key="startDateLabel" bundle="${commonBundle}"/></th>
			<th class="date"><fmt:message key="endDateLabel" bundle="${commonBundle}"/></th>
			<th><fmt:message key="destinationLabel" bundle="${travelPermitBundle}"/></th>
			<th><fmt:message key="permitPeriodicityLabel" bundle="${travelPermitBundle}"/></th>
			<th><fmt:message key="createdByLabel" bundle="${travelPermitBundle}"/></th>
		</tr>
	</thead>
	<tbody id="travelPermitListTableBody">	
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>