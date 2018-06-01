<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Sept 13, 2017)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offenderflag.msgs.offenderFlag">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_FLAG_VIEW') or hasRole('ADMIN')">
		<li>
			<a class="offenderProfileLink" href="${pageContext.request.contextPath}/offender/profile.html?offender=${offender.id}">
			<fmt:message key="returnToOffenderProfileLabel" /></a>
		</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_FLAG_VIEW') or hasRole('ADMIN')">
	        <c:if test="${not empty offender}">
	            <li>
	                <a href="${pageContext.request.contextPath}/offenderFlag/offenderFlagsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="offenderFlagsListingReportLinkLabel"/></a>
	            </li>
	        </c:if>
	    </sec:authorize>
	</ul>
</fmt:bundle>