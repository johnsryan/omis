/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
function userAttendanceItemsCreateOnClick() {
	$("#createUserAttendanceItemLink").click(function() {
		$.ajax(config.ServerConfig.getContextPath() + "/hearing/resolution/createUserAttendanceItem.html",
		   {
				type: "GET",
				async: false,
				data: {userAttendanceItemIndex: currentUserAttendanceItemIndex},
				success: function(data) {
					$("#userAttendanceTableBody").append(data);
					userAttendanceItemRowOnClick(currentUserAttendanceItemIndex);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("Error - status: " + textStatus + "; error: "
						+ errorThrown);
					$("#userAttendanceTableBody").html(jqXHR.responseText );
				}
			});
		currentUserAttendanceItemIndex++;
		return false;
	});
};

function userAttendanceItemRowOnClick(userAttendanceItemIndex) {
	applyUserIDSearch(document.getElementById("userAttendanceInput" + userAttendanceItemIndex),
			document.getElementById("userAttendanceItems[" + userAttendanceItemIndex + "].userAccount"),
			document.getElementById("userAttendanceDisplay" + userAttendanceItemIndex),
			document.getElementById("clearStaffAttendance" + userAttendanceItemIndex));
	$("#removeUserAttendanceLink" + userAttendanceItemIndex).click(function() {
		if ($("#userAttendanceOperation" + userAttendanceItemIndex).val() == "UPDATE") {
			$("#userAttendanceOperation" + userAttendanceItemIndex).val("REMOVE");
			$("#userAttendanceItemRow" + userAttendanceItemIndex).addClass("removeRow");
		} else if($("#userAttendanceOperation" + userAttendanceItemIndex).val() == "REMOVE") {
			$("#userAttendanceOperation" + userAttendanceItemIndex).val("UPDATE");
			$("#userAttendanceItemRow" + userAttendanceItemIndex).removeClass("removeRow");
		} else {
			$("#userAttendanceItemRow" + userAttendanceItemIndex).remove();
		}
		return false;
	});
};