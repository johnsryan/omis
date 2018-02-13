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
 - Document association fields for parole reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Jan 30, 2018)
 - Since: OMIS 3.0
 --%><?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
<fmt:bundle basename="omis.parolereview.msgs.paroleReview">
	<span id="paroleReviewDocumentAssociationItem${documentAssociationItemIndex}" class="documentItem">
		<a class="removeLink" id="removeParoleReviewDocumentAssociationLink${documentAssociationItemIndex}"
		href="${pageContext.request.contextPath}/paroleReview/removeParoleReviewDocumentAssociation.html?paroleReview=${paroleReview.id}">
			<fmt:message key="removeLink" bundle="${commonBundle}"/>
		</a>
		<input type="hidden" id="paroleReviewDocumentAssociationId${documentAssociationItemIndex}"
				name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].paroleReviewDocumentAssociation"
				value="${paroleReviewDocumentAssociationItem.paroleReviewDocumentAssociation.id}"/>
		<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].paroleReviewDocumentAssociation" cssClass="error"/>
		<input type="hidden" id="paroleReviewDocumentAssociationOperation${documentAssociationItemIndex}"
				name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" value="${paroleReviewDocumentAssociationItem.itemOperation}"/>
		<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" cssClass="error"/>
		
		<span class="fieldGroup">
			<label for="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].title" class="fieldLabel">
				<fmt:message key="documentTitleLabel"/>
			</label>
			<input type="text" id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].title"
					name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].title"
				value="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].title}" />
			<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].title" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<label for="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="fieldLabel">
				<fmt:message key="fileDateLabel"/>
			</label>
			<fmt:formatDate var="paroleReviewDocumentAssociationDate" value="${paroleReviewDocumentAssociationItem.fileDate}" pattern="MM/dd/yyyy"/>
			<input type="text" value="${paroleReviewDocumentAssociationDate}" id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate"
					name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="date" />
			<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<a id="createDocumentTagItemLink${documentAssociationItemIndex}" class="createLink addLink">
				<span class="visibleLinkLabel">
					<fmt:message key="addTagLink"/>
				</span>
			</a>
		</span>
		
		<span id="documentTagItems${documentAssociationItemIndex}">
			<c:forEach var="documentTagItem" items="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].documentTagItems}" varStatus="status">
				<c:set var="documentTagItem" value="${documentTagItem}" scope="request"/>
				<c:set var="documentTagItemIndex" value="${status.index}" scope="request"/>
				<c:if test="${not empty documentTagItem.itemOperation}">
					<jsp:include page="documentAssociationTagItemContent.jsp"/>
				</c:if>
			</c:forEach>
		</span>
		
		<span class="fieldGroup">
			<label for="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" class="fieldLabel">
				<fmt:message key="documentLabel"/>
			</label>
			<c:choose>
				<c:when test="${not empty paroleReviewDocumentAssociationItem.document}">
					<c:set var="filename" value = "${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].document.filename}"/>
					<fmt:formatDate value="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].document.date}"
							pattern="MM/dd/yyyy" var="documentDate"/>
					<a id="paroleReviewDocumentAssociationDownloadLink${documentAssociationItemIndex}"
		 					href="${pageContext.request.contextPath}/paroleReview/retrieveFile.html?document=${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].document.id}"
							class="downloadLink">
						<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
							<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
						</fmt:message>
					</a>
					<input type="hidden" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data"
						id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data"
						value="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].data}"/>
					<input type="hidden" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].document"
						id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].document"
						value="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].document.id}"/>
				</c:when>
				<c:otherwise>
						<input id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" type="file"
								name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" />
				</c:otherwise>
			</c:choose>
			
			<input type="hidden" name="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					id="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					value="${paroleReviewDocumentAssociationItems[documentAssociationItemIndex].fileExtension}"/>
			<form:errors path="paroleReviewDocumentAssociationItems[${documentAssociationItemIndex}].data" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>