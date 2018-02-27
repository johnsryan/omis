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
 - Version: 0.1.1 (Feb 20, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sec:authorize var="editHearingAnalysisTask" access="hasRole('HEARING_ANALYSIS_TASK_EDIT') or hasRole('HEARING_ANALYSIS_TASK_CREATE') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.hearinganalysis.msgs.hearingAnalysis" var="hearingAnalysisBundle"/>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysisHome">
<form:form commandName="hearingAnalysisHomeForm" class="editForm">
	<jsp:include page="/WEB-INF/views/hearingAnalysis/home/includes/summaryHeader.jsp"/>
	
	<fieldset>
		<legend><fmt:message key="analysisTitle"/></legend>
		<c:set var="taskItemFieldPropertyName" value="analysisTaskItems" scope="request" />
		<c:set var="taskItems" value="${hearingAnalysisHomeForm.analysisTaskItems}" scope="request"/>
		<jsp:include page="taskItems.jsp"/>
	</fieldset>
	
	<fieldset>
		<legend><fmt:message key="planningTitle"/></legend>
		<c:set var="taskItemFieldPropertyName" value="planningTaskItems" scope="request" />
		<c:set var="taskItems" value="${hearingAnalysisHomeForm.planningTaskItems}" scope="request"/>
		<jsp:include page="taskItems.jsp"/>
	</fieldset>
	
	<fieldset id="hearingAnalysisNotesHolder">
		<legend><fmt:message key="hearingAnalysisNotesTitle" bundle="${hearingAnalysisBundle}"/></legend>
		<c:set var="hearingAnalysisNoteItems" value="${hearingAnalysisHomeForm.hearingAnalysisNoteItems}" scope="request"/>
		<jsp:include page="/WEB-INF/views/hearingAnalysis/includes/hearingAnalysisNotesTable.jsp"/>
		<form:errors path="hearingAnalysisNoteItems" cssClass="error"/>
	</fieldset>

	<c:if test="${editHearingAnalysisTask}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>