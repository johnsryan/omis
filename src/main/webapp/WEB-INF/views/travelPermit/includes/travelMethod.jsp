<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle var="commonBundle" basename="omis.msgs.common"/>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
		<div id="div">
			<c:if test="${travelMethod.descriptionRequired}">
				<c:out value="${travelMethod.descriptionName}"/>
				<input type="text" name="vehicleInfo" value="${travelPermitForm.vehicleInfo}"/> 
				<form:errors path="vehicleInfo" cssClass="error"/>
				<br>
				<br>
			</c:if>
			<c:out value="${travelMethod.numberName}"/>
			<input type="text" name="plateNumber" value="${travelPermitForm.plateNumber}"/> 
			<form:errors path="plateNumber" cssClass="error"/>
		</div>
</fmt:bundle>