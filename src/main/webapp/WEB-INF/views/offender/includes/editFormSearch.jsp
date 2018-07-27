<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.offender.msgs.offenderSearch" var="offenderSearchBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:setBundle basename="omis.person.msgs.name" var="nameBundle"/>
<fmt:setBundle basename="omis.demographics.msgs.demographics" var="demographicsBundle"/>
<fmt:setBundle basename="omis.offender.msgs.offender" var="offenderBundle"/>
<form:form commandName="offenderSearchForm" method="post" id="searchForm" class="editForm">
<!-- 	Commented out code use modules not yet in production. -->
	<fieldSet>
		<legend class="accentUltraLight"><fmt:message key="searchCriteria" bundle="${offenderSearchBundle}"/></legend>
		<span class="fieldGroup formErrors">
		<form:errors cssClass="error"/>
		</span>
		<div class="searching">
		<span class="fieldGroup">
			<label for="searchByDOCIdNumber" class="fieldLabel"><fmt:message key="offenderNumberLabel" bundle="${offenderBundle}"/></label>
			<input id="searchByDOCIdNumber" name="docIdNumber" value="${offenderSearchForm.docIdNumber}" class="fieldValue"/>					
		</span>
		<span class="fieldGroup">
			<label for="searchByLastName" class="fieldLabel"><fmt:message key="lastNameLabel" bundle="${nameBundle}"/></label>
			<input tabindex="0" id="searchByLastName" name="lastName" value="${offenderSearchForm.lastName}" class="fieldValue"/>			
		</span>
		<span class="fieldGroup">
			<label for="searchByFirstName" class="fieldLabel"><fmt:message key="firstNameLabel" bundle="${nameBundle}"/></label>
			<input id="searchByFirstName" name="firstName" value="${offenderSearchForm.firstName}" class="fieldValue"/>			
		</span>
		<span class="fieldGroup">
			<label for="searchByMiddleName" class="fieldLabel"><fmt:message key="middleNameLabel" bundle="${nameBundle}"/></label>
			<input id="searchByMiddleName" name="middleName" value="${offenderSearchForm.middleName}" class="fieldValue"/>			
		</span>
		<span class="fieldGroup">
			<label for="searchByGender" class="fieldLabel"><fmt:message key="searchByGenderLabel" bundle="${offenderSearchBundle}"/></label>
			<select name="sex">
		      <jsp:include page="../../includes/nullOption.jsp"/>
			      <c:forEach items="${sexes}" var="sex">	
			      	<c:choose>
			      		<c:when test="${sex eq offenderSearchForm.sex}">
			      			<option value="${sex.name}" selected="selected"><fmt:message key="sex${sex.name}Label" bundle="${demographicsBundle}"/></option>
			      		</c:when>
			      		<c:otherwise>
			      			<option value="${sex.name}"><fmt:message key="sex${sex.name}Label" bundle="${demographicsBundle}"/></option>
			      		</c:otherwise>
			      	</c:choose>			            			         
			      </c:forEach>
		      </select>
		</span>
		<%-- <span class="fieldGroup">
			<label for="searchByLocation" id="searchByLocation" class="fieldLabel"><fmt:message key="searchByLocationLabel" bundle="${offenderSearchBundle}"/></label>
			<select name="location">
				<jsp:include page="../../includes/nullOption.jsp"/>
					<c:forEach items="${locations}" var="location">
						<option value="${location.organization.id}"><c:out value="${location.organization.name}"/></option>					
					</c:forEach>
			</select>
		</span> --%>
		<span class="fieldGroup">
			<label for="searchByBirthDate" id="searchByBirthDate" class="fieldLabel"><fmt:message key="searchTypeBirthDateLabel" bundle="${offenderSearchBundle}"/></label>
			<input id="searchByBirthDate" name="birthDate" class="date" value="<fmt:formatDate value='${offenderSearchForm.birthDate}' pattern='MM/dd/yyyy'/>" class="fieldValue"/>
		</span>
		<span class="fieldGroup">
			<label for="searchBySocialSecurityNumber" id="searchBySocialSecurityNumber" class="fieldLabel"><fmt:message key="searchBySocialSecurityNumberLabel" bundle="${offenderSearchBundle}"/></label>
			<input id="searchBySocialSecurityNumber" name="socialSecurityNumber" value="${offenderSearchForm.socialSecurityNumber}" class="fieldValue"/>
		</span>		
		<%-- <span class="fieldGroup">
			<label for="searchByActiveRadio" class="fieldLabel"><fmt:message key="searchByActiveOffendersLabel" bundle="${offenderSearchBundle}"/></label>
			<input type="radio" id="searchByActiveRadioYes" name="searchActiveOffenders" value="${true}" checked="checked" class="fieldValue"/>
			<label for="searchByActiveRadio" class="fieldValueLabel"><fmt:message key="yesLabel" bundle="${commonBundle}"/></label>				
			<input type="radio" id="searchByActiveRadioNo" name="searchActiveOffenders" value="${false}" class="fieldValue"/>
			<label for="searchByActiveRadio" class="fieldValueLabel"><fmt:message key="noLabel" bundle="${commonBundle}"/></label>
			<form:errors cssClass="error" path="searchActiveOffenders"/>
		</span> --%>
		<%-- <span class="fieldGroup">
			<label for="displayOffenderPhotoRadio" class="fieldLabel"><fmt:message key="displayOffenderPhotoLabel" bundle="${offenderSearchBundle}"/></label>
			<input type="radio" id="displayOffenderPhotoRadioYes" name="displayOffenderPhoto" value="${true}" checked="checked" class="fieldValue"/>
			<label for="displayOffenderPhotoRadioYes" class="fieldValueLabel"><fmt:message key="yesLabel" bundle="${commonBundle}"/></label>
			<input type="radio" id="displayOffenderPhotoRadioNo" name="displayOffenderPhoto" value="${false}" class="fieldValue"/>
			<label for="displayOffenderPhotoRadioNo" class="fieldValueLabel"><fmt:message key="noLabel" bundle="${commonBundle}"/></label>
		</span>		 --%>
	<p class="buttons">
		<input type="submit" value="<fmt:message key='searchLabel' bundle='${commonBundle}'/>"/>
	</p>
	</div>
	</fieldSet>
		<h1>
			<a class="actionMenuItem" id="searchResultsActionMenu" href="${pageContext.request.contextPath}/offender/searchResultsActionMenu.html"></a>
			<fmt:message key="offenderSearchResultsTitle" bundle="${offenderSearchBundle}"/>
		</h1>				
		<jsp:include page="listTableBody.jsp"/>
</form:form>