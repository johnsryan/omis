<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<form>
		<div id="div">
		<c:choose>
			<c:when test="${travelMethod.name eq 'Private Vehicle'}">
				<legend id="legend"><fmt:message key="privateVehicleLabel"/></legend>
			</c:when>
			<c:when test="${travelMethod.name eq 'Airplane'}">
				<legend id="legend"><fmt:message key="airplaneLabel"/></legend>
			</c:when>
			<c:when test="${travelMethod.name eq 'Bus'}">
				<legend id="legend"><fmt:message key="busLabel"/></legend>
			</c:when>
			<c:when test="${travelMethod.name eq 'Train'}">
				<legend id="legend"><fmt:message key="trainLabel"/></legend>
			</c:when>
			<c:otherwise>
				<legend id="legend"></legend>
			</c:otherwise>
		</c:choose>
		<c:if test="${travelMethod.descriptionRequired}">
			<label for="descriptionName">Vehicle Year-Make-Model-Color</label>
			<input type="text" name="descriptionName"/> 
			<form:errors path="descriptionName" cssClass="error"/>
		</c:if>
		<c:if test="${travelMethod.name eq 'Private Vehicle'}">
			<label for="plateNumber">Plate Number</label>
			<input type="text" name="plateNumber" /> 
			<form:errors path="plateNumber" cssClass="error"/>
		</c:if>
		<c:if test="${travelMethod.name eq 'Airplane'}">
			<label for="flightNumber">Flight Number</label>
			<input type="text" name="flightNumber" /> 
			<form:errors path="flightNumber" cssClass="error"/>
		</c:if>
		<c:if test="${travelMethod.name eq 'Bus'}">
			<label for="busNumber">Bus Number</label>
			<input type="text" name="busNumber" /> 
			<form:errors path="busNumber" cssClass="error"/>
		</c:if>
		<c:if test="${travelMethod.name eq 'Train'}">
			<label for="trainNumber">Train Number</label>
			<input type="text" name="trainNumber" /> 
			<form:errors path="trainNumber" cssClass="error"/>
		</c:if>
	</div>
</form>
</fmt:bundle>