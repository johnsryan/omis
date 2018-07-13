/**
 * Offender relationships list screen behavior.
 * 
 * Author: Joel Norris
 * Author: Yidong Li
 * Version: 0.1.0 (January 14, 2016)
 * Since: OMIS 3.0
 */
window.onload = function() {
	applyActionMenu(document.getElementById("offenderRelationshipListActionMenuLink"));
	applyRemoveLinkConfirmation();
	var relationshipRows = document.getElementsByClassName("rowActionMenuItem");
	for(var x =0; x < relationshipRows.length; x++) {
		applyActionMenu(relationshipRows[x],
			function() {
				applyLinkConfirmation("removeLink", "removeRelationshipLinkConfirmation", "omis.offenderrelationship.msgs.offenderRelationship");
			}
		);
	}
}