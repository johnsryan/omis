<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (June 12, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.family.msgs.family">
	<ul>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/offenderRelationship/search.html?offender=${offender.id}&amp;redirectTarget=${redirectTarget.name}&amp;option=FAMILY_MEMBER ">
					<span class="visibleLinkLabel">
						<fmt:message key="createFamilyAssociation"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/family/familyAssociationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="familyAssociationListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/family/familyAssociationListingLegacyReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="familyAssociationListingLegacyReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>