<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (Aug  10, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.physicalfeature.msgs.physicalfeature">
	<ul>
		<sec:authorize access="hasRole('PHYSICAL_FEATURE_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/physicalFeature/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="newOffenderPhysicalFeatureLabel"/></span></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('PHYSICAL_FEATURE_PHOTO_ASSOCIATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${otherPhotosCount > 0}">
				<li><a class="linkLink" href="${pageContext.request.contextPath}/otherPhoto/edit.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="orphanOtherPhotosLabel"/></span></a></li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PHYSICAL_FEATURE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/physicalFeature/physicalFeatureListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="physicalFeatureListingReportLinkLabel"/></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/physicalFeature/physicalFeatureListingTextReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="physicalFeatureListingTextReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>