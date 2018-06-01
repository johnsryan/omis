<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.paroleeligibility.msgs.paroleEligibility">
	<jsp:include page="../../includes/nullOption.jsp"/>
	<c:forEach var="statusReason" items="${statusReasons}" varStatus="status">
		<option value="${statusReason.id}"><c:out value="${statusReason.name}"/></option>
	</c:forEach>
</fmt:bundle>