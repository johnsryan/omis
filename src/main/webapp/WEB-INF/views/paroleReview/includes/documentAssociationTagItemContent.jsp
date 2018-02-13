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
 - Document association tag fields for parole reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Jan 30, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.parolereview.msgs.paroleReview">
	<span id="paroleReviewDocumentAssociation${documentAssociationItemIndex}documentTagItem${documentTagItemIndex}" class="documentTag">
		<input type="hidden" id="paroleReviewDocumentAssociation${documentAssociationItemIndex}documentTagId${documentTagItemIndex}" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].documentTag" value="${documentTagItem.documentTag.id}"/>
		<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].documentTag" cssClass="error"/>
		<input type="hidden" id="paroleReviewDocumentAssociation${documentAssociationItemIndex}documentTagOperation${documentTagItemIndex}" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].itemOperation" value="${documentTagItem.itemOperation}"/>
		<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].itemOperation" cssClass="error"/>
		<span class="fieldGroup">
			<a class="removeLink removeTag" id="removeParoleReviewDocumentAssociation${documentAssociationItemIndex}documentTagLink${documentTagItemIndex}"
			href="${pageContext.request.contextPath}/paroleReview/removeDocumentTag.html?paroleReview=${paroleReview.id}"></a>
			<input type="text" id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" value="${documentTagItem.name}" />
			<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].documentTagItems[${documentTagItemIndex}].name" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>