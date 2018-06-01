<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Dec 28, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.criminalassociation.msgs.criminalAssociation">
<ul>
	<sec:authorize access="hasRole('CRIMINAL_ASSOCIATION_VIEW') or hasRole('CRIMINAL_ASSOCIATION_EDIT') or hasRole('ADMIN')">
		<li>
		<a class="viewEditLink" href="${pageContext.request.contextPath}/criminalAssociation/edit.html?association=${criminalAssociation.id}&&sourceOffender=${criminalAssociation.relationship.firstPerson.id}"><fmt:message key="viewEditLabel"/></a>
		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('CRIMINAL_ASSOCIATION_REMOVE') or hasRole('ADMIN')">
		<li>
		<a class="removeLink" href="${pageContext.request.contextPath}/criminalAssociation/remove.html?association=${criminalAssociation.id}&&offender=${criminalAssociation.relationship.firstPerson.id}"><fmt:message key="removeLabel"/></a>
		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('CRIMINAL_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty criminalAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/criminalAssociation/criminalAssociationDetailsReport.html?criminalAssociation=${criminalAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="criminalAssociationDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
</ul>
</fmt:bundle>