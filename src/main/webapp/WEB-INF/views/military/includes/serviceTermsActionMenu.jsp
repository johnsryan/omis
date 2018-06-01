<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 18, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.military.msgs.military">
	<ul>
		<sec:authorize access="hasRole('MILITARY_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/military/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createMilitaryServiceTermLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('MILITARY_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/military/militaryServiceTermListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="militaryListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>