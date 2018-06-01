<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Nov 06, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.custody.msgs.custodyReview">
	<ul>
		<sec:authorize access="hasRole('CUSTODY_REVIEW_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/custody/create.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="newCustodyReviewLabel"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('CUSTODY_REVIEW_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/custody/custodyReviewListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="custodyReviewListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>