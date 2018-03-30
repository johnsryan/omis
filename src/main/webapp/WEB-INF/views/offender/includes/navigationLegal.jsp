<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
	<c:if test="${offenderProfileItemsProperties.chargeProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/courtCase/listCharges.html?defendant=${offenderSummary.id}">
					<span>
						<fmt:message key="chargeLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.commitStatusTermProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/commitStatus/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="commitStatusLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.courtCaseProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/courtCase/list.html?defendant=${offenderSummary.id}">
					<span>
						<fmt:message key="courtCaseLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.courtCaseDocumentAssociationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/courtCase/document/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="courtCaseDocumentLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.currentOffenseTermProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offenseTerm/listCurrentOffenses.html?person=${offenderSummary.id}">
					<span>
						<fmt:message key="currentOffenseLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.detainerNotificationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/detainerNotification/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="detainerNotificationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.misdemeanorCitationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/citation/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="misdemeanorCitationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenseTermProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offenseTerm/list.html?person=${offenderSummary.id}">
					<span>
						<fmt:message key="offenseTermLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.presentenceInvestigationRequestProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="presentenceInvestigationRequestLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.prisonTermProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/prisonTerm/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="prisonTermLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.courtCaseConditionProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/courtCaseCondition/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="courtCaseConditionLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.tierDesignationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/tierDesignation/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="tierDesignationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.trackedDocumentProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/trackedDocumentReport/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="trackedDocumentLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
</div>
</fmt:bundle>