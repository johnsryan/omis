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
 - Author: Ryan Johns
 - Author: Annie Wahl
 - Author: Josh Divine
 - Version: 0.1.5 (May 15, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sec:authorize var="editPresentenceInvestigationRequest" access="hasRole('PRESENTENCE_INVESTIGATION_REQUEST_CREATE') or hasRole('ADMIN')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationRequest">
<form:form commandName="presentenceInvestigationRequestForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="presentenceInvestigationRequestTitle"/></legend>
		<span class="fieldGroup">
			<form:label path="assignedUserAccount" class="fieldLabel">
				<fmt:message key="assignedUserLabel"/>
			</form:label>
			<input id="assignedUserInput"/>
			<form:hidden path="assignedUserAccount"/>
			<a id="currentAssignedUser" class="currentUserAccountLink"></a>
			<a id="clearAssignedUser" class="clearLink"></a>
			<span id="assignedUserDisplay">
				<c:choose>
					<c:when test="${not empty presentenceInvestigationRequestForm.assignedUserAccount }">
						<c:set var="userAccount" value="${presentenceInvestigationRequestForm.assignedUserAccount}" scope="request"/>
						<jsp:include page="/WEB-INF/views/user/includes/userAccount.jsp"/>
					</c:when>
				</c:choose>
			</span>
			<form:errors path="assignedUserAccount" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="requestDate" class="fieldLabel">
				<fmt:message key="requestDateLabel"/>
			</form:label>
			<form:input path="requestDate" class="date"/>
			<form:errors path="requestDate" cssClass="error"/>
		</span>
		<c:choose>
		<c:when test="${empty presentenceInvestigationRequest}">
			<span class="fieldGroup">
				<form:label path="category" class="fieldLabel">
					<fmt:message key="categoryLabel"/>
				</form:label>
				<form:select path="category">
					<jsp:include page="../../../includes/nullOption.jsp"/>
					<c:forEach items="${categories}" var="cat">
						<option value="${cat.id}" ${cat == presentenceInvestigationRequestForm.category ? 'selected="selected"' : ''}>
							<c:out value="${cat.name}"/>
						</option>
					</c:forEach>
				</form:select>
				<form:errors path="category" cssClass="error"/>
			</span>
		</c:when>
		<c:otherwise>
			<form:hidden path="category"/>
			<form:errors path="category" cssClass="error"/>
		</c:otherwise>
		</c:choose>
		
		<fieldset style="border: 1px solid black;">
			<legend><fmt:message key="docketLabel" /></legend>
			<span class="fieldGroup">
				<form:label path="docketValue" class="fieldLabel">
					<fmt:message key="docketValueLabel"/>
				</form:label>
				<form:input path="docketValue" type="text" />
				<form:errors path="docketValue"  cssClass="error"/>
			</span>
			<span class="fieldGroup">
				<form:label path="court" class="fieldLabel">
					<fmt:message key="courtLabel"/>
				</form:label>
				<form:select path="court">
					<jsp:include page="../../../includes/nullOption.jsp"/>
					<c:forEach items="${courts}" var="ct">
						<option value="${ct.id}" ${ct == presentenceInvestigationRequestForm.court ? 'selected="selected"' : ''}>
							<c:out value="${ct.name}"/>
						</option>
					</c:forEach>
				</form:select>
				<form:errors path="court" cssClass="error"/>
			</span>
			<c:choose>
			<c:when test="${empty offender and empty presentenceInvestigationRequest}" >
				<span class="fieldGroup personOption">
					<label for="searchPerson" class="fieldLabel">
						<fmt:message key="personLabel"/>
					</label>
					<form:radiobutton path="createPerson" name="searchPerson" value="false" />
				</span>
				<span id="searchPersonFields">
					<span class="fieldGroup">
						<label for="searchOffenders" class="fieldLabel">
							<fmt:message key="searchOffendersLabel"/>
						</label>
						<input type="checkbox" id="searchOffenders" name="searchOffenders" />
					</span>
					<span class="fieldGroup">
						<form:label path="person" class="fieldLabel">
							<fmt:message key="personLabel"/>
						</form:label>
						<span id="searchFields">
							<input id="personInput"/>
							<form:hidden path="person"/>
							<a id="clearPerson" class="clearLink"></a>
							<span id="personDisplay">
								<c:choose>
									<c:when test="${not empty presentenceInvestigationRequestForm.person }">
										<c:set var="person" value="${presentenceInvestigationRequestForm.person}" scope="request"/>
										<c:out value="${person.name.lastName}"/>, <c:out value="${person.name.firstName}"/>
									</c:when>
								</c:choose>
							</span>
						</span>
						<form:errors path="person" cssClass="error"/>
					</span>
				</span>
				
				
				<span class="fieldGroup personOption" >
					<label for="createPerson" class="fieldLabel">
						<fmt:message key="newPersonLabel"/>
					</label>
					<form:radiobutton path="createPerson" name="createPerson" value="true"/>
				</span>
				<span id="createPersonFields">
					<jsp:include page="/WEB-INF/views/person/name/includes/nameFields.jsp"/>
				</span>
				
				
			</c:when>
			<c:when test="${empty offenderSummary and (not empty presentenceInvestigationRequest or not empty offender)}">
				<span class="fieldGroup">
					<form:label path="person" class="fieldLabel">
						<fmt:message key="personLabel"/>
					</form:label>
					<c:set var="person" value="${presentenceInvestigationRequestForm.person}" scope="request" />
					<input value="${person.name.lastName}, ${person.name.firstName}" disabled />
					<form:hidden path="person"/>
					<form:errors path="person" cssClass="error"/>
				</span>
			</c:when>
			<c:otherwise>
				<form:hidden path="person"/>
				<form:errors path="person" cssClass="error"/>
			</c:otherwise>
			</c:choose>
		</fieldset>
		<span class="fieldGroup">
			<form:label path="sentenceDate" class="fieldLabel">
				<fmt:message key="sentenceDateLabel"/>
			</form:label>
			<form:input path="sentenceDate" class="date"/>
			<form:errors path="sentenceDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="submissionDate" class="fieldLabel">
				<fmt:message key="submissionDateLabel"/>
			</form:label>
			<form:input path="submissionDate" class="date"/>
			<form:errors path="submissionDate" cssClass="error"/>
		</span>
	</fieldset>
	
	<fieldset>
		<legend><fmt:message key="presentenceInvestigationDelaysTitle"/></legend>
		<span class="fieldGroup">
			<c:set var="delayCategories" value="${delayCategories}" scope="request" />
			<c:set var="presentenceInvestigationDelayItems" value="${presentenceInvestigationRequestForm.presentenceInvestigationDelayItems}" scope="request"/>
			<jsp:include page="presentenceInvestigationDelayTable.jsp"/>
		</span>
	</fieldset>
	
	<fieldset>
		<legend><fmt:message key="presentenceInvestigationRequestNotesTitle"/></legend>
		<span class="fieldGroup">
			<c:set var="presentenceInvestigationRequestNoteItems" value="${presentenceInvestigationRequestForm.presentenceInvestigationRequestNoteItems}" scope="request"/>
			<jsp:include page="presentenceInvestigationRequestNoteTable.jsp"/>
		</span>
	</fieldset>
	
	 
	<c:if test="${not empty presentenceInvestigationRequest}">
		<c:set var="updatable" value="${presentenceInvestigationRequest}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<c:if test="${editPresentenceInvestigationRequest}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>
