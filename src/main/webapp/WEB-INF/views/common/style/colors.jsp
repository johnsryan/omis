<%@ page contentType="text/css" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
/*
 * Application wide colors and borders where applicable.
 * Author: Stephen Abson, Ryan Johns, Joel Norris
 * Version: 0.1.3 (November 16, 2015)
 * Since: OMIS 3.0
 */
 
 
 <c:choose>
 	<c:when test="${not empty userAppearance}">
 		<c:choose>
	 		<c:when test="${not empty  userAppearance.foregroundColorValue}">
		 		<c:set var="foregroundHue" value="${userAppearance.foregroundColorValue.hue}"/>
		 		<c:set var="foregroundSaturation" value="${userAppearance.foregroundColorValue.saturation}"/>
	 		</c:when>
	 		<c:otherwise>
	 			<c:set var="foregroundHue" value="0"/>
 				<c:set var="foregroundSaturation" value="0"/>
	 		</c:otherwise>
 		</c:choose>
 		<c:choose>
	 		<c:when test="${not empty  userAppearance.backgroundColorValue}">
		 		<c:set var="backgroundHue" value="${userAppearance.backgroundColorValue.hue}"/>
 				<c:set var="backgroundSaturation" value="${userAppearance.backgroundColorValue.saturation}"/>
	 		</c:when>
	 		<c:otherwise>
	 			<c:set var="backgroundHue" value="0"/>
		 		<c:set var="backgroundSaturation" value="0"/>
		 		<c:set var="backgroundLightness" value="100"/>
	 		</c:otherwise>
 		</c:choose>
 		<c:choose>
 			<c:when test="${not empty userAppearance.whiteBackground and not userAppearance.whiteBackground}">
 				<c:set var="backgroundLightness" value="90"/>
 			</c:when>
 			<c:otherwise>
 				<c:set var="backgroundLightness" value="100"/>
 			</c:otherwise>
 		</c:choose>
 		<c:choose>
	 		<c:when test="${not empty  userAppearance.accentColorValue}">
	 			<c:set var="accentHue" value="${userAppearance.accentColorValue.hue}"/>
 				<c:set var="accentSaturation" value="${userAppearance.accentColorValue.saturation}"/>
	 		</c:when>
	 		<c:otherwise>
	 			<c:set var="accentHue" value="200"/>
 				<c:set var="accentSaturation" value="100"/>
	 		</c:otherwise>
	 	</c:choose>
 	</c:when>
 	<c:otherwise>
 		<c:set var="foregroundHue" value="0"/>
 		<c:set var="foregroundSaturation" value="0"/>
 		<c:set var="backgroundHue" value="0"/>
 		<c:set var="backgroundSaturation" value="0"/>
 		<c:set var="backgroundLightness" value="100"/>
 		<c:set var="accentHue" value="200"/>
 		<c:set var="accentSaturation" value="100"/>
 	</c:otherwise>
 </c:choose>
 
 /* Root variable declaration */
 
:root {
 	--backgroundUltraDark: hsla(${backgroundHue}, ${backgroundSaturation}%, 10%, 1);
	--backgroundDark: hsla(${backgroundHue}, ${backgroundSaturation}%, 20%, 1);
	--backgroundRegular: hsla(${backgroundHue}, ${backgroundSaturation}%, 60%, 1);
	--backgroundLight: hsla(${backgroundHue}, ${backgroundSaturation}%, 80%, 1);
	--backgroundUltraLight: hsla(${backgroundHue}, ${backgroundSaturation}%, 90%, 1);
}
 
/* Backgrounds */
body {
	background: hsla(${backgroundHue}, ${backgroundSaturation}%, ${backgroundLightness}%, 1);
}


/* Fonts. */
a {
	color: #483D8B;
}
a:active {
	color: #FF1493;
}

body.containerPage {
	background: hsla(0, 0%, 95%, 1);
}

body.containerPage .tab_content {
}

#offenderTabs_tabLinks.tabs li.tab {
	-webkit-appearance: none;
}

#offenderTabs_tabLinks.tabs li.tab_li.active {
	background: hsla(0, 0%, 100%, 1);
}
#offenderTabs_tabLinks.tabs li.tab_li.active:before {
  box-shadow: -4px 0 10px -8px inset;
  content: " ";
  height: 100%;
  left: -11px;
  position: absolute;
  top: 0;
  width: 11px;
}

#offenderTabs_tabLinks.tabs li.tab_li.active:after {
  box-shadow: 4px 0 10px -8px inset;
  content: " ";
  height: 100%;
  right: -11px;
  position: absolute;
  top: 0;
  width: 11px;
}

/* Header */
div.header, div.menu {
  border-top-color: #666666;
  border-left-color: #666666;
  border-bottom-color: #000000;
  border-right-color: #666666;
}

div.menu {
	color: #000;
}

/* Application toolbar. */

#applicationToolbar {
  background-color: #bbbbbb;
  border-color: grey;
}

/* Headers */
ul.toolbar, div.sectionHeader, ul.taskLinks li.header, ul.linkHeader,
ul.profileItemToolbar, h2.actionMenuHeader {
	background-color: #99CCFF;
}

#leftPanel, #rightPanel {
  background-color: #000;
}

/* Forms and list table headers. */
table.listTable th, .profileItemHeader, form.editForm table.editTable thead tr th,
form.editForm table.editTable th, table.formTable thead tr th,
table.formTable thead tr{
	background-color: hsla(${accentHue}, ${accentSaturation}%, 40%, 1);
	color: #FFFFFF;
	border-color: #000000;
}

table.listTable caption {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 35%, 1);
	color: #FFFFFF;
	border-color: #000000;
}

.profileItemHeader {
	border-color: black;
}

.profileItemHeader a {
	color: #FFFFFF;
}

/* Forms and list table headers. */
ul.taskLinks, form.editForm {

}

/* Subheaders. */
table.listTable th.subheader {
	background-color: #DEEFFE;
	color: #000000;
}

/* No content. */
table.listTable td.noContent {
	background-color: rgb(245, 245, 245);
}

/* Toolbar. */
ul.toolbar, ul.taskLinks {
	border-color: #666666;
}
ul.toolbar li a {
	border-color: transparent;
}

/* Highlight row over which cursor is positioned and color odd/even rows. */

table.listTable tbody tr:nth-child(odd) td {
	background-color: #FFFFFF;
}

table.listTable tbody tr:nth-child(even) td {
	background-color: #F5F5F5;
}

table.listTable tbody tr:not(.listTableRowNoHighlight):hover td, table.listTable tbody tr.activeRecord:hover td {
	background-color: #E6E6FA;
}

/* Tabs. */
.tabbed_box h4 {
	color: rgb(255, 255, 255);
}
.tabbed_box h4 small {
	color: rgb(227, 233, 236);
}
ul.tabs li span {
	
	color: #F6F6F6;
	border-color: #000000;
}
ul.tabs li span:hover {
	border-color: rgb(47, 52, 58);
}
ul.tabs li span a {
	
}
ul.tabs li span a:hover {
	border-color: rgb(47, 52, 58);
}

.tab_content {
	border-top-color: #000000;
	border-left-color:   hsla(${accentHue}, ${accentSaturation}%, 50%, 1);
}

.left_arrow:after, .right_arrow:after, .top_arrow:after, bottom_arrow:after {
	background-color: #dcdcdc;
}

ul.tabs li.tab_li:nth-child(n+2) {
	border-left-color: #757575;
}

.content {
	border-top-color: rgb(70, 76, 84);
}
.content ul li {
	border-bottom-color: rgb(214, 221, 224);
}
.content ul li:last-child {
	border-bottom-color: currentColor;
}
.content ul li span a {
	color: #000;
}
.content ul li span a small {
	color: rgb(139, 149, 156);
}
.content ul li span a:hover {
	color: rgb(165, 156, 131);
}
.content ul li span a:hover small {
	color: rgb(186, 174, 142);
}

/* Input controls */

input:focus,
input[type="text"]:focus,
input[type="password"]:focus, 
textarea:focus, 
select:focus{
	border-color: hsla(${accentHue}, ${accentSaturation}%, 50%, 1);
	box-shadow: 0 0 5px hsla(${accentHue}, ${accentSaturation}%, 50%, 1);
	outline: none;
}

form.editForm select, form select {
    -webkit-appearance: none;
	-moz-appearance: none;
    background: url('../images/selectArrow.png') no-repeat scroll right center #FFF;
} 

form.editform select:disabled, form select:disabled {
    background: url('../images/selectArrow.png') no-repeat scroll right center rgba(239, 239, 239, 1);
}

input[type="text"],
input[type="password"], 
textarea, 
select{
	-webkit-appearance: none;
	border-style: solid;
	border-width: 1px;
	border-color: hsla(0, 0%, 0%, 1);
}

/* Forms. */
form.editForm {
	border-width: 1px;
	border-style: solid;
	border-color: #333;
}

form.editForm fieldset {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 91%, 1);
	border-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 91%, 1);
}

form.editForm fieldset legend {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 80%, 1);
}

p.buttons > button, a.button, p.buttons > input[type="submit"], form input[type="submit"], form input[type="reset"] {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 100%, 1);
	border: 1px solid #000;
}

p.buttons > button:hover, a.button:hover, 
p.buttons > input[type="submit"]:hover, 
form input[type="submit"]:hover, 
form input[type="reset"]:hover {
	background-color:hsla(${foregroundHue}, ${foregroundSaturation}%, 84%, 1);
	color:#FFFFFF;
	transition: background-color .2s, color .2s;
}

form.editForm input[disabled="disabled"], 
form.editForm input:disabled, 
form.editForm textarea[disabled="disabled"], 
form.editForm textarea:disabled,
form.editForm textarea[disabled=""],
form.editForm textarea[disabled], 
form.editForm input[readonly="readonly"], 
form.editForm input:read-only, 
form.editForm textarea[readonly="readonly"], 
form.editForm textarea:read-only, 
form.editForm select[readonly="readonly"], 
form.editForm.select:read-only,
[disabled],[disabled="disabled"],[disabled=""], [disabled=disabled], :disabled {
	background-color: rgba(239, 239, 239, 1);
}



/* Form errors. */
form.editForm span.error, form.editForm span.groupError, form.searchForm span.error {
  color: #D8000C;
}

/* Form labels and values. */
form.editForm .formLabel {
  color: hsla(0, 0, 0%, 1);
}

form.editForm .restricted {
	color: red;
	background-color: #F6CECE;
}

form.editForm .unavailable {
	background-color: #CEF6F5;
}

/* Index/home/container page. */
ul.applicationToolbar {
	background-color: transparent;
}
ul.applicationToolbar li a {
	border-color: transparent;
}

h1 {
	color: hsla(${foregroundHue}, ${foregroundSaturation}%, 0%, 1);
	border-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 0%, 1);
}

ul.tabs li span.active, ul.tabs li span.active a {
	border-color: rgb(70, 76, 84);
	background-color: rgb(255,255,255);
} 

ul.content li {
	border-color: transparent; 
}

ul.ajaxList li {
	border-color: #DDDDDD; 
}

ul.tabs li span a, span.tab a {
	color:#000;
}

/* Module Group */

.moduleGroupLinkContainer > a {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 40%, 1);
	color: white;
}

/* Panels */
.horizontalPanel {
		background-color: transparent;
}

.panelContent {
	background-color: #FFFFFF;
}

/* Informational or suggested input */

.defaultDisplayText {	
	color: #909090;
}

#launchMenu {
	background-repeat: no-repeat;
	background-position:center; 
}

#navigationMenu {
	background-color: lightgreen;
}

.mapItem {
	background-color:#DDD;
	opacity: 0.4;
}
.profileItem.active {
	background-color:  hsla(${backgroundHue}, ${backgroundSaturation}%, 90%, 1);
}

.active {
	background-color: #FFF;
}

/* Tabbed index page. */
body.containerPage {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 99%, 1);
    background-size: 100% 100%;
}

/* Tab content. */
.content {
	background-color: hsla(0, 0%, );
}

#homeTab.active {
	background-color:#F9BF32;
}

#homeTab {
	background-color: #F9CF42;
}

#homeTab a {
	color:#FFF;

}

/* Read only fields. */
form.editForm [readonly="readonly"] {
	background-color: #EEEEEE;
	color: #111111;
}

#content, #contentFrame {
	background-color: transparent;
}

/* Modal dialog. */

#modalArea {
	background-color: transparent;
}

#modalLayer {
	background-color: grey;
	opacity: 0.5;
}

#modalMessage {
	background-color: white;
	border-color: black;
}

/* Warning message. */

.warningMessage {
  color: darkred;
  background-color: lightgray;
}

/* Tables. */

tr.disabled {
  background-color: rgba(239, 239, 239, 1);
}

/* Identifying information. */

.identifyingInformation {
	border-color: black;
}

.identifyingInformation, .identifyingInformation a {
	color: #000000;
}


/* Action Menus */
a.actionMenuItem:hover, .homeLink:hover, .reportHomeLink:hover, .applicationHelpLink:hover, .identifyingInformation a:hover, a.profileLink:hover, a.moduleLink:hover {
	background-color: #fafafa;
	transition: background-color .25s;
	border: 1px solid #aeaeae;
}

a.actionMenuItem.open {
	background-color: #ffffff;
	border-style: solid;
	border-color: #000;
}

.actionListContainer.show {
	background: #ffffff;
	transition: background 1s;
	border-style: solid;
	border-color: #000;
}

a.actionMenuItem:hover {
		
}

.actionListContainer {
	background-color: #FFF;
}

/* Panels */

div.panel {
	border-color: grey;
}

/* Foreground Colors */

.foregroundUltraDark {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 10%, 1);
	color: white;
}

.foregroundDark {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 20%, 1);
	color: white;
}

.foregroundRegular {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 60%, 1);
	color: black;
}

.foregroundLight {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 80%, 1);
	color: black;
}

.foregroundUltraLight {
	background-color: hsla(${foregroundHue}, ${foregroundSaturation}%, 90%, 1);
	color: black;
}

/* Accents Colors*/

.accentUltraDark {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 10%, 1);
	color: white;
}

.accentDark {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 40%, 1);
	color: white;
}

.accentRegular {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 60%, 1);
	color: black;
}

.accentLight {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 80%, 1);
	color: black;
}

.accentUltraLight {
	background-color: hsla(${accentHue}, ${accentSaturation}%, 90%, 1);
	color: black;
}

/* Backgrounds Colors*/

.backgroundUltraDark {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 10%, 1);
	color: white;
}

.backgroundDark {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 20%, 1);
	color: white;
}

.backgroundRegular {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 60%, 1);
	color: black;
}

.backgroundLight {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 80%, 1);
	color: black;
}

.backgroundUltraLight {
	background-color: hsla(${backgroundHue}, ${backgroundSaturation}%, 90%, 1);
	color: black;
}

.profileItem a:hover, .moduleLink > a:hover {
   border: 1px solid #000;
   
}

/* Hoverable Colors. */

.hoverable:hover, .profileItem a:hover, .moduleLink > a:hover {
	box-shadow: inset 0 0 15px 15px  hsla(0, 100%, 100%, .33);
}

.hoverable:hover:after, .profileItem a:hover:after, .moduleLink > a:hover:after {
	box-shadow: inset 0 0 15px 15px  hsla(0, 100%, 100%, .33);
}

.active {
	color: white;
<%-- 	text-shadow: 0px 0px 3px black, 0 0 10px hsla(${accentHue}, 100%, 50%, 1), 0 0 5px hsla(${accentHue}, 100%, 85%, 1); --%>
}

span.banner {
	background-color:  hsla(${backgroundHue}, ${backgroundSaturation}%, 85%, 1);
}

span.banner:hover {
	background-color:  hsla(${backgroundHue}, ${backgroundSaturation}%, 95%, 1);
}

span.banner:hover::before {
background:
  linear-gradient(45deg,  hsla(${backgroundHue}, ${backgroundSaturation}%, 95%, 1) 33.333%, transparent 33.333%, transparent 66.667%,  hsla(${backgroundHue}, ${backgroundSaturation}%, 95%, 1) 66.667%), linear-gradient(135deg,  hsla(${backgroundHue}, ${backgroundSaturation}%, 95%, 1) 33.333%, transparent 33.333%, transparent 66.667%, hsla(${backgroundHue}, ${backgroundSaturation}%, 95%, 1) 66.667%);
}

span.banner:before {
background:
  linear-gradient(45deg,  hsla(${backgroundHue}, ${backgroundSaturation}%, 85%, 1) 33.333%, transparent 33.333%, transparent 66.667%,  hsla(${backgroundHue}, ${backgroundSaturation}%, 85%, 1) 66.667%), linear-gradient(135deg,  hsla(${backgroundHue}, ${backgroundSaturation}%, 85%, 1) 33.333%, transparent 33.333%, transparent 66.667%, hsla(${backgroundHue}, ${backgroundSaturation}%, 85%, 1) 66.667%);
}
