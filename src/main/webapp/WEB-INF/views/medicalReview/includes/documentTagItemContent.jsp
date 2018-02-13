<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<span id="medicalReviewDocumentAssociation${medicalReviewDocumentAssociationItemIndex}documentTagItem${documentTagItemIndex}" class="documentTag">
		<input type="hidden" id="medicalReviewDocumentAssociation${medicalReviewDocumentAssociationItemIndex}documentTagId${documentTagItemIndex}" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].documentTag" value="${documentTagItem.documentTag.id}"/>
		<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].documentTag" cssClass="error"/>
		<input type="hidden" id="medicalReviewDocumentAssociation${medicalReviewDocumentAssociationItemIndex}documentTagOperation${documentTagItemIndex}" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].documentTagOperation" value="${documentTagItem.documentTagOperation}"/>
		<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].documentTagOperation" cssClass="error"/>
		<span class="fieldGroup">
			<a class="removeLink removeTag" id="removeMedicalReviewDocumentAssociation${medicalReviewDocumentAssociationItemIndex}documentTagLink${documentTagItemIndex}"
			href="${pageContext.request.contextPath}/medicalReview/removeDocumentTag.html?medicalReviewDocumentAssociation=${medicalReviewDocumentAssociation.id}"></a>
			<input type="text" id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].name" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].name" value="${documentTagItem.name}" />
		</span>
		<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].tagItems[${documentTagItemIndex}].name" cssClass="tagError"/>
	</span>
</fmt:bundle>