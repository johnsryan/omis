<%--
 - Table for STG affiliations.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="omis.region.msgs.region" var="regionBundle"/>
<fmt:setBundle basename="omis.region.msgs.state" var="stateBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.stg.msgs.stg">
<table class="listTable">
	<thead>
		<tr>
			<th class="operations"/>
			<th><fmt:message key="securityThreatGroupLabel"/></th>
			<th><fmt:message key="stgChapterLabel"/></th>
			<th><fmt:message key="stgMonikerLabel"/></th>
			<th><fmt:message key="stgRankLabel"/></th>
			<th><fmt:message key="cityLabel" bundle="${regionBundle}"/></th>
			<th><fmt:message key="stateLabel" bundle="${stateBundle}"/></th>
			<th class="date"><fmt:message key="startDateLabel" bundle="${commonBundle}"/></th>
			<th class="date"><fmt:message key="endDateLabel" bundle="${commonBundle}"/></th>
		</tr>
	</thead>
	<tbody id="affiliations">
		<jsp:include page="listTableBodyContent.jsp"/>
	</tbody>
</table>
</fmt:bundle>