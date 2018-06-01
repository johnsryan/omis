<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 05, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.substance.msgs.substance">
	<ul>
		<c:if test="${not empty substanceTestRequest}">
			<sec:authorize access="hasRole('SUBSTANCE_TEST_SAMPLE_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/substanceTest/sample/create.html?offender=${offender.id}&&random=true&&request=${substanceTestRequest.id}"><span class="visibleLinkLabel"><fmt:message key="resolveRequestLabel"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${not empty substanceTestSample}">
			<sec:authorize access="hasRole('SUBSTANCE_TEST_SAMPLE_EDIT') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/substanceTest/sample/edit.html?substanceTestSample=${substanceTestSample.id}&&request=${substanceTestRequest.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditSampleLabel"/></span></a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('SUBSTANCE_TEST_SAMPLE_REMOVE') or hasRole('ADMIN')">
				<li>
					<a id="substanceTestSample${substanceTestSample.id}DeleteLink" class="removeLink" href="${pageContext.request.contextPath}/substanceTest/sample/remove.html?substanceTestSample=${substanceTestSample.id}"><span class="visibleLinkLabel"><fmt:message key="removeSampleLabel"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:choose>
			<c:when test="${empty substanceTest and not empty substanceTestSample}">
				<sec:authorize access="hasRole('SUBSTANCE_TEST_CREATE') or hasRole('ADMIN')">
					<li>
						<a class="createLink" href="${pageContext.request.contextPath}/substanceTest/create.html?substanceTestSample=${substanceTestSample.id}"><span class="visibleLinkLabel"><fmt:message key="createSubstanceTestLabel"/></span></a>
					</li>
				</sec:authorize>
			</c:when>
			<c:when test="${not empty substanceTest}">
				<sec:authorize access="hasRole('SUBSTANCE_TEST_EDIT') or hasRole('ADMIN')">
					<li>
						<a class="viewEditLink" href="${pageContext.request.contextPath}/substanceTest/edit.html?substanceTest=${substanceTest.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditSubstanceTestLabel"/></span></a>
					</li>
				</sec:authorize>
				<sec:authorize access="hasRole('SUBSTANCE_TEST_REMOVE') or hasRole('ADMIN')">
					<li>
						<a id="substanceTest${substanceSummary.sampleId}SubstanceTestDeleteLink" class="removeLink" href="${pageContext.request.contextPath}/substanceTest/remove.html?substanceTest=${substanceTest.id}"><span class="visibleLinkLabel"><fmt:message key="removeSubstanceTestLabel"/></span></a>
					</li>
				</sec:authorize>
			</c:when>
		</c:choose>
		<sec:authorize access="hasRole('SUBSTANCE_TEST_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty substanceTestSample}">
			<li>
				<a href="${pageContext.request.contextPath}/substanceTest/sample/substanceTestDetailsReport.html?substanceTestSample=${substanceTestSample.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="substanceTestDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>