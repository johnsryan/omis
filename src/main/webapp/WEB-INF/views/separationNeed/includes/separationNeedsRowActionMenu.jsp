<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (August 17, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.separationneed.msgs.separationNeed">
	<ul>
		<sec:authorize access="hasRole('SEPARATION_NEED_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/separationNeed/edit.html?separationNeed=${separationNeed.id}&&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SEPARATION_NEED_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/separationNeed/remove.html?separationNeed=${separationNeed.id}"><span class="visibleLinkLabel"><fmt:message key="removeLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SEPARATION_NEED_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty separationNeed}">
			<li>
				<a href="${pageContext.request.contextPath}/separationNeed/separationNeedDetailsReport.html?separationNeed=${separationNeed.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="separationNeedDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>