<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
	<ul>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_CREATE') or hasRole('PRESENTENCE_INVESTIGATION_REQUEST_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createPresentenceInvestigationDelayItemLink" class="createLink" 
					href="${pageContext.request.contextPath}/presentenceInvestigation/request/createPresentenceInvestigationDelayItem.html?presentenceInvestigationDelayItemIndex=${presentenceInvestigationDelayItemIndex}"><span class="visibleLinkLabel"><fmt:message key="addPresentenceInvestigationDelayLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>