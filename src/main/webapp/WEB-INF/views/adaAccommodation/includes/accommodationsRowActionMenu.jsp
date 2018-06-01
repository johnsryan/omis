<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Aug 17, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.adaaccommodation.msgs.adaAccommodation">
	<ul id="testing">
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_VIEW') or hasRole('ADMIN')">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/adaAccommodation/edit.html?accommodationSummaries=${accommodation.id}"><fmt:message key="editAccommodationLabel"/></a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_REMOVE') or hasRole('ADMIN')">
		<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/adaAccommodation/remove.html?accommodation=${accommodation.id}"><fmt:message key="accommodationRemoveLabel"/></a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_ISSUANCE_CREATE') or hasRole('ADMIN')">
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/adaAccommodation/issuance/create.html?accommodation=${accommodation.id}"><fmt:message key="accommodationIssuanceCreateLabel"/></a>
		</li>	
		</sec:authorize>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')">
		<c:if test="${issuancesExist}">		
			<li>
				<a class="viewLink viewIssuanceLink" href="${pageContext.request.contextPath}/adaAccommodation/showAssociatedIssuances.html?accommodation=${accommodation.id}" id="viewIssuances${accommodation.id}"><fmt:message key="accommodationIssuanceListLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty accommodation}">
			<li>
				<a href="${pageContext.request.contextPath}/adaAccommodation/adaAccommodationDetailsFullReport.html?accommodation=${accommodation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="adaAccommodationDetailsFullReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
				<sec:authorize access="hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADA_ACCOMMODATION_ISSUANCE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty accommodation}">
			<li>
				<a href="${pageContext.request.contextPath}/adaAccommodation/adaAccommodationDetailsRedcatedReport.html?accommodation=${accommodation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="adaAccommodationDetailsRedactedReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>