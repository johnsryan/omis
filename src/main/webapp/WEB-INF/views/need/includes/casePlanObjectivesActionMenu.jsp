<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (July 14, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.need.msgs.need">
	<ul>
		<sec:authorize access="hasRole('CASE_PLAN_OBJECTIVE_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/need/casePlanObjective/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="newCasePlanObjectiveLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('NEED_MODULE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/need/casePlanObjective/casePlanObjectiveListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="casePlanObjectiveListingReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>