<!-- 
 - Action menu for victim associations.
 -
 - Author: Stephen Abson
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.victim.msgs.victim">
	<ul>
		<c:if test="${not empty victimAssociation}">
			<sec:authorize access="hasRole('ADMIN') or hasRole('VICTIM_PROFILE_VIEW')">
				<li>
					<a class="victimProfileLink" href="${pageContext.request.contextPath}/victim/profile.html?victim=${victimAssociation.relationship.secondPerson.id}" class="victimProfileLink" title="<fmt:message key='victimProfileLink'/>"><span class="visibleLinkLabel"><fmt:message key="victimProfileLink"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty offender}">
			<sec:authorize access="hasRole('VICTIM_ASSOCIATION_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/offenderRelationship/search.html?offender=${offender.id}&amp;redirectTarget=${redirectTarget.name}&amp;option=VICTIM"><span class="visibleLinkLabel"><fmt:message key="createVictimAssociationLink"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty victim}">
			<sec:authorize access="hasRole('VICTIM_ASSOCIATION_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/victim/offender/search.html?victim=${victim.id}&amp;redirectTarget=${redirectTarget.name}"><span class="visibleLinkLabel"><fmt:message key="createVictimAssociationOffenderLink"/></span></a>
				</li>
			</sec:authorize>
		</c:if>		 
		
	 	<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
	 		<li>
	 			<c:if test="${not offenderYesNo and not empty victimAssociation}">
	 				<a class="viewEditLink" href="${pageContext.request.contextPath}/offenderRelationship/update/edit.html?relationship=${victimAssociation.relationship.id}"><span class="visibleLinkLabel"><fmt:message key="editRelationLink"/></span></a>
	 			</c:if>
	 		</li>
	 	</sec:authorize>
	 	 	
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimImpactStatementReport.html?reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimImpactReportLinkLabel"/></a>
			</li>
		</c:if>	
		</sec:authorize>
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimImpactStatementKidReport.html?reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimImpactKidReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
				<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimListingLegacyReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimListingLegacyReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<omis:reportPro reportPath="/Relationships/Victims/Victims_Rapid_Contact_List&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="victimRapidContactListReportLinkLabel"/></omis:reportPro>
			</li>
		</c:if>
		</sec:authorize>
		<c:if test="${not empty victimAssociation}">
			<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/victim/association/edit.html?victimAssociation=${victimAssociation.id}&amp;redirectTarget=${redirectTarget.name}"><span class="visibleLinkLabel"><fmt:message key="viewEditVictimAssociationLink"/></span></a>
				</li>
			</sec:authorize>
			<c:choose>
				<c:when test="${victimAssociationHasNotes}">
					<sec:authorize access="hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_REMOVE')">
						<li>
							<a class="removeLink" href="${pageContext.request.contextPath}/victim/association/remove.html?victimAssociation=${victimAssociation.id}&amp;redirectTarget=${redirectTarget.name}&amp;removeNotes=true"><span class="visibleLinkLabel"><fmt:message key="removeVictimAssociationWithNotesLink"/></span></a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('VICTIM_ASSOCIATION_REMOVE') or hasRole('ADMIN')">
						<li>
							<a class="removeLink" href="${pageContext.request.contextPath}/victim/association/remove.html?victimAssociation=${victimAssociation.id}&amp;redirectTarget=${redirectTarget.name}&amp;removeNotes=false"><span class="visibleLinkLabel"><fmt:message key="removeVictimAssociationDisassociateNotesLink"/></span></a>
						</li>
					</sec:authorize>
				</c:when>
				<c:otherwise>
					<sec:authorize access="hasRole('VICTIM_ASSOCIATION_REMOVE') or hasRole('ADMIN')">
						<li>
							<a class="removeLink" href="${pageContext.request.contextPath}/victim/association/remove.html?victimAssociation=${victimAssociation.id}&amp;redirectTarget=${redirectTarget.name}"><span class="visibleLinkLabel"><fmt:message key="removeVictimAssociationLink"/></span></a>
						</li>
					</sec:authorize>
				</c:otherwise>
			</c:choose>
		</c:if>
		<sec:authorize access="(hasRole('VICTIM_ASSOCIATION_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty victimAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimAssocDetailsReport.html?victimAssociation=${victimAssociation.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimAssocDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty victimAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimAssocDetailsRedactedReport.html?victimAssociation=${victimAssociation.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimAssocDetailsRedactedReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="(hasRole('VICTIM_ASSOCIATION_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty victimAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimDetailsReport.html?victimAssociation=${victimAssociation.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VICTIM_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty victimAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/victim/association/victimDetailsRedactedReport.html?victimAssociation=${victimAssociation.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="victimDetailsRedactedReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>