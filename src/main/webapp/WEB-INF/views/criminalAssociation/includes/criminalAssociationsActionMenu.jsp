<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (April 17, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.criminalassociation.msgs.criminalAssociation">
	<ul style = 'display: none'>
		<li>
			<a class="printLink" >
				<span class="visibleLinkLabel">
					<fmt:message key="printAssociationLabel"/>
				</span>
			</a>
		</li>
	</ul>
	<ul style = 'display: none'>
		<li>
			<a class="helpLink" >
				<span class="visibleLinkLabel">
					<fmt:message key="associationHelpLabel"/>
				</span>
			</a>
		</li>
	</ul>
	<ul>
		<sec:authorize access="hasRole('CRIMINAL_ASSOCIATION_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/criminalAssociation/create.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="newCriminalAssociationLabel"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('CRIMINAL_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/criminalAssociation/criminalAssociationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="criminalAssociationListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>