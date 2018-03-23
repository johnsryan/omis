<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessmentNotes">
	<c:forEach var="assessmentNoteItem" items="${assessmentNoteItems}" varStatus="status">
		<c:set var="assessmentNoteItem" value="${assessmentNoteItem}" scope="request"/>
		<c:set var="assessmentNoteItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty assessmentNoteItem.itemOperation}">
			<jsp:include page="assessmentNoteItemTableRow.jsp"/>
		</c:if>
	</c:forEach>
</fmt:bundle>