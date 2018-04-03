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
 - Author: Josh Divine
 - Version: 0.1.0 (Jan 17, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysisHome">
	<ul>
		<sec:authorize access="hasRole('PAROLE_ELIGIBILITY_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/paroleEligibility/list.html?offender=${hearingAnalysis.eligibility.offender.id}"><span class="visibleLinkLabel"><fmt:message key="viewParoleEligibilityLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_ANALYSIS_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/hearingAnalysis/edit.html?eligibility=${hearingAnalysis.eligibility.id}">
					<span class="visibleLinkLabel"><fmt:message key="editHearingAnalysisLink"/></span></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>