<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offender.msgs.offenderHeader" var="offenderHeader"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.commitstatus.msgs.commitStatus">
	<ul>
		<sec:authorize access="hasRole('COMMIT_STATUS_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/commitStatus/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createCommitStatusLink" /></span></a>
			</li>
	 	</sec:authorize> 
	 	<sec:authorize access="hasRole('COMMIT_STATUS_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/commitStatus/commitStatusListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="commitStatusListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>