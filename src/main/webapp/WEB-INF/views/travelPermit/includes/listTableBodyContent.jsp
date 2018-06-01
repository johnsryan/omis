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
				href="${pageContext.request.contextPath}/travelPermit/travelPermitsRowActionMenu.html?offender=${offender.id}&travelPermits=${travelPermit.id}"></a>
			</td>
			<td><fmt:formatDate value="${travelPermit.startDate}" pattern="MM/dd/yyyy"/></td>
			<td><fmt:formatDate value="${travelPermit.endDate}" pattern="MM/dd/yyyy"/></td>
			<td><c:out value="${travelPermit.destination}"/></td>
			<td><c:out value="${travelPermit.periodicity}"/></td>
			<td><fmt:formatDate var="createdDate"
					value="${travelPermit.createdDate}" pattern="MM/dd/yyyy" /> <fmt:message
					key="creationSignatureLabel">
					<fmt:param value="${travelPermit.createdByLastName}" />
					<fmt:param value="${travelPermit.createdByFirstName}" />
					<fmt:param value="${travelPermit.createdByUsername}" />
					<fmt:param value="${createdDate}" />
				</fmt:message>
			</td>
		</tr>
	</c:forEach>