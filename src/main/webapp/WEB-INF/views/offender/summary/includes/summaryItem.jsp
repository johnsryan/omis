<%--
 - Author: Ryan Johns
 - Version: 0.1.0 (Jul 29, 2015)
 - Since: OMIS 3.0 --%>
<%@ page trimDirectiveWhitespaces="true" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/tld/calculate.tld" prefix="calc"%>
<%@ taglib uri="/WEB-INF/tld/person.tld" prefix="person" %>
<sec:authorize access="hasRole('OFFENDER_MODULES_VIEW') or hasRole('ADMIN')" var="hasOffenderModulesAccess"/>
<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')" var="hasOffenderProfileAccess"/>
<fmt:setBundle basename="omis.demographics.msgs.demographics" var="demographicsBundle"/>
<fmt:bundle basename="omis.offender.msgs.offenderHeader">
<div class="itemsContainer" id="badgeItemsContainer" >
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<c:if test="${hasOffenderModulesAccess}">
			<a href="${pageContext.request.contextPath}/offender/modules.html?offender=${offenderSummary.id}" title="<fmt:message key='offenderModulesLink'/>" class="offenderModulesLink"><span class="linkLabel"><fmt:message key='offenderModulesLink'/></span></a>
		</c:if>
		<c:choose>
			<c:when test="${hasOffenderProfileAccess}">
				<a href="${pageContext.request.contextPath}/offender/profile.html?offender=${offenderSummary.id}" title="<fmt:message key='gotoProfileLink'/>">
					<span class="offenderHeaderFieldValue offenderNameValue"><c:out value="${offenderSummary.lastName}, ${offenderSummary.firstName} "/><c:if test="${not empty offenderSummary.middleName}"><c:out value="${offenderSummary.middleName} "/></c:if><c:if test="${not empty offenderSummary.suffix}"> <c:out value="${offenderSummary.suffix} "/></c:if></span>
				</a>
			</c:when>
			<c:otherwise>
				<span class="offenderHeaderFieldValue offenderNameValue"><c:out value="${offenderSummary.lastName}, ${offenderSummary.firstName} "/><c:if test="${not empty offenderSummary.middleName}"><c:out value="${offenderSummary.middleName} "/></c:if><c:if test="${not empty offenderSummary.suffix}"> <c:out value="${offenderSummary.suffix} "/></c:if></span>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="offenderNumberLabel"/></span>
		<span class="offenderHeaderFieldValue">#<c:out value="${offenderSummary.offenderNumber}"/></span>
	</div>
<c:if test="${not empty offenderSummary.sex or not empty offenderSummary.birthDate}">
	<c:if test="${not empty offenderSummary.sex}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="offenderSexLabel"/></span>
		<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><fmt:message key="sex${offenderSummary.sex.name}Label" bundle="${demographicsBundle}"/></span>
		</a>
	</div>
	</c:if>
	<c:if test="${not empty offenderSummary.birthDate}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="offenderDateOfBirthLabel"/></span>
		<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><fmt:formatDate value="${offenderSummary.birthDate}" pattern="MM/dd/yyyy"/></span>
		</a>
	</div>
	<div class="headerCell">
		<jsp:useBean id="now" class="java.util.Date"/>
		<span class="offenderHeaderFieldLabel"><fmt:message key="offenderAgeLabel"/></span>
		<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><calc:elapsedYears end="${now}" start="${offenderSummary.birthDate}"/></span>
		</a>
	</div>
	</c:if>	
	<c:if test="${not empty offenderSummary.socialSecurityNumber}">
		<div class="headerCell">
			<span class="offenderHeaderFieldLabel"><fmt:message key="offenderSocialSecurityNumberLabel"/></span>
			<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
				<span class="offenderHeaderFieldValue">
					<person:formatSsn value="${offenderSummary.socialSecurityNumber}" masked="true"/>
				</span>
			</a>
		</div>
	</c:if>
</c:if>
</div>
</div>
</fmt:bundle>