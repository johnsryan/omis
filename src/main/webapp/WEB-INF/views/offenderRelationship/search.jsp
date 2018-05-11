<?xml version="1.0" encoding="UTF-8"?>
<%--
  - Offender relation search screen.
  -
  - Used to query people that can be related to an offender.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setBundle basename="omis.offenderrelationship.msgs.offenderRelationship" var="offenderRelationshipBundle"/>
<fmt:setBundle var="criminalAssociationBundle" basename="omis.criminalassociation.msgs.criminalAssociation"/>
<fmt:setBundle var="familyBundle" basename="omis.family.msgs.family"/>
<fmt:setBundle var="victimBundle" basename="omis.victim.msgs.victim"/>
<fmt:setBundle var="visitationBundle" basename="omis.visitation.msgs.visitation"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><fmt:message key="searchOffenderRelationshipsTitle" bundle="${offenderRelationshipBundle}"/></title>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offenderRelationship/style/offenderRelationship.css?VERSION=1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/offenderRelationship/scripts/searchOffenderRelationships.js"> </script>
</head>
<body>
  <jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
  <h1><fmt:message key="searchOffenderRelationshipsTitle" bundle="${offenderRelationshipBundle}"/></h1>
  <jsp:include page="includes/searchForm.jsp"/>
  <c:if test="${not empty offenderRelationshipSummaries}">
    <jsp:include page="includes/searchResults.jsp"/>
  </c:if>
  
  <div class="createRelationshipLinkContainer foregroundUltraLight">
  <c:forEach var="option" items="${options}">
	  <c:choose>
			<c:when test="${option.name eq 'CRIMINAL'}">
				<a class="createLink" href="${pageContext.request.contextPath}/criminalAssociation/create.html?offender=${offenderSummary.id}" title="<fmt:message key='createCriminalAssociationWithNewAssociateLink' bundle='${criminalAssociationBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='createCriminalAssociationWithNewAssociateLink' bundle='${criminalAssociationBundle}'/></span></a>
			</c:when>
			<c:when test="${option.name eq 'FAMILY_MEMBER'}">
				<a class="createLink" href="${pageContext.request.contextPath}/family/create.html?offender=${offenderSummary.id}" ><span class="visibleLinkLabel"><fmt:message key='createFamilyAssociationWithNewFamilyMemberLink' bundle='${familyBundle}'/></span></a>
			</c:when>
			<c:when test="${option.name eq 'VICTIM'}">
				<a class="createLink" href="${pageContext.request.contextPath}/victim/association/create.html?offender=${offenderSummary.id}&amp;redirectTarget=OFFENDER" title="<fmt:message key='createVictimAssociationWithNewVictimLink' bundle='${victimBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='createVictimAssociationWithNewVictimLink' bundle='${victimBundle}'/></span></a>
			</c:when>
			<c:when test="${option.name eq 'VISITOR'}">
				<a class="createLink" href="${pageContext.request.contextPath}/visitation/create.html?offender=${offenderSummary.id}" title="<fmt:message key='createVisitationAssociationWithNewVisitorLink' bundle='${visitationBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='createVisitationAssociationWithNewVisitorLink' bundle='${visitationBundle}'/></span></a>
			</c:when>
			<c:when test="${option.name eq 'NEW_RELATION'}">
				<a class="createLink" href="${pageContext.request.contextPath}/offenderRelationship/create.html?offender=${offenderSummary.id}" title="<fmt:message key='createOffenderRelationshipWithNewRelationLink' bundle='${offenderRelationshipBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='createOffenderRelationshipWithNewRelationLink' bundle='${offenderRelationshipBundle}'/></span></a>
			</c:when>
			<c:otherwise>
				Error - unknown option: <c:out value="${option}"/>
			</c:otherwise>
	  </c:choose>
  </c:forEach>
  </div>
</body>
</html>