<!-- 
 - Author: Joel Norris
 - Author: Yidong Li
 - Version: 0.1.1 (Oct 28, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.dna.msgs.dna">
	<ul>
		<sec:authorize access="hasRole('DNA_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/dna/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="newDnaSampleLabel"/></span></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('DNA_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty dnaSample}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/dna/edit.html?dnaSample=${dnaSample.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditDNALink"/></span></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('DNA_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty dnaSample}">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/dna/remove.html?dnaSample=${dnaSample.id}"><span class="visibleLinkLabel"><fmt:message key="removeDNALink"/></span></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="(hasRole('DNA_VIEW') and hasRole('OFFENDER_SSN_VIEW')) or hasRole('ADMIN')">
			<c:if test="${not empty dnaSample}">
				<li>
					<omis:reportPro reportPath="/BasicInformation/DNA/Documentation_of_DNA_Sample_Collection&DNA_SAMPLE_ID=${dnaSample.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="dnaSampleCollectedReportLinkLabel"/></omis:reportPro>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('DNA_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty dnaSample}">
				<li>
					<omis:reportPro reportPath="/BasicInformation/DNA/Documentation_of_DNA_Sample_Collection_Redacted&DNA_SAMPLE_ID=${dnaSample.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="dnaSampleCollectedRedactedReportLinkLabel"/></omis:reportPro>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('DNA_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty dnaSample}">
			<li>
				<a href="${pageContext.request.contextPath}/dna/dnaDetailsReport.html?dnaSample=${dnaSample.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="dnaDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
		<sec:authorize access="hasRole('DNA_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/dna/biologicalSampleReport.html?offender=${offender.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="biologicalSampleReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('DNA_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/dna/dnaListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="dnaListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>