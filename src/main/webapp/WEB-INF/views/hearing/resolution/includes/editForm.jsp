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
					<form:label path="time" class="fieldLabel">
						<fmt:message key="timeLabel"/>
					</form:label>
					<form:input path="time" class="time"/>
					<form:errors path="time" cssClass="error"/>
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
					<fmt:message key="attendedUserLabel"/>
				</legend>
				<span class="fieldGroup">
					<form:errors path="userAttendanceItems" cssClass="error"/>
					<c:set var="userAttendanceItems" value="${resolutionForm.userAttendanceItems}" scope="request"/>
					<jsp:include page="userAttendanceTable.jsp"/>
				</span>
			</fieldset>
		</c:when>
	</c:choose>
	
	<fieldset>
	<legend>
		<fmt:message key="violationsLabel" />
	</legend>
	<c:choose>
		<c:when test="${fn:length(resolutionForm.violationItems) gt 1}">
			<span class="fieldGroup">
				<label for="groupEdit.false" class="fieldLabel">
					<fmt:message key="groupEditFalseLabel"/>
				</label>
				<form:radiobutton path="groupEdit" value="false" class="fieldValue"/>
				<label for="groupEdit.true" class="fieldLabel">
					<fmt:message key="groupEditTrueLabel"/>
				</label>
				<form:radiobutton path="groupEdit" value="true" class="fieldValue"/>
			</span>
			<span id="groupViolationDescription" class="violationDescription" style="display:none;"></span>
			<span id="groupViolations" style="display:none;"></span>
		</c:when>
		<c:otherwise>
			<form:input type="hidden" path="groupEdit" />
		</c:otherwise>
	</c:choose>
	<c:forEach var="violationItem" items="${resolutionForm.violationItems}" varStatus="status">
		<c:set var="i" value="${status.index}" />
		<div class="violationItem" >
		<form:input type="hidden" path="violationItems[${i}].infraction" />
		<form:input type="hidden" path="violationItems[${i}].disciplinaryCodeViolation" />
		<form:input type="hidden" path="violationItems[${i}].conditionViolation" />
		
		<div id="violationDescription${i}" class="violationDescription" >
			<span class="fieldGroup">
				<label class="fieldLabel">
					<fmt:message key="violationDescriptionLabel"/>
				</label>
				<span class="violationDescriptionNoOverflow">
					<c:out value="${violationItem.summary.violationEventDetails}" />
					<span class="hideOverflow"></span>
				</span>
				<span class="showOverflow"></span>
			</span>
		</div>
		<div id="violation${i}" class="violation">
			<span class="fieldGroup">
				<label class="fieldLabel">
					<fmt:message key="violationLabel"/>
				</label>
				<c:choose>
					<c:when test="${not empty violationItem.summary.disciplinaryCodeDescription}">
						<c:out value="${violationItem.summary.disciplinaryCodeValue}"/> - <c:out value="${violationItem.summary.disciplinaryCodeDescription}"/>
					</c:when>
					<c:when test="${not empty violationItem.summary.conditionClause}">
						<c:out value="${violationItem.summary.conditionTitle}"/> - <c:out value="${violationItem.summary.conditionClause}"/>
					</c:when>
				</c:choose>
			</span>
		</div>
		<c:choose>
			<c:when test="${resolutionCategory eq 'FORMAL'}">
				<span class="fieldGroup">
					<form:label path="violationItems[${i}].plea" class="fieldLabel">
						<fmt:message key="pleaLabel"/>
					</form:label>
					<form:select path="violationItems[${i}].plea">
						<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
						<form:options items="${infractionPleas}" itemLabel="name" itemValue="id"/>
					</form:select>
					<form:errors path="violationItems[${i}].plea" cssClass="error"/>
				</span>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${resolutionCategory eq 'INFORMAL'
							or resolutionCategory eq 'DISMISSED'}">
				<span class="fieldGroup">
					<form:label path="violationItems[${i}].decision" class="fieldLabel">
						<fmt:message key="decisionLabel"/>
					</form:label>
					<form:textarea path="violationItems[${i}].decision"/>
					<form:errors path="violationItems[${i}].decision" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="violationItems[${i}].date" class="fieldLabel">
						<fmt:message key="dateLabel"/>
					</form:label>
					<form:input path="violationItems[${i}].date" class="date"/>
					<form:errors path="violationItems[${i}].date" cssClass="error"/>
				</span>
				<span class="fieldGroup">
					<form:label path="violationItems[${i}].authority" class="fieldLabel">
						<fmt:message key="authorityLabel"/>
					</form:label>
					<input id="authorityInput${i}"/>
						<form:hidden id="violationItems[${i}].authority" path="violationItems[${i}].authority"/>
						<a id="clearAuthority${i}" class="clearLink"></a>
					<span id="authorityDisplay${i}">
						<c:if test="${not empty violationItem.authority}" >
							<c:out value="${violationItem.authority.name.lastName}, ${violationItem.authority.name.firstName}"/>
						</c:if>
					</span>
					<form:errors path="violationItems[${i}].authority" cssClass="error"/>
				</span>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${resolutionCategory eq 'INFORMAL'
							or resolutionCategory eq 'FORMAL'}">
				<c:choose>
					<c:when test="${resolutionCategory eq 'FORMAL'}">
						<span class="fieldGroup">
							<form:label path="violationItems[${i}].disposition" class="fieldLabel">
								<fmt:message key="dispositionLabel"/>
							</form:label>
							<form:select path="violationItems[${i}].disposition" class="disposition">
								<jsp:include page="../../../includes/nullOption.jsp"/>
								<c:forEach items="${dispositionCategories}" var="disposition">
									<option value="${disposition}" ${disposition == resolutionForm.violationItems[i].disposition ? 'selected="selected"' : ''}>
										<fmt:message key="${disposition.name}DispositionLabel"/>
									</option>
								</c:forEach>
							</form:select>
							<form:errors path="violationItems[${i}].disposition" cssClass="error"/>
						</span>
					</c:when>
				</c:choose>
				<span class="fieldGroup">
						<c:choose>
							<c:when test="${(not (violationItem.disposition eq 'GUILTY' or violationItem.disposition eq 'REDUCED')) and resolutionCategory eq 'FORMAL'}">
								<span id="sanction${i}" class="hidden">
									<form:label path="violationItem.sanction" class="fieldLabel">
										<fmt:message key="sanctionLabel"/>
									</form:label>
									<form:textarea path="violationItems[${i}].sanction" />
									<form:errors path="violationItems[${i}].sanction" cssClass="error"/>
								</span>
							</c:when>
							<c:otherwise>
								<span id="sanction${i}">
									<form:label path="violationItem.sanction" class="fieldLabel">
										<fmt:message key="sanctionLabel"/>
									</form:label>
									<form:textarea path="violationItems[${i}].sanction"/>
									<form:errors path="violationItems[${i}].sanction" cssClass="error"/>
								</span>
							</c:otherwise>
						</c:choose>
					</span>
			</c:when>
		</c:choose>
		<span class="fieldGroup">
			<form:label path="violationItems[${i}].reason" class="fieldLabel">
				<fmt:message key="reasonForDecisionLabel"/>
			</form:label>
			<form:textarea path="violationItems[${i}].reason"/>
			<form:errors path="violationItems[${i}].reason" cssClass="error"/>
		</span>
		<span class="fieldGroup">
			<form:label path="violationItems[${i}].appealDate" class="fieldLabel">
				<fmt:message key="appealDateLabel"/>
			</form:label>
			<form:input path="violationItems[${i}].appealDate" class="date"/>
			<form:errors path="violationItems[${i}].appealDate" cssClass="error"/>
		</span>
		</div>
	</c:forEach>
	</fieldset>
	
<c:if test="${editResolution}">
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
	</p>
</c:if>
</form:form>
</fmt:bundle>
