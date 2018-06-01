<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (Mar 23, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.residence.msgs.residence">
	<ul>
		<sec:authorize access="hasRole('RESIDENCE_TERM_CREATE') or hasRole('NON_RESIDENCE_TERM_CREATE') or hasRole('ADMIN')">
		<c:forEach items="${residenceStatusOptions}" var="statusOption" varStatus="status"> 
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/residence/create.html?offender=${offender.id}&amp;residenceStatusOption=${statusOption}">
					<span class="visibleLinkLabel">
						<fmt:message key="residenceCreateLabel">
							<fmt:param>
								<fmt:message key="residenceStatusOptionLabel.${statusOption}"/>
							</fmt:param>
						</fmt:message>
					</span>
				</a>
			</li>
		</c:forEach>		
		</sec:authorize>
		<sec:authorize access="hasRole('RESIDENCE_TERM_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/residence/residenceListingLegacyReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="residenceListingLegacyReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('RESIDENCE_TERM_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/residence/residenceListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="residenceListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>