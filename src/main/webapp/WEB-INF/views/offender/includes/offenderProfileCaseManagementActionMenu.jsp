<!-- 
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 8, 2017)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:bundle basename="omis.offender.msgs.profile">
	<ul>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<omis:reportPro reportPath="/CaseManagement/Contact_Letter&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="contactLetterLinkLabel"/></omis:reportPro>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/profileInstitutionalCasePlanReport.rtf?offender=${offender.id}&reportFormat=RTF" class="reportLink"><fmt:message key="institutionalCasePlanLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>			
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/profileOffenderContactReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="offenderContactReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/profileSignUpReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="signUpReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offender/profileTransitionalCasePlanReport.rtf?offender=${offender.id}&reportFormat=RTF" class="reportLink"><fmt:message key="transitionalCasePlanLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>	
		
		<sec:authorize access="hasRole('CHRONOLOGICAL_NOTE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			    <li>
			        <omis:reportPro reportPath="/CaseManagement/Chronological_Notes/Offender_Activity_Notes&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="offenderActivityNotesReportLinkLabel"/></omis:reportPro>
			    </li>
		    </c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>