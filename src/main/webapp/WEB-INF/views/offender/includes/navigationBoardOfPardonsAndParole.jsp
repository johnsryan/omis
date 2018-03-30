<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
	<c:if test="${offenderProfileItemsProperties.boardHearingProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/boardHearing/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="boardHearingLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.paroleBoardAgreementProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/paroleBoardCondition/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="paroleBoardConditionLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.paroleEligibilityProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/paroleEligibility/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="paroleEligibilityLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
</div>
</fmt:bundle>