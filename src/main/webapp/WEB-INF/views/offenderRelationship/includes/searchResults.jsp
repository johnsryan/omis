<%--
  - Search results for offender relationships
  -
  - Author: Stephen Abson
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setBundle var="criminalAssociationBundle" basename="omis.criminalassociation.msgs.criminalAssociation"/>
<fmt:setBundle var="familyBundle" basename="omis.family.msgs.family"/>
<fmt:setBundle var="victimBundle" basename="omis.victim.msgs.victim"/>
<fmt:setBundle var="visitationBundle" basename="omis.visitation.msgs.visitation"/>
<fmt:setBundle var="offenderRelationshipBundle" basename="omis.offenderrelationship.msgs.offenderRelationship"/>
<table id="offenderRelationsSearchResults" class="listTable">
	<thead>
		<tr>
			<th></th>
			<th><fmt:message key="offenderRelationLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="addressLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="telephoneNumberLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="offenderCountLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="criminalAssociationCountLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="familyAssociationsCountLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="victimAssociationCountLabel" bundle="${offenderRelationshipBundle}"/></th>
			<th><fmt:message key="visitationAssociationCountLabel" bundle="${offenderRelationshipBundle}"/></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="offenderRelationshipSummary" items="${offenderRelationshipSummaries}">
		<tr>
			<td>
			<c:set var="relationshipCount" value="${offenderRelaitonshipSummary.relationshipCount}"/>
			<c:forEach var="option" items="${options}">
			<c:choose>
				<c:when test="${option.name eq 'CRIMINAL'}">
					<a href="${pageContext.request.contextPath}/criminalAssociation/create.html?associate=${offenderRelationshipSummary.id}&amp;offender=${offenderSummary.id}" title="<fmt:message key='createCriminalAssociationLink' bundle='${criminalAssociationBundle}'/>"><span class="invisibleLinkLabel"><fmt:message key='createCriminalAssociationLink' bundle='${criminalAssociationBundle}'/></span></a>
				</c:when>
				<c:when test="${option.name eq 'FAMILY_MEMBER'}">
					<a class="actionMenuItem rowActionMenuItem" id="actionMenuLink${status.index}" href="${pageContext.request.contextPath}/family/familyAssociationSearchRowActionMenu.html?familyMember=${offenderRelationshipSummary.id}&amp;offender=${offenderSummary.id}"></a>
				</c:when>
				<c:when test="${option.name eq 'VICTIM'}">
					<a class="actionMenuItem rowActionMenuItem" id="actionMenuLink${status.index}" href="${pageContext.request.contextPath}/victim/association/victimAssociationSearchRowActionMenu.html?offender=${offenderSummary.id}&amp;victim=${offenderRelationshipSummary.id}"></a>
				</c:when>
				<c:when test="${option.name eq 'VISITOR'}">
					<a class="actionMenuItem rowActionMenuItem" id="actionMenuLink${status.index}" href="${pageContext.request.contextPath}/visitation/visitationAssociationRowSearchActionMenu.html?visitor=${offenderRelationshipSummary.id}&amp;offender=${offenderSummary.id}"></a>
				</c:when>
				<c:when test="${option.name eq 'NEW_RELATION'}">
					<a class="actionMenuItem rowActionMenuItem" id="actionMenuLink${status.index}" href="${pageContext.request.contextPath}/offenderRelationship/offenderRelationshipSearchRowActionMenu.html?offender=${offenderSummary.id}&amp;relation=${offenderRelationshipSummary.id}"></a>
				</c:when>
				<c:otherwise>
					Error - unknown option: <c:out value="${option}"/>
				</c:otherwise>
			</c:choose>
			</c:forEach>
			</td>
			<td>
				<c:out value="${offenderRelationshipSummary.lastName}"/>,
				<c:out value="${offenderRelationshipSummary.firstName}"/>
				<c:if test="${not empty offenderRelationshipSummary.middleName}">
					<c:out value="${fn:substring(offenderRelationshipSummary.middleName, 0, 1)}"/>.
				</c:if>
				<c:if test="${not empty offenderRelationshipSummary.suffix}">
					<c:out value="${offenderRelationshipSummary.suffix}"/>
				</c:if>
				<c:if test="${not empty offenderRelationshipSummary.offenderNumber}">
					#<c:out value="${offenderRelationshipSummary.offenderNumber}"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.address}">
					<c:set var="addressSummarizable" value="${offenderRelationshipSummary}" scope="request"/>
					<jsp:include page="/WEB-INF/views/address/includes/addressSummary.jsp"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.telephoneNumber}">
					<c:set var="hidePrimary" value="${true}" scope="request"/>
					<c:set var="telephoneNumberSummarizable" value="${offenderRelationshipSummary}" scope="request"/>
					<jsp:include page="/WEB-INF/views/contact/includes/telephoneNumberSummary.jsp"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.offenderAssociationCount gt 0}">
					<c:out value="${offenderRelationshipSummary.offenderAssociationCount}"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.criminalAssociationCount gt 0}">
					<c:out value="${offenderRelationshipSummary.criminalAssociationCount}"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.familyMemberCount gt 0}">
					<c:out value="${offenderRelationshipSummary.familyMemberCount}"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.victimCount gt 0}">
					<c:out value="${offenderRelationshipSummary.victimCount}"/>
				</c:if>
			</td>
			<td>
				<c:if test="${offenderRelationshipSummary.visitorCount gt 0}">
					<c:out value="${offenderRelationshipSummary.visitorCount}"/>
				</c:if>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>