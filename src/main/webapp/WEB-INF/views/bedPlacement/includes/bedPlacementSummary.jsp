<?xml version="1.0" encoding="UTF-8"?>
<!--  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.bedplacement.msgs.bedPlacement">
	<sec:authorize access="hasRole('ADMIN') or hasRole('BED_PLACEMENT_VIEW')">
		<span class="summarySection">
			<c:if test="${not empty bedPlacementSummary.facilityName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementFacilityNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementFacilityNameValueLabel">
							<fmt:param value="${bedPlacementSummary.facilityName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.complexName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementComplexNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementComplexNameValueLabel">
							<fmt:param value="${bedPlacementSummary.complexName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.unitName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementUnitNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementUnitNameValueLabel">
							<fmt:param value="${bedPlacementSummary.unitName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.levelName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementLevelNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementLevelNameValueLabel">
							<fmt:param value="${bedPlacementSummary.levelName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.sectionName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementSectionNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementSectionNameValueLabel">
							<fmt:param value="${bedPlacementSummary.sectionName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.roomName}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementRoomNameLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementRoomNameValueLabel">
							<fmt:param value="${bedPlacementSummary.roomName}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
			<c:if test="${not empty bedPlacementSummary.bedNumber}">
				<span class="summarySubSection">
					<label class="subSectionLabel"><fmt:message key="bedPlacementBedNumberLabel"/></label>
					<label class="subSectionValueLabel">
						<fmt:message key="bedPlacementBedNumberValueLabel">
							<fmt:param value="${bedPlacementSummary.bedNumber}"/>
						</fmt:message>
					</label>
				</span>
			</c:if>
		</span>
	</sec:authorize>
</fmt:bundle>