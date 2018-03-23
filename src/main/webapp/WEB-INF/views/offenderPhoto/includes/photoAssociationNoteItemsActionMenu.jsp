<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (December 16, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offenderphoto.msgs.offenderPhoto" var="offenderPhotoBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<ul>
	<li>
		<a class="createLink" id="createOffenderPhotoAssociationNoteItemLink" href="${pageContext.request.contextPath}/offenderPhoto/createOffenderPhotoAssociationNoteItem.html?offenderPhotoAssociationNoteItemIndex=${offenderPhotoAssociationNoteItemIndex}"><span class="visibleLinkLabel"><fmt:message key="newOffenderPhotoAssociationNoteItemLabel" bundle='${offenderPhotoBundle}'/></span></a>
	</li>
</ul>