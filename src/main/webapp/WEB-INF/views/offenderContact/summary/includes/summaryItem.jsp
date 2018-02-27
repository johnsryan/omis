<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offendercontact.msgs.summary">
<c:if test="${not empty offenderContactSummary}">
<div class="offenderHeaderDetailsSection">
	<c:if test="${offenderContactSummary.address}">
		<div class="headerCell">
			<span class="offenderHeaderFieldLabel"><fmt:message key="mailingAddressLabel"/></span>
			<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
				<span class="offenderHeaderFieldValue">
					<c:out value=" ${offenderContactSummary.addressValue} ${offenderContactSummary.addressCityName}, ${offenderContactSummary.addressStateAbbreviation} ${offenderContactSummary.addressZipCodeValue}" />
				</span>
			</a>
		</div>
	</c:if>
	
	<c:if test="${offenderContactSummary.poBox}">
		<div class="headerCell">
			<span class="offenderHeaderFieldLabel"><fmt:message key="poBoxLabel"/></span>
			<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
				<span class="offenderHeaderFieldValue"><c:out value=" ${offenderContactSummary.poBoxValue} ${offenderContactSummary.poBoxCityName}, ${offenderContactSummary.poBoxStateAbbreviation} ${offenderContactSummary.poBoxZipCodeValue}" /></span>
			</a>
		</div>
	</c:if>
	
	<c:if test="${offenderContactSummary.telephoneNumber}">
		<div class="headerCell">
			<span class="offenderHeaderFieldLabel"><fmt:message key="telephoneNumberLabel"/></span>
			<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
				<span class="offenderHeaderFieldValue"><c:out value="${offenderContactSummary.telephoneNumberValue}" /></span>
			</a>
		</div>
	</c:if>
	<c:if test="${offenderContactSummary.onlineAccount}">
		<div class="headerCell">
			<span class="offenderHeaderFieldLabel"><fmt:message key="onlineAccountLabel"/></span>
			<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
				<span class="offenderHeaderFieldValue"><c:out value="${offenderContactSummary.onlineAccountName}" /></span>
			</a>
		</div>
	</c:if>
</div>
</c:if>
</fmt:bundle>