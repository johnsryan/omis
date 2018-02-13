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
 - Edit form screen for mental health reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 5, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.mentalhealthreview.msgs.mentalHealthReview">
<form:form commandName="mentalHealthReviewForm" class="editForm" enctype="multipart/form-data">
<fieldset>
	<legend><fmt:message key="mentalHealthReviewDetailsTitle"/></legend>
	
	<span class="fieldGroup">
		<form:label path="date" class="fieldLabel">
			<fmt:message key="dateLabel"/></form:label>
		<form:input path="date" class="date"/> 
		<form:errors path="date" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<form:label path="text" class="fieldLabel">
			<fmt:message key="descriptionLabel"/></form:label>
		<form:textarea path="text"/> 
		<form:errors path="text" cssClass="error"/>
	</span>
</fieldset>
<fieldset>
	<legend>
		<fmt:message key="mentalHealthReviewNotesTitle" />
	</legend>
	<span class="fieldGroup">
		<c:set var="mentalHealthReviewNoteItems" value="${mentalHealthReviewForm.mentalHealthNoteItems}" scope="request"/>
		<jsp:include page="mentalHealthReviewNoteTable.jsp"/>
	</span>
</fieldset>
<fieldset>
	<legend>
		<fmt:message key="mentalHealthReviewDocumentAssociationsTitle" />
	</legend>
	<span class="fieldGroup">
		<c:set var="mentalHealthReviewDocumentAssociationItems" value="${mentalHealthReviewForm.mentalHealthReviewDocumentAssociationItems}" scope="request"/>
		<jsp:include page="documentAssociationItems.jsp"/>
	</span>
</fieldset>
<c:if test="${not empty mentalHealthReview}">
<c:set var="updatable" value="${mentalHealthReview}" scope="request"/>
<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
</c:if>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>