<%--
 - Author: Yidong Li
 - Date : May 24 2018
 - Display city options.
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
<c:forEach var="city" items="${partialAddressCities}">
	<c:choose>
	<c:when test="${not empty travelPermitForm.partialAddressCity and travelPermitForm.partialAddressCity eq city}">
		<option value="${city.id}" selected="selected"><c:out value="${city.name}"/></option>
	</c:when>
	<c:otherwise>
		<option value="${city.id}"><c:out value="${city.name}"/></option>
	</c:otherwise>
	</c:choose>
</c:forEach>