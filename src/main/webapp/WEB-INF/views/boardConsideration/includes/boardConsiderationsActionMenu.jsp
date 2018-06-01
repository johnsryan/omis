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
 - Version: 0.1.0 (May 31, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.boardconsideration.msgs.boardConsideration">
	<ul>
		<sec:authorize access="hasRole('BOARD_CONSIDERATION_CREATE') or hasRole('BOARD_CONSIDERATION_EDIT') or hasRole('ADMIN')">
			<li>
				<a id="createBoardConsiderationItemLink" class="createLink" href="${pageContext.request.contextPath}/boardConsideration/addBoardConsideration.html?boardConsiderationIndex=${boardConsiderationIndex}"><span class="visibleLinkLabel"><fmt:message key="addBoardConsiderationLink"/></span></a>
			</li>
		</sec:authorize>
		<li>
			<a id="viewEditHearingAnalysisItemLink" class="viewEditLink" href="${pageContext.request.contextPath}/hearingAnalysis/edit.html?hearingAnalysis=${hearingAnalysis.id}"><span class="visibleLinkLabel"><fmt:message key="hearingAnalysisLink"/></span></a>
		</li>
		<li>
			<a id="viewEditEligibilityItemLink" class="viewEditLink" href="${pageContext.request.contextPath}/paroleEligibility/edit.html?eligibility=${hearingAnalysis.eligibility.id}"><span class="visibleLinkLabel"><fmt:message key="eligibilityLink"/></span></a>
		</li>
	</ul>
</fmt:bundle>