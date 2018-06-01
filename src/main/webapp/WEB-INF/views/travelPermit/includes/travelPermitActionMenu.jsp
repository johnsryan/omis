<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (May 30, 2018)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<ul>
		<sec:authorize access="hasRole('TRAVEL_PERMIT_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/travelPermit/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listTravelPermitLink"/></span>
				</a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>