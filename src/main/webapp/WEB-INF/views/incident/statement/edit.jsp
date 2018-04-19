<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<fmt:bundle basename="omis.incident.msgs.incident">
		<head>
			<jsp:include page="/WEB-INF/views/common/includes/headerMetas.jsp"/>
			<jsp:include page="/WEB-INF/views/common/includes/headerGeneralResources.jsp"/>
			<jsp:include page="/WEB-INF/views/common/includes/headerToolbarResources.jsp"/>
			<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
			<jsp:include page="/WEB-INF/views/common/includes/formResources.jsp"/>
			<jsp:include page="/WEB-INF/views/common/includes/interactiveImageResources.jsp"/>
			<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/jquery/ui/jquery.ptTimeSelect.css"/>
			<script type="text/javascript" src="${pageContext.request.contextPath}/resources/3rdparty/JQuery/ui/jquery.ptTimeSelect.js"> </script>
			<jsp:include page="/WEB-INF/views/common/includes/searchResources.jsp"/>
			<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/incident/statement/style/incidentStatement.css?VERSION=1"/>
			<script type="text/javascript" src="${pageContext.request.contextPath}/resources/incident/statement/scripts/includes/jquery.omis.incidentStatement.js?VERSION=1"> </script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/resources/incident/statement/scripts/incidentStatement.js?VERSION=1"> </script>
			<script type="text/javascript">
				/* <![CDATA[ */
					//Incident report note index used to track incident report note items on the form 
					var currentIncidentStatementNoteItemIndex = ${incidentStatementNoteItemIndex};
					var currentInvolvedPartyItemIndex = ${involvedPartyItemIndex};
				/* ]]> */
			</script>
			<title>
				<c:choose>
					<c:when test="${not empty incidentStatement}">
						<fmt:message key="incidentStatementEditTitle"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="incidentStatementCreateTitle"/>
					</c:otherwise>
				</c:choose>
			</title>
		</head>
		<body>
			<h1>
				<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/incident/statement/incidentStatementActionMenu.html"></a>
				<c:choose>
					<c:when test="${not empty incidentStatement}">
						<fmt:message key="incidentStatementEditTitle"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="incidentStatementCreateTitle"/>
					</c:otherwise>
				</c:choose>
			</h1>
			<jsp:include page="includes/editForm.jsp"/>
		</body>
	</fmt:bundle>
</html>