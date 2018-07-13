<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<sec:authorize var="editBoardHearing" access="hasRole('BOARD_HEARING_EDIT') or hasRole('ADMIN') or hasRole('BOARD_HEARING_CREATE')"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.boardhearing.msgs.scheduleHearings">
<form:form commandName="scheduleHearingsForm" class="editForm">
	
	<%-- <span class="fieldGroup">
		<label for="scheduleAsGroup.false" class="fieldLabel">
			<fmt:message key="groupEditFalseLabel"/>
		</label>
		<form:radiobutton path="scheduleAsGroup" value="false" class="fieldValue"/>
		<label for="scheduleAsGroup.true" class="fieldLabel">
			<fmt:message key="groupEditTrueLabel"/>
		</label>
		<form:radiobutton path="scheduleAsGroup" value="true" class="fieldValue"/>
	</span>
	<c:set var="hidden" value="${scheduleHearingsForm.scheduleAsGroup eq 'true' ? '' : 'hidden'}" />
	<fieldset id="groupHearingFields" class="hearingFields ${hidden}">
		TODO
	</fieldset> --%>

<table id="selectViolationSummariesTable" class="listTable">
	<thead>
		<tr>
			<th class="operations"><%-- <input type="checkbox" id="selectAll" /> --%></th>
			<th><fmt:message key="offenderLabel"/></th>
			<th><fmt:message key="paroleEligibilityDateLabel"/></th>
		</tr>
	</thead>
	<tbody>
<c:forEach items="${eligibilitySummaryMap}" var="summary" >
	<c:forEach items="${scheduleHearingsForm.boardHearingItems}" var="item" varStatus="index">
		<c:if test="${item.paroleEligibility.id eq summary.key.paroleEligibilityId}">
			<tr class="eligibilityField">
				<fmt:formatDate value="${summary.key.hearingEligibilityDate}" var="hearingEligibilityDate" pattern="MM/dd/yyyy"/>
				<fmt:formatNumber value="${summary.key.offenderNumber}" pattern="####" var="offenderNumber" />
				<td>
					<form:checkbox path="boardHearingItems[${index.index}].selected" class="selectEligibility" />
				</td>
				<td>
					<fmt:message key="offenderNameLabel">
						<fmt:param value="${summary.key.lastName}" />
						<fmt:param value="${summary.key.firstName}" />
						<fmt:param value="${summary.key.middleName}" />
						<fmt:param value="${offenderNumber}" />
					</fmt:message>
				</td>
				<td>
					<c:out value="${hearingEligibilityDate}" />
				</td>
				<%-- <form:checkbox path="boardHearingItems[${index.index}].selected" class="selectEligibility" />
				<fmt:message key="eligibilityLabel">
					<fmt:param value="${summary.key.lastName}" />
					<fmt:param value="${summary.key.firstName}" />
					<fmt:param value="${summary.key.middleName}" />
					<fmt:param value="${offenderNumber}" />
					<fmt:param value="${hearingEligibilityDate}" />
				</fmt:message> --%>
				<form:input path="boardHearingItems[${index.index}].paroleEligibility" type="hidden" />
				<form:input path="boardHearingItems[${index.index}].boardHearing" type="hidden" />
				<c:set var="hidden" value="${scheduleHearingsForm.boardHearingItems[index.index].selected eq 'true' ? '' : 'hidden'}" />
				</tr><tr><td colspan="3"><fieldset class="hearingFields ${hidden}">
					<span class="fieldGroup">
						<form:label path="boardHearingItems[${index.index}].hearingDate" class="fieldLabel">
							<fmt:message key="hearingDate"/>
						</form:label>
						<form:input path="boardHearingItems[${index.index}].hearingDate" class="date"/>
						<form:errors path="boardHearingItems[${index.index}].hearingDate" cssClass="error"/>
					</span>
					<span class="fieldGroup">
						<form:label path="boardHearingItems[${index.index}].category" class="fieldLabel">
							<fmt:message key="categoryLabel"/>
						</form:label>
						<form:select path="boardHearingItems[${index.index}].category">
							<jsp:include page="../../../includes/nullOption.jsp"/>
							<c:forEach items="${summary.value}" var="cat">
								<option value="${cat.id}" ${cat.id == scheduleHearingsForm.boardHearingItems[index.index].category.id ? 'selected="selected"' : ''}>
									<c:out value="${cat.name}"/>
								</option>
							</c:forEach>
						</form:select>
						<form:errors path="boardHearingItems[${index.index}].category" cssClass="error"/>
					</span>
					<span class="fieldGroup">
						<form:label path="boardHearingItems[${index.index}].videoConference" class="fieldLabel">
							<fmt:message key="videoConference"/>
						</form:label>
						<form:checkbox path="boardHearingItems[${index.index}].videoConference" />
						<form:errors path="boardHearingItems[${index.index}].videoConference" cssClass="error"/>
					</span>
					
					<span id="boardMembers">
						<span class="fieldGroup">
							<form:label path="boardHearingItems[${index.index}].boardMember1" class="fieldLabel">
								<fmt:message key="boardMember1Label"/>
							</form:label>
							<form:select path="boardHearingItems[${index.index}].boardMember1">
								<jsp:include page="../../../includes/nullOption.jsp"/>
								<c:forEach items="${boardAttendees}" var="attendee">
									<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
									<option value="${attendee.boardMember.id}" ${attendee.boardMember == scheduleHearingsForm.boardHearingItems[index.index].boardMember1 ? 'selected="selected"' : ''}>
									<c:out value="${name.lastName}, ${name.firstName}"/>
								</option>
								</c:forEach>
							</form:select>
							<form:errors path="boardHearingItems[${index.index}].boardMember1" cssClass="error"/>
						</span>
						<span class="fieldGroup">
							<form:label path="boardHearingItems[${index.index}].boardMember2" class="fieldLabel">
								<fmt:message key="boardMember2Label"/>
							</form:label>
							<form:select path="boardHearingItems[${index.index}].boardMember2">
								<jsp:include page="../../../includes/nullOption.jsp"/>
								<c:forEach items="${boardAttendees}" var="attendee">
									<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
									<option value="${attendee.boardMember.id}" ${attendee.boardMember == scheduleHearingsForm.boardHearingItems[index.index].boardMember2 ? 'selected="selected"' : ''}>
									<c:out value="${name.lastName}, ${name.firstName}"/>
								</option>
								</c:forEach>
							</form:select>
							<form:errors path="boardHearingItems[${index.index}].boardMember2" cssClass="error"/>
						</span>
						<span class="fieldGroup">
							<form:label path="boardHearingItems[${index.index}].boardMember3" class="fieldLabel">
								<fmt:message key="boardMember3Label"/>
							</form:label>
							<form:select path="boardHearingItems[${index.index}].boardMember3">
								<jsp:include page="../../../includes/nullOption.jsp"/>
								<c:forEach items="${boardAttendees}" var="attendee">
									<c:set var="name" value="${attendee.boardMember.staffAssignment.staffMember.name}" />
									<option value="${attendee.boardMember.id}" ${attendee.boardMember == scheduleHearingsForm.boardHearingItems[index.index].boardMember3 ? 'selected="selected"' : ''}>
									<c:out value="${name.lastName}, ${name.firstName}"/>
								</option>
								</c:forEach>
							</form:select>
							<form:errors path="boardHearingItems[${index.index}].boardMember3" cssClass="error"/>
						</span>
					</span>
					<form:input path="boardHearingItems[${index.index}].boardHearingParticipant1" type="hidden" />
					<form:input path="boardHearingItems[${index.index}].boardHearingParticipant2" type="hidden" />
					<form:input path="boardHearingItems[${index.index}].boardHearingParticipant3" type="hidden" />
				</fieldset></td></tr>
		</c:if>
	</c:forEach>
</c:forEach>
</tbody>
</table>
	<c:if test="${editBoardHearing}">
		<p class="buttons">
			<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
		</p>
	</c:if>
</form:form>
</fmt:bundle>