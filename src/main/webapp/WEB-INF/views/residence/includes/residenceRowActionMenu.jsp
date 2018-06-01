<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.residence.msgs.residence">
	<ul>
		<sec:authorize access="hasRole('RESIDENCE_TERM_VIEW') or hasRole('ADMIN')">
			<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/residence/editResidenceTerm.html?residenceTerm=${residenceTerm.id}" title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('RESIDENCE_REMOVE') or hasRole('NON_RESIDENCE_TERM_REMOVE') or hasRole('ADMIN')">
			<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/residence/remove.html?residenceTerm=${residenceTerm.id}" title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('RESIDENCE_TERM_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty residenceTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/residence/residenceDetailsReport.html?residenceTerm=${residenceTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="residenceDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>