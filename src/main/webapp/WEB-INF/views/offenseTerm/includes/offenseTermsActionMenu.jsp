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
  - Action menu for offense terms.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offenseterm.msgs.offenseTerm" var="offenseTermBundle"/>
<ul>
	<c:if test="${not empty person}">
		<sec:authorize access="hasRole('OFFENSE_TERM_CREATE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/create.html?person=${person.id}" class="createLink"><span class="visibleLinkLabel"><fmt:message key="createCourtCaseLink" bundle="${offenseTermBundle}"/></span></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty person}">
		<sec:authorize access="hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/courtCaseListingReport.html?person=${person.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="courtCaseListingReportLinkLabel" bundle="${offenseTermBundle}"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty person}">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/courtCaseDetailListingReport.html?person=${person.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="courtCaseDetailListingReportLinkLabel" bundle="${offenseTermBundle}" /></a>
			</li>
			</c:if>
		</sec:authorize>		
	</c:if>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<sec:authorize access="hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/edit.html?courtCase=${courtCase.id}" class="viewEditLink"><span class="visibleLinkLabel"><fmt:message key="viewEditOffenseTermLink" bundle="${offenseTermBundle}"/></span></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<sec:authorize access="hasRole('OFFENSE_TERM_DOCKET_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/editDocket.html?courtCase=${courtCase.id}" class="viewEditLink"><span class="visibleLinkLabel"><fmt:message key="editOffenseTermDocketLink" bundle="${offenseTermBundle}"/></span></a>
			</li>
		</sec:authorize>
	</c:if>
	<sec:authorize access="hasRole('PROBATION_TERM_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/probationTerm/list.html?courtCase=${courtCase.id}">
					<fmt:message key="viewProbationTermsLink" bundle="${offenseTermBundle}"/>
				</a>
			</li>
		</c:if>
	</sec:authorize>
	<c:if test="${not empty courtCase}">
		<sec:authorize access="hasRole('OFFENSE_TERM_REMOVE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/remove.html?courtCase=${courtCase.id}" class="removeLink"><span class="visibleLinkLabel"><fmt:message key="removeOffenseTermLink" bundle="${offenseTermBundle}"/></span></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<sec:authorize access="hasRole('OFFENSE_TERM_REMOVE') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/dismissDocket.html?courtCase=${courtCase.id}" class="dismissDocketLink"><span class="visibleLinkLabel"><fmt:message key="dismissDocketLink" bundle="${offenseTermBundle}"/></span></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<sec:authorize access="hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/courtCaseDetailsReport.html?courtCase=${courtCase.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="courtCaseDetailsReportLinkLabel" bundle="${offenseTermBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty courtCase and not courtCase.flags.dismissed}">
		<sec:authorize access="hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/offenseTerm/reportOfViolationReport.rtf?courtCase=${courtCase.id}&reportFormat=RTF" class="reportLink"><fmt:message key="reportOfViolationReportLinkLabel" bundle="${offenseTermBundle}"/></a>
			</li>
		</sec:authorize>
	</c:if>	
</ul>