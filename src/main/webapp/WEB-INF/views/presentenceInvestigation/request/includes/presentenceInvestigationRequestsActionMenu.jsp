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
  - Author: Joel Norris
  - Author: Annie Wahl
  - Author: Sierra Rosales
  - Author: Josh Divine
  - Version: 0.1.6 (May 2, 2018)
  - Since: OMIS 3.0 
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
	<ul>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/create.html?assignedUser=${assignedUser.id}&offender=${offender.id}&onReturn=${onReturn}"><span class="visibleLinkLabel"><fmt:message key="createPresentenceInvestigationRequestLink"/></span></a>
			</li>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/presentenceInvestigation/request/list.html?assignedUser=${assignedUser.id}"><span class="visibleLinkLabel"><fmt:message key="listPresentenceInvestigationRequestsByUserLink"/></span></a>
			</li>
		</sec:authorize>
		</sec:authorize>
		<sec:authorize access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_LIST') or hasRole('ADMIN')">
		    <li>
		    	<omis:reportPro reportPath="/Legal/PSI/Overdue_PSI_Assignments_for_Supervising_Office" decorate="no" title="" className="newTab reportLink"><fmt:message key="overduePSIAssignmentsSupervisingOfficeReportLinkLabel"/></omis:reportPro>
		    </li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/investigationAssignmentReport.html?reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="investigationAssignmentReportLinkLabel"/></a>
			</li>
		    <li>
		    	<omis:reportPro reportPath="/Legal/PSI/PSI_Assignments_for_Supervising_Office" decorate="no" title="" className="newTab reportLink"><fmt:message key="psiAssignmentsSupervisingOfficeReportLinkLabel"/></omis:reportPro>
		    </li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/investigationProgressReport.html?reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="investigationProgressReportLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/investigationTaskReport.html?reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="investigationTaskReportLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/victimImpactStatementReport.html?reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="victimImpactReportLinkLabel"/></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/victimImpactStatementKidReport.html?reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="victimImpactKidReportLinkLabel"/></a>
			</li>			
		</sec:authorize>
	</ul>
</fmt:bundle>