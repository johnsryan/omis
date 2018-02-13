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
 - Form to edit court cases.
 -
 - Author: Stephen Abson
 - Author: Joel Norris
 - Author: Josh Divine
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle var="docketBundle" basename="omis.docket.msgs.docket"/>
<fmt:bundle basename="omis.courtcase.msgs.courtCase">
<form:form commandName="courtCaseForm" class="editForm">
<fieldset>
		<legend><fmt:message key="docketLabel" bundle="${docketBundle}"/></legend>
		<form:hidden path="allowExistingDocket"/>
		<c:if test="${courtCaseForm.allowExistingDocket}">
			<span class="fieldGroup">
				<form:label class="fieldLabel" path="existingDocket"><fmt:message key="existingDocketLabel"/></form:label>
				<form:select path="existingDocket">
					<form:option value=""><fmt:message key="createDocketLabel"/></form:option>
					<c:forEach var="existingDocket" items="${existingDockets}">
						<fmt:message var="docketLabel" key="docketText" bundle="${docketBundle}">
							<fmt:param>${existingDocket.value}</fmt:param>
							<fmt:param>${existingDocket.court.name}</fmt:param>
						</fmt:message>
						<c:choose>
							<c:when test="${courtCaseForm.existingDocket eq existingDocket}">
								<form:option value="${existingDocket.id}" selected="selected">${docketLabel}</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${existingDocket.id}">${docketLabel}</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>
				<form:errors path="existingDocket" cssClass="error"/>
			</span>
		</c:if>
		<form:hidden path="allowDocket"/>
		<c:choose>
			<c:when test="${courtCaseForm.allowDocket}">
				<c:set var="docketFieldsEnabled" value="${empty courtCaseForm.existingDocket}" scope="request"/>
				<c:set var="fieldsPropertyName" value="docketFields" scope="request"/>
				<c:set var="docketFields" value="${courtCaseForm.docketFields}" scope="request"/>
				<jsp:include page="/WEB-INF/views/docket/includes/docketFields.jsp"/>
			</c:when>
			<c:otherwise>
				<c:set var="docket" value="${docket}" scope="request"/>
				<jsp:include page="/WEB-INF/views/docket/includes/docket.jsp"/>
			</c:otherwise>
		</c:choose>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="courtCaseLegendLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="interStateNumber" class="fieldLabel">
				<fmt:message key="interStateNumberLabel"/></form:label>
			<form:input path="interStateNumber"/>
			<form:errors path="interStateNumber" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="interState" class="fieldLabel">
				<fmt:message key="interStateLabel"/></form:label>
			<form:select path="interState">
				<form:option value="">...</form:option>
				<form:options items="${states}" itemLabel="name" itemValue="id"/>
			</form:select>
			<form:errors path="interState" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="pronouncementDate" class="fieldLabel">
				<fmt:message key="pronouncementDateLabel"/></form:label>
			<form:input path="pronouncementDate" class="date"/>
			<form:errors path="pronouncementDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="jurisdictionAuthority" class="fieldLabel">
				<fmt:message key="jurisdictionAuthorityLabel"/></form:label>
			<form:select path="jurisdictionAuthority">
				<form:option value="">...</form:option>
				<c:forEach items="${jurisdictionAuthorities}" var="jurisdictionAuthority">
					<c:choose>
						<c:when test="${not empty courtCaseForm.jurisdictionAuthority and courtCaseForm.jurisdictionAuthority eq jurisdictionAuthority}">
							<form:option selected="selected" value="${jurisdictionAuthority}"><fmt:message key="jurisdictionAuthorityLabel.${jurisdictionAuthority}" /></form:option>
						</c:when>
						<c:otherwise>
							<form:option value="${jurisdictionAuthority}"><fmt:message key="jurisdictionAuthorityLabel.${jurisdictionAuthority}" /></form:option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</form:select>
			<form:errors path="jurisdictionAuthority" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="dangerDesignator" class="fieldLabel">
				<fmt:message key="dangerDesignatorLabel"/></form:label>
			<form:select path="dangerDesignator">
				<form:option value="">...</form:option>
				<c:forEach items="${dangerDesignators}" var="dangerDesignator">
					<c:choose>
						<c:when test="${not empty courtCaseForm.dangerDesignator and courtCaseForm.dangerDesignator eq dangerDesignator}">
							<form:option selected="selected" value="${dangerDesignator}"><fmt:message key="dangerDesignatorLabel.${dangerDesignator}" /></form:option>
						</c:when>
						<c:otherwise>
							<form:option value="${dangerDesignator}"><fmt:message key="dangerDesignatorLabel.${dangerDesignator}" /></form:option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</form:select>
			<form:errors path="dangerDesignator" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="sentenceReviewDate" class="fieldLabel">
				<fmt:message key="sentenceReviewDateLabel"/></form:label>
			<form:input path="sentenceReviewDate" class="date"/>
			<form:errors path="sentenceReviewDate" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="convictionOverturned" class="fieldLabel"><fmt:message key="convictionsOverturnedLabel"/></form:label>
			<form:checkbox path="convictionOverturned" value="true"/>
		</span>
		<span class="fieldGroup">
			<form:label path="criminallyConvictedYouth" class="fieldLabel"><fmt:message key="criminallyConvictedYouthLabel"/></form:label>
			<form:checkbox path="criminallyConvictedYouth" value="true"/>
		</span>
		<span class="fieldGroup">
			<form:label path="dismissed" class="fieldLabel"><fmt:message key="dismissedLabel"/></form:label>
			<form:checkbox path="dismissed" value="true"/>
		</span>
		<span class="fieldGroup">
			<form:label path="youthTransfer" class="fieldLabel"><fmt:message key="youthTransferLabel"/></form:label>
			<form:checkbox path="youthTransfer" value="true"/>
		</span>
		<span class="fieldGroup">
			<form:label path="comments" class="fieldLabel">
				<fmt:message key="commentsLabel"/>
			</form:label>
			<form:textarea path="comments"/>
		</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="courtPersonnelLabel"/></legend>
		<span class="fieldGroup">
		  <form:label path="judge" class="fieldLabel">
					<fmt:message key="judgeLabel"/>
			</form:label>
			<input id="judgeQuery"/>
			<form:hidden path="judge"/>
			<a id="judgeClear" class="clearLink"></a>
			<span id="judgeDisplay">
			<c:if test="${not empty courtCaseForm.judge}">
				<c:out value="${courtCaseForm.judge.name.lastName}"/>,
				<c:out value="${courtCaseForm.judge.name.firstName}"/>
			</c:if>
			</span>
			<form:errors path="judge" cssClass="error"/>
		</span>
		<span class="fieldGroup">
		  	<form:label path="defenseAttorneyName" class="fieldLabel">
				<fmt:message key="defenseAttorneyLabel"/></form:label>
			<form:input path="defenseAttorneyName"/>
			<form:errors path="defenseAttorneyName" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="prosecutingAttorneyName" class="fieldLabel">
				<fmt:message key="prosecutingAttorneyLabel"/></form:label>
			<form:input path="prosecutingAttorneyName"/>
			<form:errors path="prosecutingAttorneyName" cssClass="error"/>
		</span>
	</fieldset>
	<fieldset id="chargesHolder">
		<legend><fmt:message key="chargesTitle"/></legend>
		<%-- <a href="${pageContext.request.contextPath}/courtCase/addCharge.html" class="createLink" id="addChargeLink"><span class="visibleLinkLabel"><fmt:message key="addChargeLink"/></span></a>--%>
		<jsp:include page="chargesContent.jsp"/>
		<form:errors path="charges" cssClass="error"/>
	</fieldset>
	<fieldset id="notesHolder">
		<legend><fmt:message key="courtCaseNotesTitle"/></legend>
		<jsp:include page="courtCaseNotesTable.jsp"/>
		<form:errors path="noteItems" cssClass="error"/>
	</fieldset>
	<c:if test="${not empty courtCase}">
		<c:set var="updatable" value="${courtCase}" scope="request"/>
		<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
	</c:if>
	<p class="buttons">
		<button type="submit" name="operation" value="SAVE">
			<fmt:message key="saveLabel" bundle="${commonBundle}"/></button>
		<%-- <button type="submit" name="operation" value="SAVE_AND_CONTINUE">
			<fmt:message key="saveAndContinueLabel" bundle="${commonBundle}"/></button> --%>
	</p>
</form:form>
</fmt:bundle>