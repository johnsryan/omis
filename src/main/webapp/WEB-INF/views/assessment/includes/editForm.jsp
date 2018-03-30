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
 - Edit form screen for assessments.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Mar 28, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.assessment.msgs.assessment">
<form:form commandName="assessmentForm" class="editForm" enctype="multipart/form-data">
<fieldset>
	<legend><fmt:message key="assessmentDetailsTitle"/></legend>
	<span class="fieldGroup">
		<form:label path="questionnaireType" class="fieldLabel">
		<fmt:message key="assessmentLabel"/></form:label>
		<form:select path="questionnaireType">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="questionType" items="${questionnaireTypes}">
				<option value="${questionType.id}" ${assessmentForm.questionnaireType == questionType ? 'selected="selected"' : ''}><c:out value="${questionType.title}"/></option>
			</c:forEach>
		</form:select>
		<form:errors path="questionnaireType" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="assessDate" class="fieldLabel">
			<fmt:message key="assessmentDateLabel"/></form:label>
		<form:input path="assessDate" class="date"/> 
		<form:errors path="assessDate" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<c:choose>
			<c:when test="${not empty assessmentForm.assessor}">
				<c:set value="hidden" var="displayInput"/>
			</c:when>
			<c:otherwise>
				<c:set value="" var="displayInput"/>
			</c:otherwise>
		</c:choose>
		<form:label path="assessor" class="fieldLabel">
			<fmt:message key="assessorLabel"/></form:label>
		<form:hidden path="assessor"/>
		<input type="text" id="assessorInput" class="${displayInput}"/>
<%-- 		<a id="assessorCurrent" class="currentUserAccountLink" ${displayInput}></a> --%>
		<a id="assessorClear" class="clearLink" ${displayInput}></a>
		<span id="assessorDisplay">
			<c:if test="${not empty assessmentForm.assessor}">
				<c:set var="name" value="${assessmentForm.assessor.name}" scope="request"/>
				<c:out value="${name.lastName}, ${name.firstName}"/>
				<c:if test="${not empty name.middleName}">
					<c:out value=" ${name.middleName}"/>
				</c:if>
			</c:if>
		</span>
		<form:errors path="assessor" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="comments" class="fieldLabel">
			<fmt:message key="commentsLabel"/></form:label>
		<form:textarea path="comments"/> 
		<form:errors path="comments" cssClass="error"/>
	</span>
</fieldset>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>