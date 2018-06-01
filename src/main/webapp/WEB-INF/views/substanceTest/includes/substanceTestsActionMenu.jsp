<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 05, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.substance.msgs.substance">
	<ul>
		<sec:authorize access="hasRole('SUBSTANCE_TEST_SAMPLE_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/substanceTest/sample/create.html?offender=${offender.id}&&random=false"><span class="visibleLinkLabel"><fmt:message key="newSubstanceTestSampleLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('SUBSTANCE_TEST_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/substanceTest/sample/substanceTestListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="substanceTestListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>