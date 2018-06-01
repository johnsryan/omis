<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offender.msgs.offenderHeader" var="offenderHeader"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.family.msgs.family">
	<ul>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('FAMILY_ASSOCIATION_EDIT') or hasRole('ADMIN')">
			<li>
				<c:if test="${familyMemberOffender}">
					<a id="offenderModuleLink" class="moduleLink" href="${pageContext.request.contextPath}/offender/profile.html?offender=${familyAssociation.relationship.secondPerson.id}" title="<fmt:message key='offenderModulesLink' bundle='${offenderHeader}'/>"><span class="visibleLinkLabel"><fmt:message key="viewProfileLink" /></span></a>
				</c:if>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/family/edit.html?familyAssociation=${familyAssociation.id}&&offender=${offender.id}" 
				title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" /></span></a>
			</li>
	 	</sec:authorize> 
	 	<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<li>
				<c:if test="${not familyMemberOffender}">
					<a class="viewEditLink" href="${pageContext.request.contextPath}/offenderRelationship/update/edit.html?relationship=${familyAssociation.relationship.id}" 
					title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="editRelationLink"/></span></a>
				</c:if>
			</li>
	 	</sec:authorize>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/family/remove.html?familyAssociation=${familyAssociation.id}&&offender=${offender.id}" 
				title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="removeLink" /></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty familyAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/family/familyAssociationDetailsReport.html?familyAssociation=${familyAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="familyAssociationDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>	
		<sec:authorize access="hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty familyAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/family/familyAssociationDetailsReportRedacted.html?familyAssociation=${familyAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="familyAssociationDetailsReportRedactedLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>