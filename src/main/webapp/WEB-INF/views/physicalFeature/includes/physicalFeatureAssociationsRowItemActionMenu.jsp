<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (Aug  10, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.physicalfeature.msgs.physicalfeature">
	<ul>
		<sec:authorize access="hasRole('PHYSICAL_FEATURE_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/physicalFeature/edit.html?physicalFeatureAssociation=${physicalFeatureAssociation.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLabel"/></span></a>
			</li>
			<li>
				<a class="removeLink" id="${offenderPhysicalFeature.id}RemoveLink" href="${pageContext.request.contextPath}/physicalFeature/remove.html?physicalFeatureAssociation=${physicalFeatureAssociation.id}"><span class="visibleLinkLabel"><fmt:message key="removeLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('PHYSICAL_FEATURE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty physicalFeatureAssociation}">
			<li>
				<a href="${pageContext.request.contextPath}/physicalFeature/physicalFeatureDetailsReport.html?physicalFeatureAssociation=${physicalFeatureAssociation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="physicalFeatureDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>