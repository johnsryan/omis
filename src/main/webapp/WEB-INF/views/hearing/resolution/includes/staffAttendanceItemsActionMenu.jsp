<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('HEARING_CREATE') or hasRole('HEARING_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createStaffAttendanceItemLink" class="createLink" href="${pageContext.request.contextPath}/hearing/createStaffAttendanceItem.html?staffAttendanceItemIndex=${staffAttendanceItemIndex}"><span class="visibleLinkLabel"><fmt:message key="addAttendedStaffLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>