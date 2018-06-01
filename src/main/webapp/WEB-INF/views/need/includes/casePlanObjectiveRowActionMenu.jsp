<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (July 15, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.need.msgs.need">
	<ul>
		<sec:authorize access="hasRole('NEED_MODULE') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/need/casePlanObjective/edit.html?objective=${casePlanObjective.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLabel"/></span></a>
			</li>
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/need/casePlanObjective/remove.html?objective=${casePlanObjective.id}"><span class="visibleLinkLabel"><fmt:message key="removeLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('NEED_MODULE') or hasRole('ADMIN')">
			<c:if test="${not empty casePlanObjective}">
			<li>
				<a href="${pageContext.request.contextPath}/need/casePlanObjective/casePlanObjectiveDetailsReport.html?objective=${casePlanObjective.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="casePlanObjectiveDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>