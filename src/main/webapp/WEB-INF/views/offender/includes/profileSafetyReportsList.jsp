<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.profile">
		<sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<span>
				<a href="${pageContext.request.contextPath}/offender/profileSafetyDetailsReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="safetyDetailsReportLinkLabel"/></a>
			</span>
			</c:if>
		</sec:authorize>
</fmt:bundle>