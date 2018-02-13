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
 - Table for mental health review notes.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
<table id="mentalHealthReviewNoteTable" class="editTable">
	<thead class="notes">
		<tr>
			<th class="operations"><a class="actionMenuItem" id="mentalHealthReviewNoteItemsActionMenuLink" href="${pageContext.request.contextPath}/mentalHealthReview/mentalHealthReviewNoteItemsActionMenu.html?mentalHealthReviewNoteItemIndex=${mentalHealthReviewNoteItemIndex}"></a></th>
			<th class="date"><fmt:message key="dateLabel"/></th>
			<th><fmt:message key="noteLabel"/></th>	
			<th><fmt:message key="lastUpdatedByLabel"/></th>		
		</tr>
	</thead>
	<tbody id="mentalHealthReviewNoteTableBody">
		<jsp:include page="mentalHealthReviewNoteTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>