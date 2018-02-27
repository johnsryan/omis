<%--
 - Author: Stephen Abson
 - Author: Annie Jacques
 - Version: 0.1.1 (Sep 18, 2017)
 - Since: OMIS 3.0
 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.demographics.msgs.demographics" var="demographicsBundle"/>
<fmt:bundle basename="omis.offender.msgs.offenderHeader">
<div id="offenderHeader">
	<jsp:include page="offenderHeaderSummaryContent.jsp"/>
</div>
<div id="offenderNavHeader">
	<jsp:include page="offenderHeaderNavContent.jsp"/>
</div>
</fmt:bundle>