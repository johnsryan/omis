<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<tr id="staffAttendanceItemRow${staffAttendanceItemIndex}" class="staffAttendanceItemRow">
		<td>
			<a class="removeLink" id="removeStaffAttendanceLink${staffAttendanceItemIndex}" href="${pageContext.request.contextPath}/hearing/removeStaffAttendance.html?hearing=${hearing.id}"><span class="linkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			<input type="hidden" id="staffAttendanceId${staffAttendanceItemIndex}" name="staffAttendanceItems[${staffAttendanceItemIndex}].staffAttended" value="${staffAttendanceItem.staffAttended.id}"/>
			<form:errors path="staffAttendanceItems[${staffAttendanceItemIndex}].staffAttended" cssClass="error"/>
			<input type="hidden" id="staffAttendanceOperation${staffAttendanceItemIndex}" name="staffAttendanceItems[${staffAttendanceItemIndex}].itemOperation" value="${staffAttendanceItem.itemOperation}"/>
			<form:errors path="staffAttendanceItems[${staffAttendanceItemIndex}].itemOperation" cssClass="error"/>
		</td>
		<td>
			<input id="staffAttendanceInput${staffAttendanceItemIndex}"/>
				<input type="hidden" id="staffAttendanceItems[${staffAttendanceItemIndex}].staff" name="staffAttendanceItems[${staffAttendanceItemIndex}].staff" value="${staffAttendanceItems[staffAttendanceItemIndex].staff.id}"/>
				<a id="clearStaffAttendance${staffAttendanceItemIndex}" class="clearLink"></a>
			<span id="staffAttendanceDisplay${staffAttendanceItemIndex}">
				<c:if test="${not empty staffAttendanceItems[staffAttendanceItemIndex].staff}" >
					<c:out value="${staffAttendanceItems[staffAttendanceItemIndex].staff.staffMember.name.lastName}, ${staffAttendanceItems[staffAttendanceItemIndex].staff.staffMember.name.firstName} ${staffAttendanceItems[staffAttendanceItemIndex].staff.title.name}"/>
				</c:if>
			</span>
			<form:errors path="staffAttendanceItems[${staffAttendanceItemIndex}].staff" cssClass="error"/>
		</td>
	</tr>
</fmt:bundle>