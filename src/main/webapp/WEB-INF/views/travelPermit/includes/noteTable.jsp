<%--
  - Travel permit note table.
  -
  - Author: Yidong Li
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<table id="noteTable" class="editTable">
		<thead>
			<tr>
			<th><a class="actionMenuItem" id="noteActionMenuLink" href="${pageContext.request.contextPath}/travelPermit/noteActionMenu.html?offender=${offenderSummary.id}"></a><span class="visibleLinkLabel"/></span></th>
			<th><fmt:message key="travelPermitDateLabel"/></th>
			<th><fmt:message key="travelPermitLastUpdatedByLabel"/></th>
			<th><fmt:message key="travelPermitNoteLabel"/></th>
			</tr>
		</thead>
		<tbody id="noteTableBody">
			<c:forEach var="travelPermitNoteItem" items="${travelPermitNoteItems}" varStatus="status">
				<c:if test="${travelPermitNoteItem.operation != null}">
					<c:set var="travelPermitNoteItem" value="${travelPermitNoteItem}" scope="request"/>
					<c:set var="travelPermitNoteItemIndex" value="${status.index}" scope="request"/>
					<jsp:include page="noteTableRow.jsp"/>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
</fmt:bundle>