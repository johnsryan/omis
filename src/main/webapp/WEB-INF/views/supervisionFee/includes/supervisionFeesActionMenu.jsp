<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (December 8, 2014)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.supervisionfee.msgs.supervisionFee">
	<ul>
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/supervisionFee/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createSupervisionFeeTitle"/>
				</span>
			</a>
		</li>
		<sec:authorize access="hasRole('SUPERVISION_FEE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/supervisionFee/supervisionFeeListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="supervisionFeeListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>