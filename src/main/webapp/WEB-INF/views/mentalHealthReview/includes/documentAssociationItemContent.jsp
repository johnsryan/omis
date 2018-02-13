<?xml version="1.0" encoding="UTF-8"?>
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
 - Document association fields for mental health reviews.
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
<fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
	<span id="mentalHealthReviewDocumentAssociationItem${documentAssociationItemIndex}" class="documentItem">
		<a class="removeLink" id="removeMentalHealthReviewDocumentAssociationLink${documentAssociationItemIndex}"
		href="${pageContext.request.contextPath}/mentalHealthReview/removeMentalHealthReviewDocumentAssociation.html?mentalHealthReview=${mentalHealthReview.id}">
			<fmt:message key="removeLink" bundle="${commonBundle}"/>
		</a>
		<input type="hidden" id="mentalHealthReviewDocumentAssociationId${documentAssociationItemIndex}"
				name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].mentalHealthReviewDocumentAssociation"
				value="${mentalHealthReviewDocumentAssociationItem.mentalHealthReviewDocumentAssociation.id}"/>
		<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].mentalHealthReviewDocumentAssociation" cssClass="error"/>
		<input type="hidden" id="mentalHealthReviewDocumentAssociationOperation${documentAssociationItemIndex}"
				name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" value="${mentalHealthReviewDocumentAssociationItem.itemOperation}"/>
		<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" cssClass="error"/>
		
		<span class="fieldGroup">
			<label for="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].title" class="fieldLabel">
				<fmt:message key="documentTitleLabel"/>
			</label>
			<input type="text" id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].title"
					name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].title"
				value="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].title}" />
			<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].title" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<label for="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="fieldLabel">
				<fmt:message key="fileDateLabel"/>
			</label>
			<fmt:formatDate var="mentalHealthReviewDocumentAssociationDate" value="${mentalHealthReviewDocumentAssociationItem.fileDate}" pattern="MM/dd/yyyy"/>
			<input type="text" value="${mentalHealthReviewDocumentAssociationDate}" id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate"
					name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="date" />
			<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<a id="createDocumentTagItemLink${documentAssociationItemIndex}" class="createLink addLink">
				<span class="visibleLinkLabel">
					<fmt:message key="addTagLink"/>
				</span>
			</a>
		</span>
		
		<span id="documentTagItems${documentAssociationItemIndex}">
			<c:forEach var="documentTagItem" items="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].documentTagItems}" varStatus="status">
				<c:set var="documentTagItem" value="${documentTagItem}" scope="request"/>
				<c:set var="documentTagItemIndex" value="${status.index}" scope="request"/>
				<c:if test="${not empty documentTagItem.itemOperation}">
					<jsp:include page="documentAssociationTagItemContent.jsp"/>
				</c:if>
			</c:forEach>
		</span>
		
		<span class="fieldGroup">
			<label for="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" class="fieldLabel">
				<fmt:message key="documentLabel"/>
			</label>
			<c:choose>
				<c:when test="${not empty mentalHealthReviewDocumentAssociationItem.document}">
					<c:set var="filename" value = "${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].document.filename}"/>
					<fmt:formatDate value="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].document.date}"
							pattern="MM/dd/yyyy" var="documentDate"/>
					<a id="mentalHealthReviewDocumentAssociationDownloadLink${documentAssociationItemIndex}"
		 					href="${pageContext.request.contextPath}/mentalHealthReview/retrieveFile.html?document=${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].document.id}"
							class="downloadLink">
						<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
							<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
						</fmt:message>
					</a>
					<input type="hidden" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data"
						id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data"
						value="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].data}"/>
					<input type="hidden" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].document"
						id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].document"
						value="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].document.id}"/>
				</c:when>
				<c:otherwise>
						<input id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" type="file"
								name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" />
				</c:otherwise>
			</c:choose>
			
			<input type="hidden" name="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					id="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					value="${mentalHealthReviewDocumentAssociationItems[documentAssociationItemIndex].fileExtension}"/>
			<form:errors path="mentalHealthReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>