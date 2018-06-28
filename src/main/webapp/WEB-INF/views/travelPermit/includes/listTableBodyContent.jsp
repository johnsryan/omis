<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.travelpermit.msgs.travelPermit" var="travelPermitBundle"/>
	<c:forEach var="travelPermit" items="${travelPermits}" varStatus="status">
		<c:set var="travelPermit" value="${travelPermit}" scope="request"/>
		<tr>
			<td><a class="actionMenuItem rowActionMenuLinks" id="summaryActionMenuLink${status.index}"
				href="${pageContext.request.contextPath}/travelPermit/travelPermitsRowActionMenu.html?offender=${offender.id}&travelPermit=${travelPermit.id}"></a>
			</td>
			<td><fmt:formatDate value="${travelPermit.startDate}" pattern="MM/dd/yyyy"/></td>
			<td><fmt:formatDate value="${travelPermit.endDate}" pattern="MM/dd/yyyy"/></td>
			<td><c:out value="${travelPermit.destinationName}"/></td>
			<td><c:out value="${travelPermit.periodicityName}"/></td>
			<td><fmt:message key="creationSignatureLabel" bundle="${travelPermitBundle}">
					<c:choose>
						<c:when test="${not empty travelPermit.createdByMiddleName}">
							<c:set value="${travelPermit.createdByMiddleName}" var="middleName"/>
						</c:when>
						<c:otherwise>
							<c:set value="" var="middleName"/>
						</c:otherwise>
					</c:choose>		
					<c:choose>
						<c:when test="${not empty travelPermit.createdBySuffix}">
							<c:set value="${travelPermit.createdBySuffix}" var="suffix"/>
						</c:when>
						<c:otherwise>
							<c:set value="" var="suffix"/>
						</c:otherwise>
					</c:choose>	
					<fmt:param value="${travelPermit.createdByLastName}" />
					<fmt:param value="${travelPermit.createdByFirstName}" />
					<fmt:param value="${middleName}" />
					<fmt:param value="${suffix}" />					
					<fmt:param value="${travelPermit.createdByUsername}" />
				</fmt:message></td>
		</tr>
	</c:forEach>