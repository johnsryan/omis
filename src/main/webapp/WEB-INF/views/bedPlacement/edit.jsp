<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.bedplacement.msgs.bedPlacement">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderFormResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/common/style/jquery/ui/jquery.ptTimeSelect.css?VERSION=1"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/bedPlacement/style/bedPlacementSummary.css?VERSION=1"/>
	<script type="text/javascript">
		/* <![CDATA[ */
			//offender id for use with action menu display
			var offender = ${offender.id};
		/* ]]> */
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/3rdparty/JQuery/ui/jquery.ptTimeSelect.js?VERSION=1"> </script>
   	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/bedPlacement/scripts/jQuery/jquery.omis.bedPlacement.js?VERSION=1"> </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/bedPlacement/scripts/bedPlacement.js?VERSION=1"> </script>    
	<title>
		<c:choose>
			<c:when test="${not empty bedPlacement}">
				<fmt:message key="bedPlacementEditTitle">
				</fmt:message>
			</c:when>
			<c:otherwise>
				<fmt:message key="bedPlacementCreateTitle">
				</fmt:message>
			</c:otherwise>
		</c:choose>
		<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
	</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderHeader.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/bedPlacement/bedPlacementActionMenu.html?offender=${offender.id}"></a>
		<c:choose>
			<c:when test="${not empty bedPlacement}">
				<fmt:message key="bedPlacementEditTitle">
				</fmt:message>
			</c:when>
			<c:otherwise>
				<fmt:message key="bedPlacementCreateTitle">
				</fmt:message>
			</c:otherwise>
		</c:choose>
	</h1>
	<jsp:include page="includes/editForm.jsp"/>
</body>
</fmt:bundle>
</html>