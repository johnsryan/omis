<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<table id="achievementTable" class="editTable">
	<thead>
		<tr>
			<th class="operations"><a class="actionMenuItem" id="staffAttendanceItemsActionMenuLink" href="${pageContext.request.contextPath}/hearing/resolution/staffAttendanceItemsActionMenu.html?staffAttendanceItemIndex=${staffAttendanceItemIndex}"></a></th>
			<th><fmt:message key="staffNameLabel"/></th>
		</tr>
	</thead>
	<tbody id="staffAttendanceTableBody">
		<jsp:include page="staffAttendanceTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>