<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.visitation.msgs.visitation">
	<ul>
		<li><a class="viewEditLink" href="${pageContext.request.contextPath}/visitation/visit/edit.html?visit=${visit.id}"><fmt:message key="viewEditLabel"/></a></li>
		<c:choose>
			<c:when test="${currentlyVisiting}">
				<li><a class="checkOutLink" href="${pageContext.request.contextPath}/visitation/visit/checkOut.html?visit=${visit.id}&&date=${date}&&startDate=${startDate}&&endDate=${endDate}"><fmt:message key="checkOutLabel"/></a></li>
			</c:when>
		</c:choose>
		<li><a class="removeLink" href="${pageContext.request.contextPath}/visitation/visit/remove.html?visit=${visit.id}"><fmt:message key="removeLabel"/></a></li>
		<sec:authorize access="hasRole('VISIT_VIEW') or hasRole('ADMIN')">
		<li>
			 <a href="${pageContext.request.contextPath}/visitation/visit/visitDetailsReport.html?visit=${visit.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="visitDetailsReportLinkLabel"/></a>
		</li>
		</sec:authorize>
	</ul>
</fmt:bundle>