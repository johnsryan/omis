<!-- 
 - Action menu for offender contact.
 -
 - Author: Josh Divine
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offendercontact.msgs.offenderContact">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_CONTACT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offenderContact/offenderContactDetailsReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="offenderContactDetailsReportLink"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>