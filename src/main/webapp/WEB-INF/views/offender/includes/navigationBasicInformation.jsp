<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.navigation">
<div class="navItems">
	<c:if test="${offenderProfileItemsProperties.offenderAlternativeIdentityProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offender/identity/alternative/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="alternativeIdentityLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderAlternativeNameProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offender/name/alternative/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="alternativeNameLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderDemographicsProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offender/demographics/edit.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="demographicsLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.dnaProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/dna/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="dnaLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderFlagProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offenderFlag/edit.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="offenderFlagLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.identificationNumberProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/identificationNumber/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="identificationNumbersLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderDetailsProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="personLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.militaryProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/military/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="militaryLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.offenderPhotoProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/offenderPhoto/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="mugshotsLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.religionProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/religion/religiousPreference/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="religiousPreferenceLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
	<c:if test="${offenderProfileItemsProperties.physicalFeatureProfileItemEnabled}">
		<div class="navItemContainer hoverable">
			<div class="navItem">
				<a href="${pageContext.request.contextPath}/physicalFeature/list.html?offender=${offenderSummary.id}">
					<span>
						<fmt:message key="scarsMarksLabel"/>
					</span>
				</a>
			</div>
		</div>
	</c:if>
</div>
</fmt:bundle>