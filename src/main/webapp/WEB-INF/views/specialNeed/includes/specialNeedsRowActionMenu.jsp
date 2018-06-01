<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 05, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.specialneed.msgs.specialNeed">
	<ul>
		<sec:authorize access="hasRole('SPECIAL_NEED_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/specialNeed/edit.html?specialNeed=${specialNeed.id}&specialNeedClassification=${classification.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SPECIAL_NEED_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/specialNeed/remove.html?specialNeed=${specialNeed.id}&specialNeedClassification=${classification.id}"><span class="visibleLinkLabel"><fmt:message key="removeLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SPECIAL_NEED_LIST') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/specialNeed/specialNeedDetailsReport.html?specialNeed=${specialNeed.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="specialNeedDetailsReportLinkLabel"/></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>