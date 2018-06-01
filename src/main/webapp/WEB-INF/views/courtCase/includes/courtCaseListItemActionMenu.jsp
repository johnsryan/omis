<?xml version="1.0" encoding="UTF-8"?>
<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%--
 - Author: Ryan Johns
 - Author: Josh Divine
 - Version: 0.1.1 (Jan 31, 2018)
 - Since: OMIS 3.0 
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.courtcase.msgs.courtCase">
<ul>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<li>
			<c:choose>
				<c:when test="${hasConvictions}">
					<sec:authorize access="hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')">
						<a class="viewEditLink" href="${pageContext.request.contextPath}/offenseTerm/edit.html?courtCase=${courtCase.id}">
							<fmt:message key="viewEditOffenseLink"/>
						</a>
					</sec:authorize>
				</c:when>
				<c:otherwise>
					<sec:authorize access="hasRole('LIST_COURT_CASE') or hasRole('ADMIN')">
						<a class="viewEditLink" href="${pageContext.request.contextPath}/courtCase/edit.html?courtCase=${courtCase.id}">
							<fmt:message key="viewEditCourtCaseLink"/>
						</a>
					</sec:authorize>	
				</c:otherwise>
			</c:choose>
		</li>
		<sec:authorize access="hasRole('PROBATION_TERM_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty courtCase}">
				<li>
					<a class="listLink" href="${pageContext.request.contextPath}/probationTerm/list.html?courtCase=${courtCase.id}">
						<fmt:message key="viewProbationTermsLink"/>
					</a>
				</li>
			</c:if>
		</sec:authorize>
	</c:if>
	<sec:authorize access="hasRole('COURT_CASE_REMOVE') or hasRole('ADMIN')">
		<c:if test="${not empty courtCase and not courtCase.flags.dismissed }">
			<li>
				<a class="dismissDocketLink removeLink" href="${pageContext.request.contextPath}/courtCase/dismissDocket.html?courtCase=${courtCase.id}">
					<fmt:message key="dismissDocketLink"/>
				</a>
			</li>
		</c:if>
	</sec:authorize>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed }">
		<sec:authorize access="hasRole('LIST_COURT_CASE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/courtCase/courtCaseDetailsReport.html?courtCase=${courtCase.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="courtCaseDetailsReportLinkLabel"/></a>
			</li>
		</sec:authorize>
	</c:if>
</ul>
</fmt:bundle>