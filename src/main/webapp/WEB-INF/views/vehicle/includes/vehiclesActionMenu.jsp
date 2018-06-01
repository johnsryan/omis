<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (Jan 13, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.vehicle.msgs.vehicle">
	<ul>
		<sec:authorize access="hasRole('VEHICLE_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/vehicle/create.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="createVehicleTitle"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VEHICLE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/vehicle/vehicleListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="vehicleListingReportLinkLabel"/></a>
			</li>
		</c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>