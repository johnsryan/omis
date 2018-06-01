<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="address" uri="/WEB-INF/tld/address.tld" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.residence.msgs.residence">
<c:forEach var="residenceSummary" items="${residenceSummaries}">
<c:set var="residenceSummary" value="${residenceSummary}" scope="request"/>
<tr>
	<td>			
		<c:choose>
			<c:when test="${residenceSummary.type.name eq 'RESIDENCE'}">
				<a class="actionMenuItem rowActionMenuItem" id="residenceActionMenuLink${residenceSummary.id}" href="${pageContext.request.contextPath}/residence/residenceRowActionMenu.html?residenceTerm=${residenceSummary.id}"></a>
			</c:when>
			<c:otherwise>
				<a class="actionMenuItem rowActionMenuItem" id="residenceActionMenuLink${residenceSummary.id}" href="${pageContext.request.contextPath}/residence/nonResidenceRowActionMenu.html?nonResidenceTerm=${residenceSummary.id}"></a>
			</c:otherwise>
		</c:choose>	
	</td>
	<td></td>
	<td><input type="hidden" name="residenceType" value="${residenceSummary.type.name}" id="residenceType"></td>
	<td><fmt:formatDate value="${residenceSummary.startDate}" pattern="MM/dd/yyyy"/></td>
	<td><fmt:formatDate value="${residenceSummary.endDate}" pattern="MM/dd/yyyy"/></td>
	<td>
	<c:choose>
		<c:when test="${residenceSummary.type eq 'RESIDENCE'}">
				<address:formatSummary value="${residenceSummary}" format="LINE1"/> <address:formatSummary value="${residenceSummary}" format="LINE2"/>
		</c:when>
		<c:otherwise>
		<c:if test="${residenceSummary.type eq 'NONRESIDENCE'}">
			<c:if test="${residenceSummary.residenceStatus.name ne 'HOMELESS'}">				
				<c:if test="${residenceSummary.location && residenceSummary.address}">
					<c:choose>						
						<c:when test="${not empty residenceSummary.addressValue}">
							<c:out value="${residenceSummary.locationName}"/> - <address:formatSummary value="${residenceSummary}" format="LINE1"/> <address:formatSummary value="${residenceSummary}" format="LINE2"/>
						</c:when>
						<c:otherwise>
							<c:out value="${residenceSummary.locationName}"/> - <address:formatSummary value="${residenceSummary}" format="LINE2"/>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:if>
		</c:if>
		</c:otherwise>
	</c:choose>	
	</td>
	<td>
	<c:if test="${residenceSummary.type eq 'RESIDENCE'}">
	<fmt:message key="residenceCategoryLabel.${residenceSummary.category.name}"/>	
	</c:if>
	</td>
	<td><fmt:message key="residenceStatusLabel.${residenceSummary.residenceStatus.name}"/></td>
<%-- 	<td class="comment"><c:out value="${residenceSummary.comment}"/></td> --%>
</tr>
</c:forEach>
</fmt:bundle>