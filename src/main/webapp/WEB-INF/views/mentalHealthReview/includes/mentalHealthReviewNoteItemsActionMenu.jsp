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
 - Action menu for mental health review notes.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
	<ul>
		<sec:authorize access="hasRole('MENTAL_HEALTH_REVIEW_CREATE') or hasRole('MENTAL_HEALTH_REVIEW_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createMentalHealthReviewNoteItemLink" class="createLink" 
					href="${pageContext.request.contextPath}/mentalHealthReview/createMentalHealthReviewNoteItem.html?mentalHealthReviewNoteItemIndex=${mentalHealthReviewNoteItemIndex}"><span class="visibleLinkLabel"><fmt:message key="addMentalHealthReviewNoteLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>