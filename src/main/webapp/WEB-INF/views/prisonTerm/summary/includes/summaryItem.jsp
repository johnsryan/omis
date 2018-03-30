<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.prisonterm.msgs.summary">
<c:if test="${not empty prisonTermSummary}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="paroleEligibilityDateLabel"/></span>
		<a href="${pageContext.request.contextPath}/prisonTerm/list.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><fmt:formatDate value="${prisonTermSummary.paroleEligibilityDate}" pattern="MM/dd/yyyy"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="prisonDischargeDateLabel"/></span>
		<a href="${pageContext.request.contextPath}/prisonTerm/list.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><fmt:formatDate value="${prisonTermSummary.projectedDischargeDate}" pattern="MM/dd/yyyy"/></span>
		</a>
	</div>
	<div class="headerCell">
		<c:if test="${prisonTermSummary.sentenceToFollow}">
		<span class="offenderHeaderField"><fmt:message key="sentenceToFollowLabel"/></span>
		</c:if>
	</div>
</div>
</c:if>
</fmt:bundle>