<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<c:forEach var="warrantCauseViolationItem" items="${warrantCauseViolationItems}" varStatus="status">
		<c:set var="warrantCauseViolationItem" value="${warrantCauseViolationItem}" scope="request"/>
		<c:set var="violationToWitItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty warrantCauseViolationItem.itemOperation}">
			<jsp:include page="violationToWitItemRow.jsp"/>
		</c:if>
	</c:forEach>
</fmt:bundle>