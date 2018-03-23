 <%@ page trimDirectiveWhitespaces="true" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 <fmt:setBundle basename="omis.msgs.common" var="common"/>
 <fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
 <fmt:bundle basename="omis.assessment.msgs.assessmentDocument">
 <form:form commandName="assessmentDocumentForm" class="editForm" method="post" enctype="multipart/form-data">
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
 		<c:set var="form" value="${assessmentDocumentForm}" scope="request" />
 		<jsp:include page="/WEB-INF/views/document/includes/documentTags.jsp"/>
 		<c:choose>
 		<c:when test="${empty assessmentDocumentAssociation}">
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
 				<c:set var="filename" value = "${assessmentDocumentAssociation.document.filename}"/>
 				<a id="assessmentDocumentDownloadLink" href="${pageContext.request.contextPath}/assessment/document/retrieveFile.html?document=${assessmentDocumentAssociation.document.id}" class="downloadLink"> 
 				<fmt:message key="titleExtensionFormat" bundle="${documentBundle}">
 					<fmt:formatDate value="${assessmentDocumentAssociation.document.date}" pattern="MM/dd/yyyy" var="documentDate"/>
 					<fmt:param value="${filename}"/><fmt:param value="${documentDate}"/>
 				</fmt:message>
 				</a>
 			</span>
 		</c:otherwise>
 		</c:choose>
 	</fieldset>
 	
 	<c:if test="${not empty assessmentDocumentAssociation}">
	<c:set var="updatable" value="${assessmentDocumentAssociation}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle="${common}"/>"/>
	</p>
</form:form>
 </fmt:bundle>