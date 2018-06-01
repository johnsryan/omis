<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offender.msgs.offenderHeader" var="offenderHeader"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.commitstatus.msgs.commitStatus">
	<ul>
		<sec:authorize access="hasRole('COMMIT_STATUS_VIEW') or hasRole('COMMIT_STATUS_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/commitStatus/edit.html?commitStatusTerm=${commitStatusTerm.id}"><span class="visibleLinkLabel"><fmt:message key="view/EditCommitStatusLink" /></span></a>
			</li>
	 	</sec:authorize> 
	 	<sec:authorize access="hasRole('COMMIT_STATUS_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/commitStatus/remove.html?commitStatusTerm=${commitStatusTerm.id}"><span class="visibleLinkLabel"><fmt:message key="removeCommitStatusLink" /></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('COMMIT_STATUS_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty commitStatusTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/commitStatus/commitStatusDetailsReport.html?commitStatusTerm=${commitStatusTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="commitStatusDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>