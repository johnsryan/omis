<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
<c:if test="${offenderProfileItemsProperties.chronologicalNoteProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/chronologicalNote/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="chronologicalNoteLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderContactProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="offenderContactLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.educationProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/education/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="educationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.employmentProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/employment/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="employmentLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.financialProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/financial/edit.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="financialLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.grievanceProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/grievance/listByOffender.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="grievanceLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.residenceProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/residence/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="residenceLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.vehicleProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/vehicle/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="vehicleLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.medicalReviewProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/medicalReview/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="medicalReviewLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.mentalHealthReviewProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/mentalHealthReview/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="mentalHealthReviewLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>	
	<c:if test="${offenderProfileItemsProperties.travelPermitProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/travelPermit/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="travelPermitLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>	
</div>
</fmt:bundle>