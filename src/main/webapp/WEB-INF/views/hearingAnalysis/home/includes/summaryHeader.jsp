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
 - Version: 0.1.0 (Jan 17, 2018)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearinganalysis.msgs.hearingAnalysisHome">
<div id="requestHeader">
	<fieldset>
		<legend><fmt:message key="hearingAnalysisDetailsTitle"/></legend>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="hearingEligibilityDateLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${hearingAnalysisSummary.hearingEligibleDate}" pattern="MM/dd/yyyy"/>
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="nextReviewDateLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${hearingAnalysisSummary.reviewDate}" pattern="MM/dd/yyyy" />
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="analystLabel"/>
			</label>
			<c:out value="${hearingAnalysisSummary.analystLastName}, ${hearingAnalysisSummary.analystFirstName} ${hearingAnalysisSummary.analystMiddleName} (${hearingAnalysisSummary.analystTitleName})"/>
		</span>
	</fieldset>
</div>
</fmt:bundle>
