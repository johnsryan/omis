<!-- 
 - Author: Sheronda Vaughn
 - Author: Joel Norris
 - Version: 0.1.0 (Dec 12, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.custody.msgs.custodyReview">
	<ul>
		<sec:authorize access="hasRole('CUSTODY_REVIEW_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/custody/edit.html?custodyReview=${custodyReview.id}"><fmt:message key="editCustodyReviewLabel"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('CUSTODY_REVIEW_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/custody/remove.html?custodyReview=${custodyReview.id}"><fmt:message key="removeCustodyReviewLabel"/></a>
			</li>
		</sec:authorize>
		<c:choose>
			<c:when test="${not empty custodyOverride}">
				<c:if test="${empty custodyOverride.authorizationSignature}">
					<sec:authorize access="hasRole('CUSTODY_OVERRIDE_AUTHORIZE') or hasRole('ADMIN')">
						<li>
							<a class="authorizeLink" href="${pageContext.request.contextPath}/custody/override/authorize.html?custodyOverride=${custodyOverride.id}"><fmt:message key="authorizeOverrideLabel"/></a>
						</li>
					</sec:authorize>
				</c:if>
			</c:when>
			<c:otherwise>
				<sec:authorize access="hasRole('CUSTODY_OVERRIDE_CREATE') or hasRole('ADMIN')">
					<li>
						<a class="createLink" href="${pageContext.request.contextPath}/custody/override/create.html?custodyReview=${custodyReview.id}"><fmt:message key="newCustodyOverrideLabel"/></a>
					</li>
				</sec:authorize>
			</c:otherwise>
		</c:choose>
		<c:if test="${not empty custodyOverride}">
			<sec:authorize access="hasRole('CUSTODY_OVERRIDE_REMOVE') or hasRole('ADMIN')">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/custody/override/remove.html?custodyOverride=${custodyOverride.id}"><fmt:message key="removeLabel"/></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty custodyReview}">
			<sec:authorize access="hasRole('CUSTODY_REVIEW_VIEW') or hasRole('ADMIN')">
				<li>
					<a href="${pageContext.request.contextPath}/custody/custodyReviewDetailsReport.html?custodyReview=${custodyReview.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="custodyReviewDetailsReportLinkLabel"/></a>
				</li>
			</sec:authorize>
		</c:if>
	</ul>
</fmt:bundle>