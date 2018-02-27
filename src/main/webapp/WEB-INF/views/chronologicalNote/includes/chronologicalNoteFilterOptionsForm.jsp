<%--
  - Chronological note filter options form.
  -
  - Author: Yidong Li
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
<form:form commandName = "chronologicalNoteFilterOptionsForm" id="chronologicalNoteFilterOptionsForm" method="POST" class="listForm">
	<fieldset class="foregroundUltraLight">
			<legend class="chronologicalNote foregroundLight"><fmt:message key="chronologicalNoteFilterOptionsLabel"/></legend>
		<span class="categoryListContainer">
			<form:checkboxes items="${categories}" path="categories" itemLabel="name" itemValue="id"/>
			<form:errors path="categories" cssClass="error"/>
		</span>
		<p class="buttons">
			<input type="submit" value="<fmt:message key="RefreshLabel"/>"/>
		</p>
	</fieldset>
</form:form>
</fmt:bundle>