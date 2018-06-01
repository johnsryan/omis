<%-- Stephen Abson --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.tierdesignation.msgs.tierDesignation">
	<ul>
		<sec:authorize access="hasRole('TIER_DESIGNATION_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/tierDesignation/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createTierDesignationLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('TIER_DESIGNATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/tierDesignation/tierDesignationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="tierDesignationListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
    	<sec:authorize access="hasRole('TIER_DESIGNATION_VIEW') or hasRole('ADMIN')">
    		<c:if test="${not empty tierDesignation}">
    		<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/tierDesignation/edit.html?tierDesignation=${tierDesignation.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditTierDesignationLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('TIER_DESIGNATION_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty tierDesignation}">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/tierDesignation/remove.html?tierDesignation=${tierDesignation.id}"><span class="visibleLinkLabel"><fmt:message key="removeTierDesignationLink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('TIER_DESIGNATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty tierDesignation}">
			<li>
				<a href="${pageContext.request.contextPath}/tierDesignation/tierDesignationDetailsReport.html?tierDesignation=${tierDesignation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="tierDesignationDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>