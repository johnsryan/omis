<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%--
 - Table body content for mental health review notes.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
	<c:forEach var="mentalHealthNoteItem" items="${mentalHealthReviewNoteItems}" varStatus="status">
		<c:set var="mentalHealthNoteItem" value="${mentalHealthNoteItem}" scope="request"/>
		<c:set var="mentalHealthReviewNoteItemIndex" value="${status.index}" scope="request"/>
		<c:if test="${not empty mentalHealthNoteItem.itemOperation}">
			<jsp:include page="mentalHealthReviewNoteTableRow.jsp"/>
		</c:if>
	</c:forEach>
</fmt:bundle>