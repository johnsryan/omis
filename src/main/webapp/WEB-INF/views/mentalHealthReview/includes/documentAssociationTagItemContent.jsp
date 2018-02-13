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
 - Document association tag fields for mental health reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
	<span id="mentalHealthReviewDocumentAssociation${documentAssociationItemIndex}documentTagItem${documentTagItemIndex}" class="documentTag">
		<input type="hidden" id="mentalHealthReviewDocumentAssociation${documentAssociationItemIndex}documentTagId${documentTagItemIndex}" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].documentTag" value="${documentTagItem.documentTag.id}"/>
		<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].documentTag" cssClass="error"/>
		<input type="hidden" id="mentalHealthReviewDocumentAssociation${documentAssociationItemIndex}documentTagOperation${documentTagItemIndex}" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].itemOperation" value="${documentTagItem.itemOperation}"/>
		<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].itemOperation" cssClass="error"/>
		<span class="fieldGroup">
			<a class="removeLink removeTag" id="removeMentalHealthReviewDocumentAssociation${documentAssociationItemIndex}documentTagLink${documentTagItemIndex}"
			href="${pageContext.request.contextPath}/mentalHealthReview/removeDocumentTag.html?mentalHealthReview=${mentalHealthReview.id}"></a>
			<input type="text" id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" value="${documentTagItem.name}" />
			<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>