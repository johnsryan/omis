<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
	<c:if test="${offenderProfileItemsProperties.hearingProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/hearing/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="hearingLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.substanceTestProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/substanceTest/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="substanceTestLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.supervisionFeeProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/supervisionFee/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="supervisionFeeLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.violationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/hearing/violations/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="violationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.violationEventProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/violationEvent/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="violationEventLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.warrantProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/warrant/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="warrantLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
</div>
</fmt:bundle>