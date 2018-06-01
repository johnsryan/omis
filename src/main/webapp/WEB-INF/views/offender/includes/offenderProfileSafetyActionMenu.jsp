<!-- 
 - Author: Josh Divine
 - Version: 0.1.0 (Oct  20, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/profilePREAAcknowledgementReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="safetyPREAAcknowledgementReportLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/offender/profileSafetyDetailsReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="safetyDetailsReportLinkLabel"/></a>
			</li>			
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>