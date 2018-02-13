<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<c:forEach var="medicalReviewNoteItem" items="${medicalReviewNoteItems}" varStatus="status">
		<c:set var="medicalReviewNoteItem" value="${medicalReviewNoteItem}" scope="request"/>
		<c:set var="medicalReviewNoteItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty medicalReviewNoteItem.itemOperation}">
			<jsp:include page="medicalReviewNoteItemTableRow.jsp"/>
		</c:if>
	</c:forEach>
</fmt:bundle>