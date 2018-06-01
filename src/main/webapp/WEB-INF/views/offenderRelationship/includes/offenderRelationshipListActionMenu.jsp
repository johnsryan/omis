<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.offenderrelationship.msgs.offenderRelationship">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/offenderRelationship/search.html?offender=${offender.id}&amp;redirectTarget=${redirectTarget.name}&amp;option=NEW_RELATION"><span class="visibleLinkLabel"><fmt:message key="createRelationLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_RELATIONSHIP_LIST') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenderRelationship/relationshipListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="printListReportLinkLabel"/></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>