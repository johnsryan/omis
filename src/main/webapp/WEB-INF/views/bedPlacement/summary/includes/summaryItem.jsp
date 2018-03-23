<%-- Author: Ryan Johns
   - Version: 0.1.0 (Jul 28, 2015)
   - Since: OMIS 3.0 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.bedplacement.msgs.summary">
<c:if test="${not empty bedPlacementSummary.facilityName}">
<div class="offenderHeaderDetailsSection">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="facilityNameLabel"/></span>
		<a href="${pageContext.request.contextPath}/bedPlacement/list.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue"><c:out value="${bedPlacementSummary.facilityName}"/></span>
		</a>
	</div>
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="bedPlacementLabel"/></span>
		<a href="${pageContext.request.contextPath}/bedPlacement/list.html?offender=${offenderSummary.id}">
			<span class="offenderHeaderFieldValue">
				<c:if test="${not empty bedPlacementSummary.complexName}"><c:out value="${bedPlacementSummary.complexName}"/> - </c:if><c:if test="${not empty bedPlacementSummary.unitName}"><c:out value="${bedPlacementSummary.unitName}"/> - </c:if><c:if test="${not empty bedPlacementSummary.levelName}"><c:out value="${bedPlacementSummary.levelName}"/> - </c:if><c:if test="${not empty bedPlacementSummary.sectionName}"><c:out value="${bedPlacementSummary.sectionName}"/> - </c:if><c:if test="${not empty bedPlacementSummary.roomName}"><c:out value="${bedPlacementSummary.roomName}"/> - </c:if><c:if test="${not empty bedPlacementSummary.bedNumber}"><c:out value="${bedPlacementSummary.bedNumber}"/></c:if>
			</span>
		</a>
	</div>
	<%-- <c:if test="${not empty bedPlacementSummary.unitName}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="unitNameLabel"/></span>
		<span class="offenderHeaderFieldValue"></span>
	</div>
	</c:if>
	<c:if test="${not empty bedPlacementSummary.sectionName}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="sectionNameLabel"/></span>
		<span class="offenderHeaderFieldValue"></span>
	</div>
	</c:if>
	<c:if test="${not empty bedPlacementSummary.levelName}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="levelNameLabel"/></span>
		<span class="offenderHeaderFieldValue"><c:out value="${bedPlacementSummary.levelName}"/></span>
	</div>
	</c:if>
	<c:if test="${not empty bedPlacementSummary.roomName}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="roomNameLabel"/></span>
		<span class="offenderHeaderFieldValue"><c:out value="${bedPlacementSummary.roomName}"/></span>
	</div>
	</c:if>
	<c:if test="${not empty bedPlacementSummary.bedNumber}">
	<div class="headerCell">
		<span class="offenderHeaderFieldLabel"><fmt:message key="bedNumberLabel"/></span>
		<span class="offenderHeaderFieldValue"></span>
	</div> 
	</c:if>--%>
</div>
</c:if>
</fmt:bundle>