<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.separationneed.msgs.separationNeed">
<form:form commandName="separationNeedForm" class="editForm">
	<fieldset>
		<legend><fmt:message key="separationNeedDetailsLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="date" class="fieldLabel"><fmt:message key="dateLabel"/></form:label>
			<form:input path="date" class="date"/>
			<form:errors cssClass="error" path="date"/>
		</span>
		<span class="fieldGroup">
			<form:label path="targetOffender" class="fieldLabel"><fmt:message key="targetOffenderLabel"/></form:label>
			<form:hidden path="targetOffender"/>		
			<c:if test="${separationNeedForm.createTargetOffender}">
				<input type="text" id="targetOffenderInput"/>
				<a id="targetOffenderClear" class="clearLink"></a>
			</c:if>	
			<span id="targetOffenderDisplay">
				<c:if test="${not empty separationNeedForm.targetOffender}">
					<fmt:message key="offenderLastFirstName">
						<fmt:param value="${separationNeedForm.targetOffender.name.lastName}"/>
						<fmt:param value="${separationNeedForm.targetOffender.name.firstName}"/>
					</fmt:message>
					<c:if test="${not empty separationNeedForm.targetOffender.name.middleName}">
						<fmt:message key="offenderMiddleInitial">
							<fmt:param value="${separationNeedForm.targetOffender.name.middleName}"/>
						</fmt:message>
					</c:if>					
						<c:out value="#${separationNeedForm.targetOffender.offenderNumber}"/>
				</c:if>
			</span>
			<form:errors cssClass="error" path="targetOffender"/>
		</span>
		<span class="fieldGroup">
			<form:label path="creationComment" class="fieldLabel"><fmt:message key="creationCommentLabel"/></form:label>
			<form:textarea path="creationComment" maxlength="4000"/>
			<span class="characterCounter" id="creationCommentCharacterCounter">
			</span>
			<form:errors cssClass="error" path="creationComment"/>
		</span>
		<span class="fieldGroup">
			<form:label path="reportingStaff" class="fieldLabel"><fmt:message key="reportingStaffLabel"/></form:label>
			<form:hidden path="reportingStaff"/>
			<input type="text" id="reportingStaffInput"/>
			<a id="reportingStaffCurrent" class="currentUserAccountLink"></a>
			<a id="reportingStaffClear" class="clearLink"></a>
			<span id="reportingStaffDisplay">
				<c:if test="${not empty separationNeedForm.reportingStaff}">
					<fmt:message key="reportingStaffInformation">
						<fmt:param value="${separationNeedForm.reportingStaff.name.lastName}"/>
						<fmt:param value="${separationNeedForm.reportingStaff.name.firstName}"/>
					</fmt:message>
				</c:if>
			</span>
			<form:errors cssClass="error" path="reportingStaff"/>
		</span>
		<span class="fieldGroup">
			<form:label  path="separationNeedReasons" class="fieldLabel">
				<fmt:message key="separationNeedReasonsLabel"/>
			</form:label>
<%-- 			<c:forEach items="${reasons}" var="reason" varStatus="status"> --%>
<%-- 				<form:label class="fieldValueLabel" path="separationNeedReasons"><c:out value="${reason.name}"/></form:label> --%>
<%-- 				<form:checkbox path="separationNeedReasons" value="${reason.id}"/> --%>
<%-- 			</c:forEach> --%>
			<form:checkboxes items="${reasons}" path="separationNeedReasons" itemLabel="name" itemValue="id" class="fieldValue"/>
			<form:errors path="separationNeedReasons" cssClass="error"/>
		</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="removalLegendLabel"/></legend>
		<span class="fieldGroup">
			<form:label path="removalDate" class="fieldLabel"><fmt:message key="removalDateLabel"/></form:label>
			<form:input path="removalDate" class="date"/>
			<form:errors cssClass="error" path="removalDate"/>
		</span>
		<span class="fieldGroup">
			<form:label path="removalReason" class="fieldLabel"><fmt:message key="removalReasonLabel"/></form:label>
			<form:select path="removalReason">
				<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
				<form:options items="${removalReasons}" itemLabel="name" itemValue="id"/>
			</form:select>
			<form:errors cssClass="error" path="removalReason"/>
		</span>
		<span class="fieldGroup">
			<form:label path="removalComment" class="fieldLabel"><fmt:message key="removalCommentLabel"/></form:label>
			<form:textarea path="removalComment" maxlength="4000"/>
			<span class="characterCounter" id="removalCommentCharacterCounter">
			</span>
			<form:errors cssClass="error" path="removalComment"/>
		</span>
	</fieldset>
	<fieldset>
		<legend><fmt:message key="notesLegendLabel"/></legend>
		<c:set var="separationNeedNoteItems" value="${separationNeedForm.separationNeedNoteItems}" scope="request"/>
		<jsp:include page="separationNeedNoteItemsTable.jsp"/>
	</fieldset>
		<c:if test="${not empty separationNeed}">
			<c:set var="updatable" value="${separationNeed}" scope="request"/>
			<jsp:include page="/WEB-INF/views/audit/includes/updateSignature.jsp"/>
		</c:if>
		<p class="buttons">
			<input type="submit" value="<fmt:message key='separationNeedSaveLabel'/>"/>
		</p>
</form:form>
</fmt:bundle>
