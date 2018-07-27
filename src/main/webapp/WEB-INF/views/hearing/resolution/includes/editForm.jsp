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
	<c:choose>
		<c:when test="${not empty disciplinaryViolationSummaryMap}">
			<c:set var="disciplinary" value="true" />
			<c:set var="violationSummaryMap" value="${disciplinaryViolationSummaryMap}" />
		</c:when>
		<c:when test="${not empty supervisoryViolationSummaryMap}">
			<c:set var="supervisory" value="true" />
			<c:set var="violationSummaryMap" value="${supervisoryViolationSummaryMap}" />
		</c:when>
	</c:choose>
	
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
		
			<%-- FORMAL --%>
			
			<fieldset>
				<legend>
					<fmt:message key="violationsLabel" />
				</legend>
				<c:if test="${fn:length(resolutionForm.violationItems) gt 1}">
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
				</c:if>
				
				<c:forEach items="${violationSummaryMap}" var="summary"  varStatus="status">
				<c:choose>
					<c:when test="${not empty disciplinary}">
						<c:set var="violationId" value="${summary.key.disciplinaryCodeViolationId}" />
					</c:when>
					<c:when test="${not empty supervisory}">
						<c:set var="violationId" value="${summary.key.conditionViolationId}" />
					</c:when>
				</c:choose>
				<c:set var="i" value="${status.index}" />
				<c:forEach var="violationItem" items="${resolutionForm.violationItems}">
				<c:if test="${violationItem.disciplinaryCodeViolation.id eq violationId or violationItem.conditionViolation.id eq violationId}" >
				<div class="violationItem" >
					<form:input type="hidden" path="violationItems[${i}].infraction" />
					<form:input type="hidden" path="violationItems[${i}].disciplinaryCodeViolation" />
					<form:input type="hidden" path="violationItems[${i}].conditionViolation" />
					<form:input type="hidden" path="violationItems[${i}].adjusted" />
					
					<div id="violationDescription${i}" class="violationDescription" >
						<span class="fieldGroup">
							<label class="fieldLabel">
								<fmt:message key="violationDescriptionLabel"/>
							</label>
							<span class="violationDescriptionNoOverflow">
								<c:out value="${summary.key.violationEventDetails}" />
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
							<fmt:message key="violationFormattedLabel">
								<c:choose>
									<c:when test="${not empty disciplinary}">
										<fmt:param value="${summary.key.disciplinaryCodeValue}" />
										<fmt:param value="${summary.key.disciplinaryCodeDescription}" />
									</c:when>
									<c:when test="${not empty supervisory}">
										<fmt:param value="${summary.key.conditionTitle}" />
										<fmt:param value="${summary.key.conditionClause}" />
									</c:when>
								</c:choose>
							</fmt:message>
							
							<a href="${pageContext.request.contextPath}/hearing/resolution/modifyViolation.html" class="modifyViolationLink" id="modifyViolationLink${i}">Modify</a>
						</span>
						<c:set var="hideModify" value="hidden" />
						<c:if test="${resolutionForm.violationItems[i].adjusted eq true}">
							<c:set var="hideModify" value="" />
						</c:if>
						
						<span class="fieldGroup modification ${hideModify}" id="violationModification${i}">
						<c:choose>
							<c:when test="${not empty disciplinary}">
									<form:label path="violationItems[${i}].adjustedDisciplinaryCode" class="fieldLabel">
										<fmt:message key="adjustedDisciplinaryCodeLabel"/>
									</form:label>
									<form:select path="violationItems[${i}].adjustedDisciplinaryCode">
										<jsp:include page="../../../includes/nullOption.jsp"/>
										<c:forEach items="${summary.value}" var="disciplinaryCode">
											<option value="${disciplinaryCode.id}" ${disciplinaryCode == resolutionForm.violationItems[i].adjustedDisciplinaryCode ? 'selected="selected"' : ''}>
												<fmt:message key="violationFormattedLabel">
													<fmt:param value="${disciplinaryCode.value}" />
													<fmt:param value="${disciplinaryCode.description}" />
												</fmt:message>
											</option>
										</c:forEach>
									</form:select>
									<form:errors path="violationItems[${i}].adjustedDisciplinaryCode" cssClass="error"/>
							</c:when>
							<c:when test="${not empty supervisory}">
									<form:label path="violationItems[${i}].adjustedCondition" class="fieldLabel">
										<fmt:message key="adjustedConditionLabel"/>
									</form:label>
									<form:select path="violationItems[${i}].adjustedCondition">
										<jsp:include page="../../../includes/nullOption.jsp"/>
										<c:forEach items="${summary.value}" var="condition">
											<option value="${condition.id}" ${condition == resolutionForm.violationItems[i].adjustedCondition ? 'selected="selected"' : ''}>
												<c:out value="${condition.conditionClause.conditionTitle.title}" />
											</option>
										</c:forEach>
									</form:select>
									<form:errors path="violationItems[${i}].adjustedCondition" cssClass="error"/>
							</c:when>
						</c:choose>
						</span>
					</div>
					
					
							
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
					
					<span class="fieldGroup">
						<c:choose>
							<c:when test="${not (violationItem.disposition eq 'GUILTY'
									or violationItem.disposition eq 'REDUCED')}">
								<span id="sanction${i}" class="hidden">
									<form:label path="violationItems[${i}].sanction" class="fieldLabel">
										<fmt:message key="sanctionLabel"/>
									</form:label>
									<form:textarea path="violationItems[${i}].sanction" />
									<form:errors path="violationItems[${i}].sanction" cssClass="error"/>
								</span>
							</c:when>
							<c:otherwise>
								<span id="sanction${i}">
									<form:label path="violationItems[${i}].sanction" class="fieldLabel">
										<fmt:message key="sanctionLabel"/>
									</form:label>
									<form:textarea path="violationItems[${i}].sanction"/>
									<form:errors path="violationItems[${i}].sanction" cssClass="error"/>
								</span>
							</c:otherwise>
						</c:choose>
					</span>
					
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
				</c:if>
				</c:forEach>
				</c:forEach>
			</fieldset>
		</c:when>
		
		<%-- INFORMAL/DISMISSED --%>
		
		<c:when test="${resolutionCategory eq 'INFORMAL' or resolutionCategory eq 'DISMISSED'}">
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
				<c:forEach items="${violationSummaryMap}" var="summary"  varStatus="status">
					<c:choose>
						<c:when test="${not empty disciplinary}">
							<c:set var="violationId" value="${summary.key.disciplinaryCodeViolationId}" />
						</c:when>
						<c:when test="${not empty supervisory}">
							<c:set var="violationId" value="${summary.key.conditionViolationId}" />
						</c:when>
					</c:choose>
					<c:set var="i" value="${status.index}" />
					<c:forEach var="violationItem" items="${resolutionForm.violationItems}">
					<c:if test="${violationItem.disciplinaryCodeViolation.id eq violationId or violationItem.conditionViolation.id eq violationId}" >
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
								<c:out value="${summary.key.violationEventDetails}" />
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
							<fmt:message key="violationFormattedLabel">
								<c:choose>
									<c:when test="${not empty disciplinary}">
										<fmt:param value="${summary.key.disciplinaryCodeValue}" />
										<fmt:param value="${summary.key.disciplinaryCodeDescription}" />
									</c:when>
									<c:when test="${not empty supervisory}">
										<fmt:param value="${summary.key.conditionTitle}" />
										<fmt:param value="${summary.key.conditionClause}" />
									</c:when>
								</c:choose>
							</fmt:message>
						</span>
					</div>
					
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
					
					<span class="fieldGroup">
						<span id="sanction${i}">
							<form:label path="violationItems[${i}].sanction" class="fieldLabel">
								<fmt:message key="sanctionLabel"/>
							</form:label>
							<form:textarea path="violationItems[${i}].sanction"/>
							<form:errors path="violationItems[${i}].sanction" cssClass="error"/>
						</span>
					</span>
					
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
				</c:if>
				</c:forEach>
				</c:forEach>
			</fieldset>
		</c:when>
	</c:choose>
	
	
	
<c:if test="${editResolution}">
	<p class="buttons">
		<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
	</p>
</c:if>
</form:form>
</fmt:bundle>
