<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (February 10, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.bedplacement.msgs.bedPlacement">
	<ul>
		<sec:authorize access="hasRole('BED_PLACEMENT_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/bedPlacement/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="newBedPlacementReviewLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('BED_PLACEMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/bedPlacement/bedPlacementListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="bedPlacementListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>