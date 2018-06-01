<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.residence.msgs.residence">
<table id="residenceListTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"></th>
			<th></th>
			<th></th>
			<th class="date"><fmt:message key="startDateLabel"/></th>
			<th class="date"><fmt:message key="endDateLabel"/></th>
			<th><fmt:message key="addressLabel"/></th>			
			<th><fmt:message key="residenceCategoryLabel"/></th>
			<th><fmt:message key="residenceStatusLabel"/></th>
		</tr>		
	</thead>
	<tbody>
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>