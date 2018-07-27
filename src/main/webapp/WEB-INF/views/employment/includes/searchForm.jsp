<%--
  - Employer search form.
  -
  - Author: Yidong Li
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:bundle basename="omis.employment.msgs.employment">
<form:form commandName="employerSearchForm"  class="searchForm" id="searchForm" method="POST">
	<fieldset>
		<p>
			<label><fmt:message key="searchEmployerByLabel"/></label>
		</p>
		<p>
			<form:label path="employerName" class="fieldLabel">
				<fmt:message key="employerNameLabel"/>
			</form:label>
			<form:input path="employerName" class="medium"/>
			<form:errors path="employerName" cssClass="error"/>
		</p>
		<p class="buttons">
			<input type="submit" value="<fmt:message key="searchEmployersLabel"/>"/>
		</p>
	</fieldset>
</form:form>
</fmt:bundle>