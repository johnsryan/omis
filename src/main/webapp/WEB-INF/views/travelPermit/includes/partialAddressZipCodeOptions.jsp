<%--
 - Author: Yidong Li
 - Date : May 29 2018
 - Display city options.
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
<c:forEach var="zipCode" items="${partialAddressZipCodes}">
	<c:choose>
	<c:when test="${not empty travelPermitForm.partialAddressZipCode and travelPermitForm.partialAddressZipCode eq zipCode}">
		<option value="${zipCode.id}" selected="selected"><c:out value="${zipCode.value}"/> <c:out value="${zipCode.extension}"/></option>
	</c:when>
	<c:otherwise>
		<option value="${zipCode.id}"><c:out value="${zipCode.value}"/> <c:out value="${zipCode.extension}"/></option>
	</c:otherwise>
	</c:choose>
</c:forEach>