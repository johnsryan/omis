<?xml version="1.0" encoding="UTF-8"?>
<%-- Author: Joel Norris
 - Version: 0.1.0 (April 3, 2018)
 - Since: OMIS 3.0 --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<div class="profileItem">
		<a href="${pageContext.request.contextPath}/warrant/list.html?offender=${offenderSummary.id}">
    		<span>
    			<fmt:message key="warrantCountLabel">
    				<fmt:param value="${warrantCount}"/>
    			</fmt:message>
    		</span>
    	</a>
    </div>
</fmt:bundle>