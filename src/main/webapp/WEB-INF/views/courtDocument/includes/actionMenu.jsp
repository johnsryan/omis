<%-- Author: Ryan Johns
 - Version: 0.1.0 (Dec 3, 2015)
 - Since: OMIS 3.0 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.courtdocument.msgs.document">
<ul>
	<li>
		<a href="${pageContext.request.contextPath}/courtCase/document/create.html?offender=${offender.id}" class="createLink">
			<fmt:message key="createLinkLabel"/>
		</a>
	</li>
	<sec:authorize access="hasRole('COURT_CASE_DOCUMENT_VIEW') or hasRole('ADMIN')">
	    <c:if test="${not empty offender}">
	        <li>
	            <a href="${pageContext.request.contextPath}/courtCase/document/courtDocumentListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="courtDocumentListingReportLinkLabel"/></a>
	        </li>
	    </c:if>
	</sec:authorize>
</ul>
</fmt:bundle>