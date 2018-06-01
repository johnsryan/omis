<!-- Author: Ryan Johns
 - Version: 0.1.0 (Jun 8, 2016)
 - Since: OMIS 3.0 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.vehicle.msgs.vehicle">
	<ul>
		<sec:authorize access="hasRole('VEHICLE_VIEW') or hasRole('VEHICLE_EDIT') or hasRole('ADMIN')">
		<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/vehicle/edit.html?vehicleAssociation=${vehicleAssociation.id}" 
				title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
		</li>
 		</sec:authorize> 
		<sec:authorize access="hasRole('VEHICLE_REMOVE') or hasRole('ADMIN')">
		<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/vehicle/remove.html?vehicleAssociation=${vehicleAssociation.id}" 
				title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VEHICLE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty vehicleAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/vehicle/vehicleDetailsReport.html?vehicleAssociation=${vehicleAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="vehicleDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>