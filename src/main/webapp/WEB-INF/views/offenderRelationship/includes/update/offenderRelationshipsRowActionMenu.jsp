<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.offender.msgs.offenderHeader" var="offenderHeader"/>
<fmt:bundle basename="omis.offenderrelationship.msgs.offenderRelationship">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_VIEW') or hasRole('OFFENDER_RELATIONSHIP_EDIT') or hasRole('ADMIN')">
			<li>
				<c:if test="${not offenderYesNo}">
					<a class="viewEditLink"  href="${pageContext.request.contextPath}/offenderRelationship/update/edit.html?relationship=${relationship.id}" title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"> <span class="visibleLinkLabel"><fmt:message key="viewEditLink" /></span> </a>
				</c:if>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_VIEW') or hasRole('ADMIN')">
			<li>
				<c:if test="${offenderYesNo}">
					<a id="offenderModuleLink" class="moduleLink" href="${pageContext.request.contextPath}/offender/profile.html?offender=${relationship.secondPerson.id}" title="<fmt:message key='offenderModulesLink' bundle='${offenderHeader}'/>"><span class="visibleLinkLabel"><fmt:message key="viewProfileLink" /></span></a>
				</c:if>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/offenderRelationship/update/remove.html?relationship=${relationship.id}"><span class="visibleLinkLabel"><fmt:message key="removeRelationshipLink" /></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty familyAssociation}">
				<li>
					<a href="${pageContext.request.contextPath}/offenderRelationship/update/familyAssociationDetailsReport.html?offender=${relationship.firstPerson.id}&reportFormat=PDF&familyAssociation=${familyAssociation.id}" class="newTab printLink"><fmt:message key="printFamilyAssociationDetails"/></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="(hasRole('OFFENDER_RELATIONSHIP_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
				<li>
					<a href="${pageContext.request.contextPath}/offenderRelationship/personDetailsReport.html?person=${relationship.secondPerson.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="personDetailsReportLink"/></a>
				</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_VIEW')  or hasRole('ADMIN')">
				<li>
					<a href="${pageContext.request.contextPath}/offenderRelationship/personDetailsRedactedReport.html?person=${relationship.secondPerson.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="personDetailsRedactedReportLink"/></a>
				</li>
		</sec:authorize>		
	</ul>
</fmt:bundle>