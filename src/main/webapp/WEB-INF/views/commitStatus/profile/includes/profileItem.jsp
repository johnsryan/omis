<%--
  - Profile item for commit status term.
  -
  - Author: Yidong Li
  --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.commitstatus.msgs.commitStatus">
  	<div class="profileItem">
  			<a href="${pageContext.request.contextPath}/commitStatus/list.html?offender=${offenderSummary.id}">
  				<span>
					<fmt:message key="commitStatusTermCountLabel">
						<fmt:param value="${commitStatusTermCount}"/>
					</fmt:message>
    			</span>
    		</a>
   	</div>
</fmt:bundle>