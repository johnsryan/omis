<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id="offenderHeaderDetails">
<fmt:bundle basename="omis.offender.msgs.offenderHeader">
<div id="offenderHeaderBadge" class="offenderHeaderContainer">
	<div class="item">
		<c:forEach var="summaryItem" items="${basicInformationSummaryItemRegistry.items}" varStatus="i">
				<c:if test="${summaryItem.enabled}">
					<jsp:include page="${summaryItem.includedPageName}"/>
				</c:if>
		</c:forEach>
	</div>
</div>
<div id="scrollWrapper" class="offenderHeaderContainer">
	<div id="leftArrow" class="arrow"></div>
	<div id="rightArrow" class="arrow"></div>
	<div id="scroll" class="noSelect">
<%-- 		<div title="<fmt:message key='placementSummaryItemRegistryLabel'/>" id="placementSummaryItemRegistry" class="scrollDot"></div> --%>
		<div title="<fmt:message key='legacyPlacementSummaryItemRegistryLabel'/>" id="legacyPlacementSummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='dischargeDataSummaryItemRegistryLabel'/>" id="dischargeDataSummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='offenderFlagSummaryItemRegistryLabel'/>" id="offenderFlagSummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='facilitySummaryItemRegistryLabel'/>" id="facilitySummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='communitySupervisionSummaryItemRegistryLabel'/>" id="communitySupervisionSummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='contactSummaryItemRegistryLabel'/>" id="contactSummaryItemRegistry" class="scrollDot"></div>
		<div title="<fmt:message key='identificationSummaryItemRegistryLabel'/>" id="identificationSummaryItemRegistry" class="scrollDot"></div>
	</div>
	<div id="itemWrapper">
<%-- 		<c:set var="summaryItemRegistry" value="${placementSummaryItemRegistry}" scope="request" /> --%>
<%-- 		<c:set var="summaryItemRegistryTitle" value="placementSummaryItemRegistry" scope="request" /> --%>
<%-- 			<jsp:include page="summaryItemRegistry.jsp"/> --%>
		<c:set var="summaryItemRegistry" value="${legacyPlacementSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="legacyPlacementSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${dischargeDataSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="dischargeDataSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${offenderFlagSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="offenderFlagSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${facilitySummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="facilitySummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${communitySupervisionSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="communitySupervisionSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${contactSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="contactSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<c:set var="summaryItemRegistry" value="${identificationNumberSummaryItemRegistry}" scope="request" />
		<c:set var="summaryItemRegistryTitle" value="identificationSummaryItemRegistry" scope="request" />
			<jsp:include page="summaryItemRegistry.jsp"/>
		<div id="space"></div>
	</div>
</div>
</fmt:bundle>
</div>