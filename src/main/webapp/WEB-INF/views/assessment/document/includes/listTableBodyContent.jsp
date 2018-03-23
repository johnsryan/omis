<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:bundle basename="omis.assessment.msgs.assessmentDocument">
<c:forEach var="summary" items="${assessmentDocumentSummaries}">
	<tr>
		<td><a class="actionMenuItem rowActionMenuItem" href="${pageContext.request.contextPath}/assessment/document/assessmentDocumentsRowActionMenu.html?assessmentDocumentAssociation=${summary.assessmentDocumentAssociationId}"></a></td>
		<td>
			<c:out value="${summary.documentTitle}"/>
		</td>
		<td>
			<c:out value="${summary.updatedByLastName}, ${summary.updatedByFirstName}"/>
		</td>
	</tr>
</c:forEach>
</fmt:bundle>