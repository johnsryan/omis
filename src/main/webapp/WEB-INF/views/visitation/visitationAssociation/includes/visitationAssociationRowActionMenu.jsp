<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.visitation.msgs.visitation">
	<ul>
		<li><a class="viewEditLink" href="${pageContext.request.contextPath}/visitation/edit.html?visitationAssociation=${visitationAssociation.id}"><fmt:message key="viewEditLabel"/></a></li>
		<c:if test="${visitationAssociation.flags.specialVisit or visitationAssociation.approval.approved}">
			<c:choose>
				<c:when test="${currentlyVisiting}">
					<li><a class="checkOutLink" href="${pageContext.request.contextPath}/visitation/visit/checkOut.html?visit=${visit.id}"><fmt:message key="checkOutLabel"/></a></li>
				</c:when>
				<c:otherwise>
					<li><a class="checkInLink" href="#"><fmt:message key="checkInLabel"/></a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:choose>
			<c:when test="${isOffender}">
				<li><a id="offenderProfileLink" class="offenderProfileLink" href="/OMIS3/offender/profile.html?offender=${offender.id}" title="Goto Offender Profile"><fmt:message key="offenderProfileLinkLabel"/></a></li>
			</c:when>
			<c:otherwise>
				<li><a class="viewEditLink" href="${pageContext.request.contextPath}/offenderRelationship/update/edit.html?relationship=${visitationAssociation.relationship.id}"><fmt:message key="viewEditRelationshipLabel"/></a></li>
			</c:otherwise>
		</c:choose>
		<li><a class="unlinkLink" id="visitorSummary${visitationAssociation.id}DissociateLink" href="${pageContext.request.contextPath}/visitation/dissociateVisitationAssociation.html?visitationAssociation=${visitationAssociation.id}"><fmt:message key="dissociateLabel"/></a></li>
		<li><a class="removeLink visitationAssociationRemoveLink" id="visitorSummary${visitationAssociation.id}RemoveLink" href="${pageContext.request.contextPath}/visitation/remove.html?visitationAssociation=${visitationAssociation.id}"><fmt:message key="removeVisitorLabel"/></a></li>
		<sec:authorize access="(hasRole('VISITATION_ASSOCIATION_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<li><a href="${pageContext.request.contextPath}/visitation/visitationDetailsReport.html?visitationAssociation=${visitationAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="visitationAssociationDetailReportLinkLabel"/></a></li>
		</sec:authorize>
	    <sec:authorize access="hasRole('VISITATION_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<li><a href="${pageContext.request.contextPath}/visitation/visitationDetailsRedactedReport.html?visitationAssociation=${visitationAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="visitationAssociationDetailRedactedReportLinkLabel"/></a></li>
		</sec:authorize>
	</ul>
</fmt:bundle>