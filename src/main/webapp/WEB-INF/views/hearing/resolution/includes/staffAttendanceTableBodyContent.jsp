<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<c:forEach var="staffAttendanceItem" items="${staffAttendanceItems}" varStatus="status">
		<c:set var="staffAttendanceItem" value="${staffAttendanceItem}" scope="request"/>
		<c:set var="staffAttendanceItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty staffAttendanceItem.itemOperation}">
			<jsp:include page="staffAttendanceItemTableRow.jsp"/>
		</c:if>
	</c:forEach>
</fmt:bundle>