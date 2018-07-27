<%@ page contentType="text/css" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="foregroundOpacity" value="${userAppearance.foregroundColorValue.opacity}"/>
<c:set var="borderRadius" value="${userAppearance.borderRadius}"/>
<c:choose>
	<c:when test="${userAppearance.shadows}">
		<c:set var="boxShadowValue" value="5px 5px 5px hsla(0, 0%, 0%, ${foregroundOpacity})"/>
	</c:when>
	<c:otherwise>
		<c:set var="boxShadowValue" value="none"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${not empty userAppearance.backgroundPhoto }">
		<c:set var="backgroundImageURL" value="${pageContext.request.contextPath}/userPreference/displayPhoto.html?photo=${userAppearance.backgroundPhoto.id}&contentType=image/jpeg"/>
	</c:when>
	<c:otherwise>
		<c:set var="backgroundImageURL" value=""/>
	</c:otherwise>
</c:choose>
body {
	background-image: url('${backgroundImageURL}');
	background-attachment: fixed;
}

body h1 {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}

body > table {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}

form fieldset {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}

body navBar a.navBarItem {
	box-shadow: ${boxShadowValue};
}

<%-- Profile Screen --%>
div.offenderProfile div.profileItems h2{
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}
div.offenderProfile div.profileItems div.profileItemsWrapper div.profileItemsInnerWrapper {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}

<%-- Offender Header --%>
div#offenderHeader div#offenderHeaderDetails div.offenderHeaderContainer {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}
div#offenderNavHeader div#fullNavBarWrapper div#navBarWrapper div#navBar a.navBarItem {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}
div#offenderNavHeader div#navContentWrapper.navContentWrapper div.navContent {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}

<%--link container for list screens --%>
span.linkContainer {
	box-shadow: ${boxShadowValue};
	border-radius: ${borderRadius}px;
}