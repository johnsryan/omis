<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (October 28, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.bedplacement.msgs.bedPlacement">
	<ul>
		<sec:authorize access="hasRole('BED_PLACEMENT_VIEW') or hasRole('BED_PLACEMENT_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/bedPlacement/edit.html?bedPlacement=${bedPlacement.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('BED_PLACEMENT_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/bedPlacement/remove.html?bedPlacement=${bedPlacement.id}"><span class="visibleLinkLabel"><fmt:message key="removeLabel"/></span></a>
			</li>
		</sec:authorize>
		<c:if test="${not bedPlacement.confirmed}">
			<sec:authorize access="hasRole('BED_PLACEMENT_EDIT') or hasRole('ADMIN')">
				<li>
					<a class="authorizeLink" id="bedPlacement${bedPlacement.id}CompleteLink" href="${pageContext.request.contextPath}/bedPlacement/completeMove.html?bedPlacement=${bedPlacement.id}"><span class="visibleLinkLabel"><fmt:message key="confirmPlacementLabel"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<sec:authorize access="hasRole('BED_PLACEMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty bedPlacement}">
			<li>
				<a href="${pageContext.request.contextPath}/bedPlacement/bedPlacementDetailsReport.html?bedPlacement=${bedPlacement.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="bedPlacementDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>