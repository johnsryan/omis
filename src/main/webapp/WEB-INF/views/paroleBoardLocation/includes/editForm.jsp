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
 - Author: Josh Divine
 - Version: 0.1.0 (Feb 21, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.paroleboardlocation.msgs.paroleBoardLocation">
<form:form commandName="paroleBoardLocationForm" class="editForm">
<fieldset>
	<legend><fmt:message key="paroleBoardLocationDetailsTitle"/></legend>
	<span class="fieldGroup">
		<form:label path="location" class="fieldLabel">
			<fmt:message key="locationLabel"/></form:label>
		<form:select path="location">
			<form:option value=""><fmt:message key="nullLabel" bundle="${commonBundle}"/></form:option>
			<form:options items="${locations}" itemLabel="organization.name" itemValue="id"/>
		</form:select>
		<form:errors path="location" cssClass="error"/>
	</span>
	<span class="fieldGroup">
		<form:label path="videoConferenceCapable" class="fieldLabel">
			<fmt:message key="videoConferenceCapableLabel"/></form:label>
		<form:checkbox path="videoConferenceCapable" /> 
		<form:errors path="videoConferenceCapable" cssClass="error"/>
	</span>
</fieldset>
<p class="buttons">
	<input type="submit" value="<fmt:message key='saveLabel' bundle='${commonBundle}'/>"/>
</p>
</form:form>
</fmt:bundle>