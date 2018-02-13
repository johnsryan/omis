package omis.offenderrelationship.web.controller.delegate;

import java.util.List;

import org.springframework.ui.ModelMap;

import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Delegate to prepare model for offender relationship notes.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Feb 8, 2018)
 * @since OMIS 3.0
 */
public class OffenderRelationshipNoteFieldsControllerDelegate {
	
	private static final String NOTE_CATEGORIES_MODEL_KEY
		= "offenderRelationshipNoteCategories";

	/**
	 * Instantiates delegate to prepare model for offender relationship notes.
	 */
	public OffenderRelationshipNoteFieldsControllerDelegate() {
		// Default instantiation
	}
	
	/**
	 * Prepares model map for offender relationship notes.
	 * 
	 * @param modelMap model map
	 * @param categories note categories
	 */
	public void prepareEditOffenderRelationshipNotesFields(
			final ModelMap modelMap,
			final List<RelationshipNoteCategory> categories) {
		modelMap.addAttribute(NOTE_CATEGORIES_MODEL_KEY, categories);
	}
}