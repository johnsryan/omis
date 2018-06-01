<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (Oct  28, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.military.msgs.military">
	<ul>
		<sec:authorize access="hasRole('MILITARY_VIEW') or hasRole('MILITARY_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/military/edit.html?serviceTerm=${serviceTerm.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('MILITARY_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/military/remove.html?serviceTerm=${serviceTerm.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('MILITARY_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty serviceTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/military/militaryServiceTermDetailsReport.html?serviceTerm=${serviceTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="militaryDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>