<?xml version="1.0" encoding="UTF-8"?>
<%--
 - Offender header navigational screen.
 -
 - Author: Stephen Abson
 - Author: Ryan Johns
 - Author: Annie Jacques
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:bundle basename="omis.offender.msgs.profile">
	<input type="hidden" id="headerOffenderId" value="${offenderSummary.id}" />
	<div id="fullNavBarWrapper">
		<div id="navLeftArrow" class="arrow"></div>
		<div id="navRightArrow" class="arrow"></div>
	<div id="navBarWrapper">
		<div id="navBar" class="noSelect">
			<a href="#" class="navBarItem accentDark" id="basicInformationNav">
				<fmt:message key="basicInformationLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="placementNav">
				<fmt:message key="placementLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="legalNav">
				<fmt:message key="legalLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="caseManagementNav">
				<fmt:message key="caseManagementLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="safetyNav">
				<fmt:message key="safetyLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="complianceNav">
				<fmt:message key="complianceLabel"/>
			</a>
			<%-- <a href="#" class="navBarItem accentDark" id="healthNav">
				<fmt:message key="healthLabel"/>
			</a> --%>
			<a href="#" class="navBarItem accentDark" id="relationshipsNav">
				<fmt:message key="relationshipsLabel"/>
			</a>
			<a href="#" class="navBarItem accentDark" id="boardOfPardonsAndParoleNav">
				<fmt:message key="boardPardonsParoleLabel"/>
			</a>
		</div>
	</div>
	</div>
	<div class="navContentWrapper" id="navContentWrapper">
		<div class="navContent accentDark" id="basicInformationNavContent">
			<jsp:include page="navigationBasicInformation.jsp"/>
		</div>
		<div class="navContent accentDark" id="placementNavContent">
			<jsp:include page="navigationPlacement.jsp"/>
		</div>
		<div class="navContent accentDark" id="legalNavContent">
			<jsp:include page="navigationLegal.jsp"/>
		</div>
		<div class="navContent accentDark" id="caseManagementNavContent">
			<jsp:include page="navigationCaseManagement.jsp"/>
		</div>
		<div class="navContent accentDark" id="safetyNavContent">
			<jsp:include page="navigationSafety.jsp"/>
		</div>
		<div class="navContent accentDark" id="complianceNavContent">
			<jsp:include page="navigationCompliance.jsp"/>
		</div>
		<%-- <div class="navContent accentDark" id="healthNavContent">
			<jsp:include page="navigationHealth.jsp"/>
		</div> --%>
		<div class="navContent accentDark" id="relationshipsNavContent">
			<jsp:include page="navigationRelationships.jsp"/>
		</div>
		<div class="navContent accentDark" id="boardOfPardonsAndParoleNavContent">
			<jsp:include page="navigationBoardOfPardonsAndParole.jsp"/>
		</div>
	</div>
</fmt:bundle>