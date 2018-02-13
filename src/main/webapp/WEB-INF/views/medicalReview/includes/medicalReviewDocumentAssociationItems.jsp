	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
<a id="createMedicalReviewDocumentAssociationItemLink" class="createLink addLink">
	<span class="visibleLinkLabel">
		<fmt:message key="addAttachmentLink"/>
	</span>
</a>
<span id="medicalReviewDocumentAssociationItems">
	<c:forEach var="medicalReviewDocumentAssociationItem" items="${medicalReviewDocumentAssociationItems}" varStatus="status">
		<c:set var="medicalReviewDocumentAssociationItem" value="${medicalReviewDocumentAssociationItem}" scope="request"/>
		<c:set var="medicalReviewDocumentAssociationItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty medicalReviewDocumentAssociationItem.itemOperation}">
			<jsp:include page="medicalReviewDocumentAssociationItemContent.jsp"/>
		</c:if>
	</c:forEach>
</span>
</fmt:bundle>