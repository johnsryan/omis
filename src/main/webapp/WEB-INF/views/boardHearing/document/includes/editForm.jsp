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
 <%@ page trimDirectiveWhitespaces="true" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 <fmt:setBundle basename="omis.msgs.common" var="common"/>
 <fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
 <fmt:bundle basename="omis.boardhearing.msgs.boardHearingDocument">
 <form:form commandName="boardHearingDocumentForm" class="editForm" method="post" enctype="multipart/form-data">
 	<fieldset>
 		<span class="fieldGroup">
 			<form:label path="title" class="fieldLabel">
 				<fmt:message key="titleLabel" />
 			</form:label>
 			<form:input path="title"/>
 			<form:errors path="title" cssClass="error"/>
 		</span>
 		<span class="fieldGroup">
 			<form:label path="date" class="fieldLabel">
 				<fmt:message key="dateLabel" />
 			</form:label>
 			<form:input path="date" class="date"/>
 			<form:errors path="date" cssClass="error"/>
 		</span>
 		<c:set var="form" value="${boardHearingDocumentForm}" scope="request" />
 		<jsp:include page="/WEB-INF/views/document/includes/documentTags.jsp"/>
 		<c:choose>
 		<c:when test="${empty boardHearingAssociableDocument}">
 		<span class="fieldGroup">
 			<form:label path="data" class="fieldLabel">
 				<fmt:message key="documentLabel" />
 			</form:label>
 			<input id="documentData" type="file" name="data">
 			<form:hidden path="fileExtension" id="dataFileExtension"/>
 			<form:errors path="data" cssClass="error"/>
 		</span>
 		</c:when>
 		<c:otherwise>
 			<form:input type="hidden" path="data" />
 			<span class="fieldGroup">
 				<form:label path="data" class="fieldLabel">
 					<fmt:message key="documentLabel" />
 				</form:label>
 				<c:set var="filename" value = "${boardHearingAssociableDocument.document.filename}"/>
 				<a id="boardHearingDocumentDownloadLink" href="${pageContext.request.contextPath}/boardHearing/document/retrieveFile.html?document=${boardHearingAssociableDocument.document.id}" class="downloadLink"> 
 				<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
 					<fmt:formatDate value="${boardHearingAssociableDocument.document.date}" pattern="MM/dd/yyyy" var="documentDate"/>
 					<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
 				</fmt:message>
 				</a>
 			</span>
 		</c:otherwise>
 		</c:choose>
 	</fieldset>
 	
 	<c:if test="${not empty boardHearingAssociableDocument}">
	<c:set var="updatable" value="${boardHearingAssociableDocument}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle="${common}"/>"/>
	</p>
 </form:form>
 </fmt:bundle>