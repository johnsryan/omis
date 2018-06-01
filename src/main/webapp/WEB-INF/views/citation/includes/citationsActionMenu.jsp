<!-- 
 - Author: Trevor Isles
 - Version: 0.1.0 (Aug 22, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.citation.msgs.citation">
	<ul>
		<sec:authorize access="hasRole('MISDEMEANOR_CITATION_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
			<a class="createLink" href="${pageContext.request.contextPath}/citation/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createCitationLink"/></span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('MISDEMEANOR_CITATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/citation/misdemeanorCitationsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="citationListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('MISDEMEANOR_CITATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty citation}">
			<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/citation/edit.html?citation=${citation.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="viewEditCitationLink"/>
				</span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('MISDEMEANOR_CITATION_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty citation}">
			<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/citation/remove.html?citation=${citation.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="removeCitationLink"/>
				</span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('MISDEMEANOR_CITATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty citation}">
			<li>
				<a href="${pageContext.request.contextPath}/citation/misdemeanorCitationsDetailsReport.html?citation=${citation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="citationDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>