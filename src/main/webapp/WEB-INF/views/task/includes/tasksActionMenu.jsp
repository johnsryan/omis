<%--
  - Action menu for tasks.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.task.msgs.task" var="taskBundle"/>
<ul>
	<sec:authorize access="hasRole('ADMIN') or hasRole('TASK_INVOKE')">
		<li>
			<a class="invokeLink" href="${pageContext.request.contextPath}/task/invokeAndRedirect.html?task=${task.id}" title="<fmt:message key='invokeTaskLabel' bundle='${taskBundle}'/>"><fmt:message key="invokeTaskLabel" bundle="${taskBundle}"/></a>
		</li>
	</sec:authorize>
</ul>