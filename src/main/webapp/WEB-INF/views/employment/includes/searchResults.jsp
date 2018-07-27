<%--
  - Victim search results.
  -
  - Author: Yidong Li
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.employment.msgs.employment">
	<table id="searchResults" class="listTable">
		<thead>
		<tr>
			<th></th>
			<th><fmt:message key="employerLabel"/>
			<th><fmt:message key="addressLabel"/>
			<th><fmt:message key="telephoneNumberLabel"/>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="employerSummary" items="${employerSummaries}">
			<c:set var="telephoneNumber">${employerSummary.telephoneNumber}</c:set>
			<tr>
				<td>
					<sec:authorize access="hasRole('ADMIN') or hasRole('EMPLOYMENT_TERM_CREATE')">
						<a class="createLink" href="${pageContext.request.contextPath}/employment/create.html?offender=${employerSearchForm.offender.id}&exist=true&employer=${employerSummary.id}"><span class="visibleLinkLabel"><fmt:message key="createNewEmploymentTermLabel"/></span></a>
					</sec:authorize>
				</td>
				<td>
					<c:out value="${employerSummary.organizationName}"/>
				</td>
				<td>
					<c:out value="${employerSummary.getAddressValue()}"/>,
					<c:out value="${employerSummary.getAddressCityName()}"/>, <c:if test="${not empty employerSummary.getAddressStateAbbreviation()}"> <c:out value="${employerSummary.getAddressStateAbbreviation()}"/> </c:if><c:out value="${employerSummary.getAddressZipCodeValue()}"/> <c:if test="${not empty employerSummary.getAddressZipCodeExtension()}"><c:out value= "- ${employerSummary.getAddressZipCodeExtension()}"/></c:if>, <c:out value="${employerSummary.getAddressCountryName()}"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${fn:length(telephoneNumber) eq 10}">
							<c:out value="(${fn:substring(telephoneNumber, 0,3)})"/>
							<c:out value="${fn:substring(telephoneNumber, 3,6)}-"/>
							<c:out value="${fn:substring(telephoneNumber, 6,10)}"/>
						</c:when>
						<c:when test="${fn:length(telephoneNumber) eq 7}">
							<c:out value="${fn:substring(telephoneNumber, 0,3)}-"/>
							<c:out value="${fn:substring(telephoneNumber, 3,7)}"/>
						</c:when>
						<c:otherwise>
							<c:out value="${telephoneNumber}"/>
						</c:otherwise>
					</c:choose>			
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</fmt:bundle>