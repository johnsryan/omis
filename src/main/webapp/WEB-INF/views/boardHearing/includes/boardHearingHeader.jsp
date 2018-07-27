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
  - Board hearing header.
  -
  - Author: Annie Wahl
  - Author: Josh Divine
  - Version: 0.1.3 (July 17, 2018)
  - Since: OMIS 3.0
  --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.boardhearing.msgs.boardHearing">
<c:if test="${not empty boardHearingSummary}">
<div id="boardHearingHeader">
	<fieldset class="foregroundUltraLight">
		<legend class="foregroundLight"><fmt:message key="hearingDetailsTitle" /></legend>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="dateLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${boardHearingSummary.hearingDate}" pattern="MM/dd/yyyy" />
			</span>
		</span>
		<c:if test="${not empty boardHearingSummary.appearanceCategoryName}">
			<span class="fieldGroup">
				<label class="fieldLabel detailsLabel">
					<fmt:message key="appearanceTypeLabel"/>
				</label>
				<span class="detail">
					<c:out value="${boardHearingSummary.appearanceCategoryName}"/>
				</span>
			</span>
		</c:if>
	</fieldset>
</div>
</c:if>
</fmt:bundle>