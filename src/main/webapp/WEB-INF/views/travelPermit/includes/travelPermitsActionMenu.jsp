<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (May 23, 2018)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<ul>
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/travelPermit/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createTravelPermitTitle"/>
				</span>
			</a>
		</li>
		<sec:authorize access="hasRole('TRAVEL_PERMITE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/travelPermit/travelPermitListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="travelPermitListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>