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
 - Table body content of assessments.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Mar 28, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessment">
<c:forEach var="assessment" items="${assessmentSummaries}" varStatus="status">
	<tr>
		<td>
			<a class="actionMenuItem" id="rowActionMenuLink${status.index}" href="${pageContext.request.contextPath}/assessment/assessmentsActionMenu.html?assessment=${assessment.id}"></a>
		</td>
		<td><c:out value="${assessment.key.questionnaireCategoryName}"/></td>
		<td><c:out value="${assessment.key.questionnaireTypeName}"/></td>
		<td>
			<table id="assessmentCategoryScoreSummariesTable${status.index}" class="listTable">
				<tbody>
					<c:set var="assessmentCategoryScoreSummaries" value="${assessment.value}" scope="request" />
					<jsp:include page="assessmentCategoryScoreListTableBodyContent.jsp"/>
				</tbody>
			</table>
		</td>
		<td><fmt:formatDate value="${assessment.key.administeredQuestionnaireDate}" pattern="MM/dd/yyyy"/></td>
		<td>
			<c:out value="${assessment.key.administeredQuestionnaireAssessorLastName}, ${assessment.key.administeredQuestionnaireAssessorFirstName}"/>
		</td>
	</tr>
</c:forEach>
</fmt:bundle>