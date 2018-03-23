<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.boardhearing.msgs.boardHearing">
	<ul>
		<sec:authorize access="hasRole('PAROLE_ELIGIBILITY_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/paroleEligibility/edit.html?eligibility=${paroleEligibility.id}"><span class="visibleLinkLabel"><fmt:message key="viewParoleEligibilityLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_ANALYSIS_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearingAnalysis/edit.html?eligibility=${paroleEligibility.id}"><span class="visibleLinkLabel"><fmt:message key="viewHearingAnalysisLink"/></span></a>
			</li>
		</sec:authorize>
		<c:if test="${not empty boardHearing}">
		<sec:authorize access="hasRole('HEARING_PARTICIPANT_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearingParticipant/list.html?boardHearing=${boardHearing.id}"><span class="visibleLinkLabel"><fmt:message key="listHearingParticipantLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('BOARD_HEARING_DOCUMENT_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/boardHearing/document/list.html?boardHearing=${boardHearing.id}"><span class="visibleLinkLabel"><fmt:message key="listBoardhearingDocumentLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('BOARD_HEARING_DECISION_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/boardHearingDecision/edit.html?boardHearing=${boardHearing.id}"><span class="visibleLinkLabel"><fmt:message key="viewBoardHearingDecision"/></span></a>
			</li>
		</sec:authorize>
		</c:if>
	</ul>
</fmt:bundle>