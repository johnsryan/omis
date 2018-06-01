<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 18, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.separationneed.msgs.separationNeed">
	<ul>
		<sec:authorize access="hasRole('SEPARATION_NEED_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/separationNeed/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="newSeparationNeedLabel"/></span></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('SEPARATION_NEED_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/separationNeed/separationNeedListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="separationNeedListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
			<li>
				<a href="#" id="l1" class="tableViewLink filterSeparationNeedRow"><fmt:message key="separationNeedTableViewAllLinkLabel"/></a>
			</li>
			<li>
				<a href="#" id="l2" class="tableViewLink filterActive"><fmt:message key="separationNeedTableViewActiveLinkLabel"/></a>
			</li>
			<li>
				<a href="#" id="l3" class="tableViewLink filterInactive"><fmt:message key="separationNeedTableViewInactiveLinkLabel"/></a>
			</li>
			<li>
				<a href="#" id="l4" class="tableViewLink filterRemoved"><fmt:message key="separationNeedTableViewRemovedLinkLabel"/></a>
			</li>
	</ul>
</fmt:bundle>