<%--
  - Family association online account edit/creation table.
  -
  - Author: Yidong Li
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.family.msgs.family" var="familyBundle"/>
<fmt:bundle basename="omis.family.msgs.family">
	<table id="onlineAccountTable" class="editTable">
		<thead>
			<tr>
			<th><a class="actionMenuItem" id="onlineAccountActionMenuLink" href="${pageContext.request.contextPath}/family/onlineAccountActionMenu.html?offender=${offenderSummary.id}"></a><span class="visibleLinkLabel"/></span></th>
			<jsp:include page="../../contact/includes/onlineAccountFieldsHeader.jsp"/>
			</tr>
		</thead>
		<tbody id="emailBody">
			<c:forEach var="familyAssociationOnlineAccountItem" items="${familyAssociationOnlineAccountItems}" varStatus="status">
				<c:set var="familyAssociationOnlineAccountItem" value="${familyAssociationOnlineAccountItem}" scope="request"/>
				<c:set var="familyAssociationOnlineAccountIndex" value="${status.index}" scope="request"/>
				<jsp:include page="createFamilyAssociationOnlineAccountTableRow.jsp"/>
			</c:forEach>
		</tbody>
	</table>
</fmt:bundle>