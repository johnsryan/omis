<?xml version="1.0" encoding="UTF-8"?>
<%-- Author: Ryan Johns
 - Version: 0.1.0 (Dec 14, 2015)
 - Since: OMIS 3.0 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.document.msgs.document" var="documentBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.courtdocument.msgs.document">
<ul>
	<li>
		<a href="${pageContext.request.contextPath}/documents/retrieveFile.html?document=${courtCaseDocumentAssociation.document.id}" class="downloadLink">
			<fmt:message key="downloadLinkLabel" bundle="${documentBundle}"/>
		</a>
	</li>
	<li>
		<c:set var="mFilter" value="${filter}"/>
		<a href="${pageContext.request.contextPath}/courtCase/document/edit.html?courtCaseDocumentAssociation=${courtCaseDocumentAssociation.id}&filter=${mFilter}" class="viewLink">
			<fmt:message key="editLabel" bundle="${commonBundle}"/>
		</a>
	</li>
	<li>
		<a href="${pageContext.request.contextPath}/courtCase/document/remove.html?courtCaseDocumentAssociation=${courtCaseDocumentAssociation.id}&filter=${mFilter}" class="removeLink">
			<fmt:message key="removeLink" bundle="${commonBundle}"/>
		</a>
	</li>
		<sec:authorize access="hasRole('COURT_CASE_DOCUMENT_VIEW') or hasRole('ADMIN')">
	    <c:if test="${not empty courtCaseDocumentAssociation}">
	        <li>
	            <a href="${pageContext.request.contextPath}/courtCase/document/courtDocumentDetailsReport.html?courtCaseDocumentAssociation=${courtCaseDocumentAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="courtDocumentDetailsReportLinkLabel"/></a>
	        </li>
	    </c:if>
	</sec:authorize>
</ul>
</fmt:bundle>