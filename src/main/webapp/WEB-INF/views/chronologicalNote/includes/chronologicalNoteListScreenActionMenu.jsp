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
 - Chronological note list screen action menu 
 - Author: Yidong Li
 - Version: 0.1.1 (Feb 6, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
	<ul>
		<sec:authorize access="hasRole('CHRONOLOGICAL_NOTE_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" id="chronologicalNoteListScreenActionMenuLink" href="${pageContext.request.contextPath}/chronologicalNote/create.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="chronologicalNoteCreateLabel"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('CHRONOLOGICAL_NOTE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
				<li>
					<a href="${pageContext.request.contextPath}/chronologicalNote/chronologicalNoteListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="chronologicalNoteListingReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CHRONOLOGICAL_NOTE_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			    <li>
			        <omis:reportPro reportPath="/CaseManagement/Chronological_Notes/Chronological_Notes_Search&DOC_ID=${offender.offenderNumber}" decorate="no" title="" className="newTab reportLink"><fmt:message key="chronologicalNoteSearchReportLinkLabel"/></omis:reportPro>
			    </li>
		    </c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>