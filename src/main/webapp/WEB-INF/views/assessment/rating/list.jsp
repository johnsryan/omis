<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.assessment.msgs.assessmentRating">
<head>
	<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/headerListResources.jsp"/>
	<jsp:include page="/WEB-INF/views/common/includes/toolsResources.jsp"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assessment/style/assessmentRating.css?VERSION=1" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assessment/scripts/assessmentRatings.js?VERSION=1"> </script>
	<script type="text/javascript">
		var graphData = [
			<c:forEach var="section" items="${graphData}" varStatus="status">
				{
					section: "${section.key.ratingCategory.description}",
					score: ${section.key.score},
					ratings:
						[
						<c:forEach var="rating" items="${section.value}" varStatus="status2">
							{
								rating: "${rating.rank.name}",
								min: ${rating.min},
								max: ${rating.max}
							},
						</c:forEach>
						]
				},
			</c:forEach>
			];
	</script>
	<title>
		<fmt:message key="assessmentRatingListHeader"/>
	</title>
</head>
 <body>
 	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/assessment/rating/assessmentRatingsActionMenu.html?administeredQuestionnaire=${administeredQuestionnaire.id}"></a>
		<fmt:message key="assessmentRatingListHeader"/>
	</h1>
	<jsp:include page="includes/assessmentRatingListTable.jsp"/>
	<h1>
		<a class="actionMenuItem" id="actionMenuLink" href="${pageContext.request.contextPath}/assessment/rating/assessmentCategoryRatingsActionMenu.html?administeredQuestionnaire=${administeredQuestionnaire.id}"></a>
		<fmt:message key="assessmentCategoryRatingListHeader"/>
	</h1>
	<jsp:include page="includes/assessmentCategoryRatingListTable.jsp"/>
</body>
</fmt:bundle>
</html>