<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (May 23, 2018)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<ul>
		<sec:authorize access="hasRole('TRAVEL_PERMIT_VIEW') or hasRole('ADMIN')">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/travelPermit/edit.html?travelPermitSummaries=${travelPermitSummaries.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="viewEditLink" bundle="${commonBundle}"/>
				</span>
			</a>
		</li>
		</sec:authorize>			
		<sec:authorize access="hasRole('TRAVEL_PERMIT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty travelPermit}">
			<li>
				<a href="${pageContext.request.contextPath}/travelPermit/travelPermitDetailsReport.html?travelPermit=${travelPermit.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="travelPermitDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('TRAVEL_PERMIT_VIEW') or hasRole('ADMIN')">
		<li>
			<a class="copyLink" href="${pageContext.request.contextPath}/travelPermit/create.html?offender=${offender.id}&travelPermit=${travelPermit.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="recreateTravelPermitTitle"/>
				</span>
			</a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('TRAVEL_PERMIT_REMOVE') or hasRole('ADMIN')">
		<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/travelPermit/remove.html?travelPermitSummaries=${travelPermitSummaries.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="removeLink" bundle="${commonBundle}"/>
				</span>
			</a>
		</li>
		</sec:authorize>
	</ul>
</fmt:bundle>