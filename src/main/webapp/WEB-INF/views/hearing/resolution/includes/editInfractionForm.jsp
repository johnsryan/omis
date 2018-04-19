<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editResolution" access="hasRole('VIOLATION_EDIT') or hasRole('ADMIN') or hasRole('VIOLATION_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.hearing.msgs.hearing">
<form:form commandName="resolutionForm" class="editForm">
	<form:input type="hidden" path="resolutionCategory" />
	<c:choose>
		<c:when test="${resolutionCategory eq 'FORMAL'}">
			<fieldset>
				<legend>
					<fmt:message key="hearingStatusLabel" />
				</legend>
				<span class="fieldGroup">
					<form:label path="category" class="fieldLabel">
						<fmt:message key="categoryLabel"/>
					</form:label>
					<form:select path="category" readonly="true">
						<jsp:include page="../../../includes/nullOption.jsp"/>
						<c:forEach items="${hearingStatusCategories}" var="cat">
							<option value="${cat}" ${cat == resolutionForm.category ? 'selected="selected"' : ''}>
								<fmt:message key="${cat.name}StatusLabel"/>
							</option>
						</c:forEach>
					</form:select>
					<form:errors path="category" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="date" class="fieldLabel">
						<fmt:message key="hearingDateLabel"/>
					</form:label>
					<form:input path="date" class="date"/>
					<form:errors path="date" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="inAttendance" class="fieldLabel">
						<fmt:message key="offenderPresentLabel"/>
					</form:label>
					<form:checkbox path="inAttendance" />
					<form:errors path="inAttendance" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="statusDescription" class="fieldLabel">
						<fmt:message key="statusDescriptionLabel"/>
					</form:label>
					<form:textarea path="statusDescription"/>
					<form:errors path="statusDescription" cssClass="error"/>
				</span>
			</fieldset>
			
			<!-- Attended Staff Items -->
			<fieldset>
				<legend>
					<fmt:message key="attendedStaffLabel"/>
				</legend>
				<span class="fieldGroup">
					<form:errors path="staffAttendanceItems" cssClass="error"/>
					<c:set var="staffAttendanceItems" value="${resolutionForm.staffAttendanceItems}" scope="request"/>
					<jsp:include page="staffAttendanceTable.jsp"/>
				</span>
			</fieldset>
		</c:when>
	</c:choose>
	<fieldset>
	<legend>
		<fmt:message key="violationsLabel" />
	</legend>
	
	<div class="violationItem" >
		<div id="violationDescription" class="violationDescription" >
			<span class="fieldGroup">
				<label class="fieldLabel">
					<fmt:message key="violationDescriptionLabel"/>
				</label>
				<span class="violationDescriptionNoOverflow">
					<c:out value="${resolutionForm.violationItem.summary.violationEventDetails}" />
					<span class="hideOverflow"></span>
				</span>
				<span class="showOverflow"></span>
			</span>
		</div>
		<div id="violation" class="violation">
			<span class="fieldGroup">
				<label class="fieldLabel">
					<fmt:message key="violationLabel"/>
				</label>
				<c:out value="${not empty resolutionForm.violationItem.summary.conditionClause
					? resolutionForm.violationItem.summary.conditionClause
					: resolutionForm.violationItem.summary.disciplinaryCodeDescription}" />
			</span>
		</div>
		
		
		<c:choose>
			<c:when test="${resolutionCategory eq 'FORMAL'}">
				<span class="fieldGroup">
					<form:label path="violationItem.plea" class="fieldLabel">
						<fmt:message key="pleaLabel"/>
					</form:label>
					<form:select path="violationItem.plea">
						<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
						<form:options items="${infractionPleas}" itemLabel="name" itemValue="id"/>
					</form:select>
					<form:errors path="violationItem.plea" cssClass="error"/>
				</span>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${resolutionCategory eq 'INFORMAL'
							or resolutionCategory eq 'DISMISSED'}">
				<span class="fieldGroup">
					<form:label path="violationItem.decision" class="fieldLabel">
						<fmt:message key="decisionLabel"/>
					</form:label>
					<form:textarea path="violationItem.decision"/>
					<form:errors path="violationItem.decision" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="violationItem.date" class="fieldLabel">
						<fmt:message key="dateLabel"/>
					</form:label>
					<form:input path="violationItem.date" class="date"/>
					<form:errors path="violationItem.date" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="violationItem.authority" class="fieldLabel">
						<fmt:message key="authorityLabel"/>
					</form:label>
					<input id="authorityInput"/>
						<form:hidden id="violationItem.authority" path="violationItem.authority"/>
						<a id="clearAuthority" class="clearLink"></a>
					<span id="authorityDisplay">
						<c:if test="${not empty resolutionForm.violationItem.authority}" >
							<c:out value="${resolutionForm.violationItem.authority.name.lastName}, ${resolutionForm.violationItem.authority.name.firstName}"/>
						</c:if>
					</span>
					<form:errors path="violationItem.authority" cssClass="error"/>
				</span>
			</c:when>
		</c:choose>
		<span class="fieldGroup">
			<form:label path="violationItem.reason" class="fieldLabel">
				<fmt:message key="reasonLabel"/>
			</form:label>
			<form:textarea path="violationItem.reason"/>
			<form:errors path="violationItem.reason" cssClass="error"/>
		</span>
		<c:choose>
			<c:when test="${resolutionCategory eq 'INFORMAL'
							or resolutionCategory eq 'FORMAL'}">
				<c:choose>
					<c:when test="${resolutionCategory eq 'FORMAL'}">
						<span class="fieldGroup">
							<form:label path="violationItem.disposition" class="fieldLabel">
								<fmt:message key="dispositionLabel"/>
							</form:label>
							<form:select path="violationItem.disposition" class="disposition">
								<jsp:include page="../../../includes/nullOption.jsp"/>
								<c:forEach items="${dispositionCategories}" var="disposition">
									<option value="${disposition}" ${disposition == resolutionForm.violationItem.disposition ? 'selected="selected"' : ''}>
										<fmt:message key="${disposition.name}DispositionLabel"/>
									</option>
								</c:forEach>
							</form:select>
							<form:errors path="violationItem.disposition" cssClass="error"/>
						</span>
					</c:when>
				</c:choose>
				<span class="fieldGroup">
					<form:label path="violationItem.sanction" class="fieldLabel">
						<fmt:message key="sanctionLabel"/>
					</form:label>
					<c:if test="${resolutionCategory eq 'FORMAL'}">
						<fmt:message key='sanctionOnGuiltyLabel' var="placeholder"/>
					</c:if>
					<c:choose>
						<c:when test="${(not (resolutionForm.violationItem.disposition eq 'GUILTY')) and resolutionCategory eq 'FORMAL'}">
							<form:textarea path="violationItem.sanction" readOnly="true" placeholder="${placeholder}" />
						</c:when>
						<c:otherwise>
							<form:textarea path="violationItem.sanction" placeholder="${placeholder}" />
						</c:otherwise>
					</c:choose>
					<form:errors path="violationItem.sanction" cssClass="error"/>
				</span>
			</c:when>
		</c:choose>
		<span class="fieldGroup">
			<form:label path="violationItem.appealDate" class="fieldLabel">
				<fmt:message key="appealDateLabel"/>
			</form:label>
			<form:input path="violationItem.appealDate" class="date"/>
			<form:errors path="violationItem.appealDate" cssClass="error"/>
		</span>
	</div>
	</fieldset>
	
<c:if test="${editResolution}">
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
	</p>
</c:if>
</form:form>
</fmt:bundle>
