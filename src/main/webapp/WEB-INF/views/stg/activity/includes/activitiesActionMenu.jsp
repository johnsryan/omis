<%-- Author: Trevor Isles --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.stg.msgs.stg">
<ul>
	<sec:authorize access="hasRole('STG_ACTIVITY_CREATE') or hasRole('ADMIN')">
		<c:if test="${not empty offender and empty activity}"><li><a class="createLink" href="${pageContext.request.contextPath}/stg/activity/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createStgActivityLink"/></span></a></li>	
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_ACTIVITY_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty activity}">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/stg/activity/edit.html?activity=${activity.id}&offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="viewStgActivityEditLink"/>
				</span>
			</a>
		</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_ACTIVITY_REMOVE') or hasRole('ADMIN')">
		<c:if test="${not empty activity}">
		<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/stg/activity/remove.html?activity=${activity.id}&offender=${offender.id}">
				<span class="visibleLinkLabel"><fmt:message key="removeStgActivityLink"/>
				</span>
			</a>
		</li>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasRole('STG_ACTIVITY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty activity}">
			<li>
				<a href="${pageContext.request.contextPath}/stg/stgActivityDetailsReport.html?activity=${activity.id}&offender=${offender.id}&reportFormat=PDF" class="newTab printLink">
					<fmt:message key="stgActivityDetailsReportLinkLabel"/>
				</a>
			</li>
			</c:if>
	</sec:authorize>
</ul>
</fmt:bundle>