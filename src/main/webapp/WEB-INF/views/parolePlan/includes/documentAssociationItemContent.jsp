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
 - Document association fields for parole plans.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 13, 2018)
 - Since: OMIS 3.0
 --%><?xml version="1.0" encoding="UTF-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
<fmt:bundle basename="omis.paroleplan.msgs.parolePlan">
	<span id="parolePlanDocumentAssociationItem${documentAssociationItemIndex}" class="documentItem">
		<a class="removeLink" id="removeParolePlanDocumentAssociationLink${documentAssociationItemIndex}"
		href="${pageContext.request.contextPath}/parolePlan/removeParolePlanDocumentAssociation.html?parolePlan=${parolePlan.id}">
			<fmt:message key="removeLink" bundle="${commonBundle}"/>
		</a>
		<input type="hidden" id="parolePlanDocumentAssociationId${documentAssociationItemIndex}"
				name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].parolePlanDocumentAssociation"
				value="${parolePlanDocumentAssociationItem.parolePlanDocumentAssociation.id}"/>
		<form:errors path="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].parolePlanDocumentAssociation" cssClass="error"/>
		<input type="hidden" id="parolePlanDocumentAssociationOperation${documentAssociationItemIndex}"
				name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" value="${parolePlanDocumentAssociationItem.itemOperation}"/>
		<form:errors path="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].itemOperation" cssClass="error"/>
		
		<span class="fieldGroup">
			<label for="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].title" class="fieldLabel">
				<fmt:message key="documentTitleLabel"/>
			</label>
			<input type="text" id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].title"
					name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].title"
				value="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].title}" />
			<form:errors path="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].title" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<label for="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="fieldLabel">
				<fmt:message key="fileDateLabel"/>
			</label>
			<fmt:formatDate var="parolePlanDocumentAssociationDate" value="${parolePlanDocumentAssociationItem.fileDate}" pattern="MM/dd/yyyy"/>
			<input type="text" value="${parolePlanDocumentAssociationDate}" id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileDate"
					name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" class="date" />
			<form:errors path="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileDate" cssClass="error"/>
		</span>
		
		<span class="fieldGroup">
			<a id="createDocumentTagItemLink${documentAssociationItemIndex}" class="createLink addLink">
				<span class="visibleLinkLabel">
					<fmt:message key="addTagLink"/>
				</span>
			</a>
		</span>
		
		<span id="documentTagItems${documentAssociationItemIndex}">
			<c:forEach var="documentTagItem" items="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].documentTagItems}" varStatus="status">
				<c:set var="documentTagItem" value="${documentTagItem}" scope="request"/>
				<c:set var="documentTagItemIndex" value="${status.index}" scope="request"/>
				<c:if test="${not empty documentTagItem.itemOperation}">
					<jsp:include page="documentAssociationTagItemContent.jsp"/>
				</c:if>
			</c:forEach>
		</span>
		
		<span class="fieldGroup">
			<label for="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data" class="fieldLabel">
				<fmt:message key="documentLabel"/>
			</label>
			<c:choose>
				<c:when test="${not empty parolePlanDocumentAssociationItem.document}">
					<c:set var="filename" value = "${parolePlanDocumentAssociationItems[documentAssociationItemIndex].document.filename}"/>
					<fmt:formatDate value="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].document.date}"
							pattern="MM/dd/yyyy" var="documentDate"/>
					<a id="parolePlanDocumentAssociationDownloadLink${documentAssociationItemIndex}"
		 					href="${pageContext.request.contextPath}/parolePlan/retrieveFile.html?document=${parolePlanDocumentAssociationItems[documentAssociationItemIndex].document.id}"
							class="downloadLink">
						<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
							<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
						</fmt:message>
					</a>
					<input type="hidden" name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data"
						id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data"
						value="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].data}"/>
					<input type="hidden" name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].document"
						id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].document"
						value="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].document.id}"/>
				</c:when>
				<c:otherwise>
						<input id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data" type="file"
								name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data" />
				</c:otherwise>
			</c:choose>
			
			<input type="hidden" name="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					id="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].fileExtension"
					value="${parolePlanDocumentAssociationItems[documentAssociationItemIndex].fileExtension}"/>
			<form:errors path="parolePlanDocumentAssociationItems[${documentAssociationItemIndex}].data" cssClass="error"/>
		</span>
	</span>
</fmt:bundle>