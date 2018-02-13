<?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
<fmt:bundle basename="omis.medicalreview.msgs.medicalReview">
	<c:choose>
		<c:when test="${medicalReviewDocumentAssociationItem.itemOperation == 'REMOVE'}">
			<c:set value="removeContent" var="medicalReviewDocumentAssociationDisplayClass"/>
		</c:when>
		<c:otherwise>
			<c:set var="medicalReviewDocumentAssociationDisplayClass" value=""/>
		</c:otherwise>
	</c:choose>
	<span id="medicalReviewDocumentAssociationItem${medicalReviewDocumentAssociationItemIndex}" class="medicalReviewDocumentAssociationItem ${medicalReviewDocumentAssociationDisplayClass}">
		<a class="removeLink" id="removeMedicalReviewDocumentAssociationLink${medicalReviewDocumentAssociationItemIndex}"
		href="${pageContext.request.contextPath}/medicalReview/removeMedicalReviewDocumentAssociation.html?medicalReviewDocumentAssociation=${medicalReviewDocumentAssociation.id}">
			<fmt:message key="removeLink" bundle="${commonBundle}"/>
		</a>
		<input type="hidden" id="medicalReviewDocumentAssociationId${medicalReviewDocumentAssociationItemIndex}"
				name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].medicalReviewDocumentAssociation"
				value="${medicalReviewDocumentAssociationItem.medicalReviewDocumentAssociation.id}"/>
		<input type="hidden" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].document"
						id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].document"
						value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].document.id}"/>
		<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].medicalReviewDocumentAssociation" cssClass="error"/>
		<input type="hidden" id="medicalReviewDocumentAssociationOperation${medicalReviewDocumentAssociationItemIndex}"
				name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].itemOperation" value="${medicalReviewDocumentAssociationItem.itemOperation}"/>
		<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].itemOperation" cssClass="error"/>
		<span class="fieldGroup">
			<label for="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].title" class="fieldLabel">
				<fmt:message key="documentTitleLabel"/>
			</label>
			<input type="text" id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].title"
					name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].title"
				value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].title}" />
			<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].title" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<label for="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].date" class="fieldLabel">
				<fmt:message key="fileDateLabel"/>
			</label>
			<fmt:formatDate var="medicalReviewDocumentAssociationDate" value="${medicalReviewDocumentAssociationItem.date}" pattern="MM/dd/yyyy"/>
			<input type="text" value="${medicalReviewDocumentAssociationDate}" id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].date"
					name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].date" class="date" />
			<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].date" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<a id="createDocumentTagItemLink${medicalReviewDocumentAssociationItemIndex}" class="createLink addLink">
				<span class="visibleLinkLabel">
					<fmt:message key="addTagLink"/>
				</span>
			</a>
		</span>
		<span id="documentTagItems${medicalReviewDocumentAssociationItemIndex}">
			<c:forEach var="documentTagItem" items="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].tagItems}" varStatus="status">
				<c:set var="documentTagItem" value="${documentTagItem}" scope="request"/>
				<c:set var="documentTagItemIndex" value="${status.index}" scope="request"/>
				<c:if test="${not empty documentTagItem.documentTagOperation}">
					<jsp:include page="documentTagItemContent.jsp"/>
				</c:if>
			</c:forEach>
		</span>
		<span class="fieldGroup">
			<label for="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data" class="fieldLabel">
				<fmt:message key="documentLabel"/>
			</label>
			<c:choose>
				<c:when test="${not empty medicalReviewDocumentAssociationItem.document}">
					<c:set var="filename" value = "${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].document.filename}"/>
					<fmt:formatDate value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].document.date}"
							pattern="MM/dd/yyyy" var="documentDate"/>
					<a id="medicalReviewDocumentAssociationDownloadLink${medicalReviewDocumentAssociationItemIndex}"
		 					href="${pageContext.request.contextPath}/medicalReview/retrieveFile.html?document=${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].document.id}"
							class="downloadLink">
						<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
							<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
						</fmt:message>
					</a>
					<input type="hidden" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data"
						id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data"
						value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].data}"/>
				</c:when>
				<c:otherwise>
						<input id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data" type="file"
								name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data"
								value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].data}" />
				</c:otherwise>
			</c:choose>
			<input type="hidden" name="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].fileExtension"
					id="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].fileExtension"
					value="${medicalReviewDocumentAssociationItems[medicalReviewDocumentAssociationItemIndex].fileExtension}"/>
			<form:errors path="medicalReviewDocumentAssociationItems[${medicalReviewDocumentAssociationItemIndex}].data" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>