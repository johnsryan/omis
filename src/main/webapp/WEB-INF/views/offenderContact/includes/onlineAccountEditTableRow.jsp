<%--
 - Online account edit row.
 -
 - Author: Josh Divine
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<fmt:setBundle var="contactBundle" basename="omis.contact.msgs.contact"/>
<c:if test="${not empty onlineAccountItem.operation}">
	<c:set var="rowClass" value="${'REMOVE' eq onlineAccountItem.operation.name ? 'removeRow' : ''}"/>
	<tr id="onlineAccountItems[${onlineAccountItemIndex}].row" class="${rowClass}">
		<td>
			<a id="onlineAccountItems[${onlineAccountItemIndex}].removeLink" class="removeLink" href="${pageContext.request.contextPath}/offenderConctact/contact/removeOnlineAccount.html?onlineAccount=${onlineAccountItem.onlineAccount.id}" title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"></a>
			<input id="onlineAccountItems[${onlineAccountItemIndex}].onlineAccount" name="onlineAccountItems[${onlineAccountItemIndex}].onlineAccount" type="hidden" value="${onlineAccountItem.onlineAccount.id}"/>
			<input id="onlineAccountItems[${onlineAccountItemIndex}].operation" name="onlineAccountItems[${onlineAccountItemIndex}].operation" type="hidden" value="${onlineAccountItem.operation.name}"/>
		</td>
		<c:set var="onlineAccountFieldsPropertyName" value="onlineAccountItems[${onlineAccountItemIndex}].fields" scope="request"/>
		<c:set var="onlineAccountFields" value="${onlineAccountItem.fields}" scope="request"/>
		<c:set var="updatable" value="${onlineAccountItem.onlineAccount}" scope="request"/>
		<jsp:include page="/WEB-INF/views/contact/includes/onlineAccountFields.jsp"/>
</tr>
</c:if>