<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.workrestriction.msgs.workRestriction">
	<ul>
		<sec:authorize access="hasRole('WORK_RESTRICTION_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/workRestriction/edit.html?workRestriction=${workRestriction.id}" ><span class="visibleLinkLabel"><fmt:message key="editWorkRestrictionLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('WORK_RESTRICTION_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/workRestriction/remove.html?workRestriction=${workRestriction.id}"><span class="visibleLinkLabel"><fmt:message key="removeWorkRestrictionLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('WORK_RESTRICTION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty workRestriction}">
			<li>
				<a href="${pageContext.request.contextPath}/workRestriction/workRestrictionDetailsReport.html?workRestriction=${workRestriction.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="workRestrictionDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>