<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
	<c:if test="${offenderProfileItemsProperties.adaProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/adaAccommodation/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="adaAccommodationLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderAlertProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/alert/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="alertLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.cautionProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/caution/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="cautionLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.stgProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/stg/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="stgLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.separationNeedProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/separationNeed/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="separationNeedLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.specialNeedProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/specialNeed/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="specialNeedLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.workRestrictionProfileItemEnabled}">
		<div class="foregroundUltraLight navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/workRestriction/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="workRestrictionLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
</div>
</fmt:bundle>