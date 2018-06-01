<!-- 
 - Author: Sheronda Vaughn
 - Author: Joel Norris
 - Version: 0.1.1 (June 14, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.adaaccommodation.msgs.adaAccommodation">
	<ul>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_CREATE') or hasRole('ADMIN')"> 
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/adaAccommodation/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="accommodationCreateLabel"/>
				</span>
			</a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('ADA_ACCOMMODATION_ISSUANCE_LIST') or hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/adaAccommodation/adaAccommodationListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="adaAccommodationListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>