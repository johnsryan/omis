<%--
  - Chronological note filter options form.
  -
  - Author: Yidong Li
  - Author: Joel Norris
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:bundle basename="omis.chronologicalnote.msgs.chronologicalNote">
<form:form commandName = "chronologicalNoteFilterOptionsForm" id="chronologicalNoteFilterOptionsForm" method="POST" class="listForm">
	<fieldset class="foregroundUltraLight">
		<legend class="chronologicalNote foregroundLight"><fmt:message key="chronologicalNoteFilterOptionsLabel"/><a href="#" id="groupsVisibilityLink" class="groupsVisibilityLink expandLink"></a></legend>
		<c:forEach items="${groups}" var="group" varStatus="groupStatus">
			<c:set value="${groupCategoryMap[group.name]}" var="categories" scope="page"/>
			<c:set value="groupCategoryContainer hidden" var="groupCategoryContainerDisplayClass"/>
			<c:set value="groupVisibilityLink expandLink" var="groupVisibilityLinkDisplayClass"/>
			<c:forEach items="${categories}" var="cat" varStatus="status">
				<c:forEach items="${chronologicalNoteFilterOptionsForm.categories}" var="formCat">
					<c:if test="${cat eq formCat}">
						<c:set value="groupCategoryContainer" var="groupCategoryContainerDisplayClass"/>
						<c:set value="groupVisibilityLink collapseLink" var="groupVisibilityLinkDisplayClass"/>
					</c:if>
				</c:forEach>
			</c:forEach>
			<h2><c:out value="${group.name}"/><a href="javascript:;" id="groupVisibilityLink${groupStatus.index}" class="${groupVisibilityLinkDisplayClass}"></a></h2>
			<span class="${groupCategoryContainerDisplayClass}" id="groupCategoryContainer${groupStatus.index}">
			<c:forEach items="${categories}" var="category" varStatus="status">
				<span class="categoryContainer" id="categoryContainer${status.index}">
				<c:choose>
					<c:when test="${chronologicalNoteFilterOptionsForm.categories.contains(category)}">
						<label>
							<input type="checkbox" class="categoryItemCheckBox" name="categories" id="categoryItemCheckBox${status.index}" value="${category.id}" checked="checked"/>
							<fmt:message key="categoryCheckBoxLabel">
								<fmt:param value="${category.name}"/>
							</fmt:message>
						</label>
					</c:when>
					<c:otherwise>
						<label>
							<input type="checkbox" class="categoryItemCheckBox" name="categories" id="categoryItemCheckBox${status.index}" value="${category.id}"/>
							<fmt:message key="categoryCheckBoxLabel">
								<fmt:param value="${category.name}"/>
							</fmt:message>
						</label>
					</c:otherwise>
				</c:choose>
				</span>
			</c:forEach>
			<form:errors path="categories" cssClass="error"/>
			</span>
		</c:forEach>
		<p class="buttons">
			<input type="submit" value="<fmt:message key="RefreshLabel"/>"/>
		</p>
	</fieldset>
</form:form>
</fmt:bundle>