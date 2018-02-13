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
 - Edit form screen for parole reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Jan 29, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.parolereview.msgs.paroleReview">
<form:form commandName="paroleReviewForm" class="editForm" enctype="multipart/form-data">
<fieldset>
	<legend><fmt:message key="paroleReviewDetailsTitle"/></legend>
	
	<span class="fieldGroup">
		<form:label path="date" class="fieldLabel">
			<fmt:message key="dateLabel"/></form:label>
		<form:input path="date" class="date"/> 
		<form:errors path="date" cssClass="error"/>
	</span>
	
	<span class="fieldGroup">
		<c:choose>
			<c:when test="${not empty paroleReviewForm.staffAssignment}">
				<c:set value="hidden" var="displayInput"/>
			</c:when>
			<c:otherwise>
				<c:set value="" var="displayInput"/>
			</c:otherwise>
		</c:choose>
		<form:label path="staffAssignment" class="fieldLabel">
			<fmt:message key="staffLabel"/></form:label>
		<form:hidden path="staffAssignment"/>
		<input type="text" id="staffAssignmentInput" class="${displayInput}"/>
		<a id="staffAssignmentCurrent" class="currentUserAccountLink" ${displayInput}></a>
		<a id="staffAssignmentClear" class="clearLink" ${displayInput}></a>
		<span id="staffAssignmentDisplay">
			<c:if test="${not empty paroleReviewForm.staffAssignment}">
				<c:set var="name" value="${paroleReviewForm.staffAssignment.staffMember.name}" scope="request"/>
				<c:out value="${name.lastName}, ${name.firstName}"/>
				<c:if test="${not empty name.middleName}">
					<c:out value=" ${name.middleName}"/>
				</c:if>
				<c:if test="${not empty paroleReviewForm.staffAssignment.title}">
					<c:out value=" ${paroleReviewForm.staffAssignment.title.name}"/>
				</c:if>
			</c:if>
		</span>
		<form:errors path="staffAssignment" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="staffRole" class="fieldLabel">
		<fmt:message key="staffRoleLabel"/></form:label>
		<form:select path="staffRole">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="staffRoleCategory" items="${staffRoleCategories}">
				<option value="${staffRoleCategory.id}" ${paroleReviewForm.staffRole == staffRoleCategory ? 'selected="selected"' : ''}><c:out value="${staffRoleCategory.name}"/></option>
			</c:forEach>
		</form:select>
		<form:errors path="staffRole" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="endorsement" class="fieldLabel">
		<fmt:message key="endorsementLabel"/></form:label>
		<form:select path="endorsement">
			<option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></option>
			<c:forEach var="endorsementCategory" items="${endorsementCategories}">
				<option value="${endorsementCategory.id}" ${paroleReviewForm.endorsement == endorsementCategory ? 'selected="selected"' : ''}><c:out value="${endorsementCategory.name}"/></option>
			</c:forEach>
		</form:select>
		<form:errors path="endorsement" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="text" class="fieldLabel">
			<fmt:message key="descriptionLabel"/></form:label>
		<form:textarea path="text"/> 
		<form:errors path="text" cssClass="error"/>
	</span>
</fieldset>
<fieldset id="paroleReviewNotesHolder">
	<legend><fmt:message key="paroleReviewNotesTitle"/></legend>
	<jsp:include page="paroleReviewNotesTable.jsp"/>
	<form:errors path="paroleReviewNoteItems" cssClass="error"/>
</fieldset>
<fieldset>
	<legend>
		<fmt:message key="paroleReviewDocumentAssociationsTitle" />
	</legend>
	<span class="fieldGroup">
		<c:set var="paroleReviewDocumentAssociationItems" value="${paroleReviewForm.paroleReviewDocumentAssociationItems}" scope="request"/>
		<jsp:include page="documentAssociationItems.jsp"/>
	</span>
</fieldset>
<c:if test="${not empty paroleReview}">
<c:set var="updatable" value="${paroleReview}" scope="request"/>
<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
</c:if>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>