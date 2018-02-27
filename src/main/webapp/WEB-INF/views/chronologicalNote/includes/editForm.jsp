<?xml version="1.0" encoding="UTF-8"?>
<%--
 -  OMIS - Offender Management Information System
 -  Copyright (C) 2011 - 2017 State of Montana
 -
 -  This program is free software: you can redistribute it and/or modify
 -  it under the terms of the GNU General Public License as published by
 -  the Free Software Foundation, either version 3 of the License, or
 -  (at your option) any later version.
 -
 -  This program is distributed in the hope that it will be useful,
 -  but WITHOUT ANY WARRANTY; without even the implied warranty of
 -  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -  GNU General Public License for more details.
 -
 -  You should have received a copy of the GNU General Public License
 -  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
	<form:form commandName="chronologicalNoteForm" class="editForm">
		<fieldset>
			<legend><fmt:message key="categoryLegendLabel"/></legend>
				<form:errors cssClass="error" path="items"/>
			<c:forEach items="${chronologicalNoteForm.items}" var="item" varStatus="status">
				<span class="categoryItem" id="categoryItemContainer${status.index}">
					<form:input type="hidden" value="${item.name}" path="items[${status.index}].name"/>
					<form:input type="hidden" value="${item.associated}" path="items[${status.index}].associated" id="categoryItemAssociated${status.index}"/>
					<form:input type="hidden" value="${item.category.id}" path="items[${status.index}].category"/>
					<form:input type="hidden" value="${item.operation}" path="items[${status.index}].operation" id="categoryItemOperation${status.index}"/>
					<c:choose>
						<c:when test="${item.associated and item.operation ne 'DISSOCIATE'  or item.operation eq 'ASSOCIATE'}">
							<label><input type="checkbox" class="categoryItemCheckBox" id="categoryItemCheckBox${status.index}" checked="checked"/><c:out value="${item.name}"/></label>
						</c:when>
						<c:otherwise>
							<label><input type="checkbox" class="categoryItemCheckBox" id="categoryItemCheckBox${status.index}" /><c:out value="${item.name}"/></label>
						</c:otherwise>
					</c:choose>
				</span>
			</c:forEach>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="noteDetailsLegend"/></legend>
			<span class="fieldGroup">
				<form:label path="date" class="fieldLabel"><fmt:message key="dateLabel"/></form:label>
				<form:input path="date" class="date"/>
				<form:errors cssClass="error" path="date"/>
			</span>
			<span class="fieldGroup">
				<form:label path="narrative" class="fieldLabel"><fmt:message key="narrativeLabel"/></form:label>
				<form:textarea path="narrative" class="narrative" maxLength="6000" rows="15"/>
				<form:errors cssClass="error" path="narrative"/>
				<span class="characterCounter" id="narrativeCharacterCounterContainer"></span>
			</span>
		</fieldset>
		<c:choose>
			<c:when test="${not empty chronologicalNote}">
				<c:set var="updatable" value="${chronologicalNote}" scope="request"/>
				<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
				<sec:authorize  access="#chronologicalNote.creationSignature.userAccount.username == authentication.name">
					<p class="buttons">
						<input type="submit" value="<fmt:message key='chronologicalNoteSaveLabel'/>"/>
					</p>
				</sec:authorize>
			</c:when>
			<c:otherwise>
				<p class="buttons">
					<input type="submit" value="<fmt:message key='chronologicalNoteSaveLabel'/>"/>
				</p>
			</c:otherwise>
		</c:choose>
	</form:form>
</fmt:bundle>