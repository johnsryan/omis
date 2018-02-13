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
package omis.offenderphoto.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.BusinessException;
import omis.exception.DuplicateEntityFoundException;
import omis.io.FileRemover;
import omis.io.FilenameGenerator;
import omis.media.domain.Photo;
import omis.media.io.PhotoPersister;
import omis.media.io.PhotoRetriever;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offenderphoto.domain.OffenderPhotoAssociation;
import omis.offenderphoto.domain.OffenderPhotoAssociationNote;
import omis.offenderphoto.service.OffenderPhotoService;
import omis.offenderphoto.web.form.OffenderPhotoAssociationNoteItem;
import omis.offenderphoto.web.form.OffenderPhotoAssociationNoteItemOperation;
import omis.offenderphoto.web.form.OffenderPhotoForm;
import omis.offenderphoto.web.validator.OffenderPhotoFormValidator;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Web controller for photos of people.
 *  
 * @author Stephen Abson
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.3 (Feb 12, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/offenderPhoto")
@PreAuthorize("hasRole('USER')")
public class OffenderPhotoController {
	
	/* View names. */
	
	private static final String LIST_VIEW_NAME = "offenderPhoto/list";
	
	private static final String EDIT_FORM_VIEW_NAME = "offenderPhoto/edit";
	
	private static final String PHOTOS_ACTION_MENU
		= "offenderPhoto/includes/photosActionMenu";
	
	private static final String
	OFFENDER_PHOTO_ASSOCIATION_ACTION_MENU_VIEW_NAME
		= "offenderPhoto/includes/photoActionMenu";
	
	private static final String
	OFFENDER_PHOTO_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME
		= "offenderPhoto/includes/photosRowActionMenu";
	
	private static final String
	OFFENDER_PHOTO_ASSOC_NOTE_ACTION_MENU_VIEW_NAME 
		= "offenderPhoto/includes/photoAssociationNoteItemsActionMenu";
	
	private static final String NOTE_ITEM_ROW_VIEW_NAME
		= "offenderPhoto/includes/offenderPhotoAssociationNoteItemTableRow";

	/* Redirects. */
	
	private static final String LIST_REDIRECT
		= "redirect:/offenderPhoto/list.html?offender=%d";
	
	/* Model keys. */
		
	private static final String ASSOCIATIONS_MODEL_KEY = "associations";
	
	private static final String OFFENDER_PHOTO_FORM_MODEL_KEY
		= "offenderPhotoForm";

	private static final String ASSOCIATION_MODEL_KEY = "association";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String OFFENDER_PHOTO_ASSOCIATION_MODEL_KEY
		= "association";
	
	private static final String PROFILE_MODEL_KEY = "profile";
	
	private static final String NOTE_ITEM_MODEL_KEY
		= "offenderPhotoAssociationNoteItem";
	
	private static final String NOTE_ITEM_INDEX_MODEL_KEY
		= "offenderPhotoAssociationNoteItemIndex"; 
	
	/* Report names. */
	
	private static final String OFFENDER_PHOTO_LISTING_REPORT_NAME 
		= "/BasicInformation/Mugshots/Mugshot_Listing";
	
	private static final String OFFENDER_PHOTO_DETAILS_REPORT_NAME 
		= "/BasicInformation/Mugshots/Mugshot_Detail";
	
	/* Report parameter names. */
	
	private static final String OFFENDER_PHOTO_LISTING_ID_REPORT_PARAM_NAME
		= "DOC_ID";
	
	private static final String OFFENDER_PHOTO_DETAILS_ID_REPORT_PARAM_NAME
		= "PERSON_PHOTO_ASSOC_ID";

	/* Helper classes. */
	
	@Autowired
	@Qualifier("offenderPhotoRetriever")
	private PhotoRetriever offenderPhotoRetriever;
	
	@Autowired
	@Qualifier("offenderPhotoRemover")
	private FileRemover offenderPhotoRemover;
	
	@Autowired
	@Qualifier("offenderPhotoPersister")
	private PhotoPersister offenderPhotoPersister;
	
	@Autowired
	@Qualifier("photoFilenameGenerator")
	private FilenameGenerator photoFilenameGenerator;
	
	/* Services. */
	
	@Autowired
	@Qualifier("offenderPhotoService")
	private OffenderPhotoService offenderPhotoService;
	
	/* Report services. */
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPhotoAssociationPropertyEditorFactory")
	private PropertyEditorFactory offenderPhotoAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPhotoAssociationNotePropertyEditorFactory")
	private PropertyEditorFactory offenderPhotoAssociationNotePropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("createOffenderPhotoFormValidator")
	private OffenderPhotoFormValidator createOffenderPhotoFormValidator;

	@Autowired
	@Qualifier("editOffenderPhotoFormValidator")
	private OffenderPhotoFormValidator editOffenderPhotoFormValidator;

	/* Helpers. */
	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates a default controller for person photos. */
	public OffenderPhotoController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Outputs the photo of the specified offender.
	 * 
	 * @param offender offender
	 * @param contentType photo MIME content type
	 * @return photo data for offender
	 */
	@RequestMapping("/displayProfilePhoto.html")
	public ResponseEntity<byte[]> displayProfilePhoto(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "contentType", required = true)
				final String contentType) {
		Photo photo = this.offenderPhotoService.findProfilePhoto(offender);
		byte[] photoData;
		if (photo != null) {
			photoData = this.offenderPhotoRetriever.retrieve(photo);	
		} else {
			// TODO: Do this better - 'NotSet.jpg' should not be hard coded
			photoData = this.offenderPhotoRetriever.retrieve("NotSet.jpg");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", contentType);
		return new ResponseEntity<byte[]>(photoData, headers, HttpStatus.OK);
	}
	
	/**
	 * Outputs the photo of the specified offender photo association.
	 * 
	 * @param association association
	 * @param contentType photo MIME content type
	 * @return photo data for association
	 */
	@RequestMapping("/displayPhoto.html")
	public ResponseEntity<byte[]> displayPhoto(
			@RequestParam(value = "association", required = true)
				final OffenderPhotoAssociation association,
			@RequestParam(value = "contentType", required = true)
				final String contentType) {
		Photo photo = association.getPhoto();
		byte[] photoData;
		if (photo != null) {
			photoData = this.offenderPhotoRetriever.retrieve(photo);	
		} else {
			// TODO: Do this better - 'NotSet.jpg' should not be hard coded
			photoData = this.offenderPhotoRetriever.retrieve("NotSet.jpg");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", contentType);
		return new ResponseEntity<byte[]>(photoData, headers, HttpStatus.OK);
	}
	
	/**
	 * Displays photos by offender.
	 * 
	 * @param offender offender
	 * @return screen to display photos by offender
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		List<OffenderPhotoAssociation> associations = this.offenderPhotoService
				.findByOffender(offender);
		mav.addObject(ASSOCIATIONS_MODEL_KEY, associations);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Displays screen to create new offender photo.
	 * 
	 * @param offender offender for whom to create photo
	 * @param profile whether new photo is to be profile photo
	 * @return screen to create new offender photo
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "profile", required = true)
				final boolean profile) {
		OffenderPhotoForm offenderPhotoForm = new OffenderPhotoForm();
		return this.prepareEditMav(offender, offenderPhotoForm);
	}
	
	/**
	 * Displays screen to update existing offender photo.
	 * 
	 * @param association association of offender photo
	 * @return screen to update existing offender photo
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "association", required = true)
				final OffenderPhotoAssociation association) {
		Offender offender = (Offender) association.getPerson();
		OffenderPhotoForm offenderPhotoForm = new OffenderPhotoForm();
		byte[] photoData = this.offenderPhotoRetriever
				.retrieve(association.getPhoto());
		offenderPhotoForm.setPhotoData(photoData);
		offenderPhotoForm.setPhotoDate(association.getPhoto().getDate());
		List<OffenderPhotoAssociationNoteItem> items =
				new ArrayList<OffenderPhotoAssociationNoteItem>();
		for (OffenderPhotoAssociationNote note : 
			this.offenderPhotoService.findAssociationNotes(association)) {
			OffenderPhotoAssociationNoteItem item = 
					new OffenderPhotoAssociationNoteItem();
			item.setDate(note.getDate());
			item.setValue(note.getValue());
			item.setNote(note);
			item.setOperation(OffenderPhotoAssociationNoteItemOperation.UPDATE);
			items.add(item);
		}
		offenderPhotoForm.setNoteItems(items);
		ModelAndView mav = this.prepareEditMav(offender, offenderPhotoForm);
		mav.addObject(ASSOCIATION_MODEL_KEY, association);
		return mav;
	}
	
	/**
	 * Saves a new offender photo.
	 * 
	 * @param offender offender for whom to create new photo
	 * @param profile whether new photo should be used as profile photo
	 * @param offenderPhotoForm form for offender photos
	 * @param result binding result
	 * @return redirect to list offender photos
	 * @throws DuplicateEntityFoundException thrown when a duplicate offender
	 * photo association note is found
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "profile", required = true)
				final boolean profile,
			final OffenderPhotoForm offenderPhotoForm,
			final BindingResult result) throws DuplicateEntityFoundException {
		this.createOffenderPhotoFormValidator
			.validate(offenderPhotoForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(
					offender, offenderPhotoForm, result);
		}
		String filename = this.photoFilenameGenerator.generate();
		OffenderPhotoAssociation association;
		if (profile) {
			association = this.offenderPhotoService.associateProfilePhotoFile(
					offender, filename, offenderPhotoForm.getPhotoDate());
		} else {
			association = this.offenderPhotoService
					.associateNoneProfilePhotoFile(offender, filename,
							offenderPhotoForm.getPhotoDate());
		}
		offenderPhotoPersister.persist(
				association.getPhoto(), offenderPhotoForm.getPhotoData());
		this.processOffenderPhotoAssociationNoteItems(
				offenderPhotoForm.getNoteItems(), association);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}

	/**
	 * Updates an existing offender photo.
	 * 
	 * @param association association of offender photo to update
	 * @param offenderPhotoForm form for offender photos
	 * @param result binding result
	 * @return redirect to list offender photos
	 * @throws DuplicateEntityFoundException thrown when a duplicate offender
	 * photo association note is found
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "association", required = true)
				final OffenderPhotoAssociation association,
			final OffenderPhotoForm offenderPhotoForm,
				final BindingResult result) 
			throws DuplicateEntityFoundException {
		final Offender offender = (Offender) association.getPerson();
		this.editOffenderPhotoFormValidator.validate(offenderPhotoForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayMav(
					offender, offenderPhotoForm, result);
			mav.addObject(ASSOCIATION_MODEL_KEY, association);
			return mav;
		}
		this.offenderPhotoService.updateAssociation(association,
				offenderPhotoForm.getPhotoDate());
		this.processOffenderPhotoAssociationNoteItems(
				offenderPhotoForm.getNoteItems(), association);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Removes an offender photo association.
	 * 
	 * @param association offender photo association to remove
	 * @return redirect to list offender photos
	 * @throws BusinessException if the association is the 
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "association", required = true)
				final OffenderPhotoAssociation association)
						throws BusinessException {
		Offender offender = (Offender) association.getPerson();
		String filename = association.getPhoto().getFilename();
		this.offenderPhotoService.remove(association);
		this.offenderPhotoRemover.remove(filename);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Sets offender profile photo.
	 * 
	 * @param association association of photo to set as profile for offender
	 * @return redirect to list offender photos
	 */
	@PreAuthorize("hasRole('OFFENDER_PHOTO_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/setProfilePhoto.html", method = RequestMethod.GET)
	public ModelAndView setProfilePhoto(
			@RequestParam(value = "association", required = true)
				final OffenderPhotoAssociation association) {
		this.offenderPhotoService.setOffenderProfilePhoto(association);
		Offender offender = (Offender) association.getPerson();
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Photos action menu.
	 * 
	 * @param offender offender
	 * @param profile profile
	 * @return
	 */
	@RequestMapping(value = "/photosActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView photosActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "profile", required = true)
				final boolean profile) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(PROFILE_MODEL_KEY, profile);
		return new ModelAndView(PHOTOS_ACTION_MENU, map);
	}
	
	/**
	 * Offender photo association row action menu.
	 * 
	 * @param association offender photo association
	 * @return model and view for offender photo association row action menu
	 */
	@RequestMapping(value = "/photosRowActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayOffenderPhotoAssociationRowActionMenu(
			@RequestParam(value = "association", required = true)
			final OffenderPhotoAssociation association) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_PHOTO_ASSOCIATION_MODEL_KEY, association);
		return new ModelAndView(
				OFFENDER_PHOTO_ASSOCIATION_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Offender photo association action menu.
	 * 
	 * @param offender offender
	 * @return model and view for offender photo association action menu
	 */
	@RequestMapping(value = "/photoActionMenu.html", method = RequestMethod.GET)
	public ModelAndView displayOffenderPhotoAssociationActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
				OFFENDER_PHOTO_ASSOCIATION_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the action menu for offender photo association note items.
	 * 
	 * @return offender photo association note items action menu model and view
	 */
	@RequestMapping(value = "offenderPhotoAssociationNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayOffenderPhotoAssociationNoteItemsActionMenu() {
		ModelMap map = new ModelMap();
		return new ModelAndView(
				OFFENDER_PHOTO_ASSOC_NOTE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the offender photo association note item row.
	 * 
	 * @param noteItemIndex offender photo association note item index
	 * @return model and view for offender photo association note item row
	 */
	@RequestMapping(value = "createOffenderPhotoAssociationNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayOffenderPhotoAssociationNoteItemRow(
				@RequestParam(value = "offenderPhotoAssociationNoteItemIndex",
						required = true)final Integer noteItemIndex) {
		ModelMap map = new ModelMap();
		OffenderPhotoAssociationNoteItem item 
			= new OffenderPhotoAssociationNoteItem();
		item.setOperation(OffenderPhotoAssociationNoteItemOperation.CREATE);
		map.addAttribute(NOTE_ITEM_MODEL_KEY, item);
		map.addAttribute(NOTE_ITEM_INDEX_MODEL_KEY, noteItemIndex);
		return new ModelAndView(NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/* Helper methods. */
	
	// Prepares screen to edit offender photo information
	private ModelAndView prepareEditMav(final Offender offender,
			final OffenderPhotoForm offenderPhotoForm) {
		ModelAndView mav = new ModelAndView(EDIT_FORM_VIEW_NAME);
		mav.addObject(OFFENDER_PHOTO_FORM_MODEL_KEY, offenderPhotoForm);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		final Integer offenderPhotoAssociationNoteIndex;
		if (offenderPhotoForm.getNoteItems() != null) {
			offenderPhotoAssociationNoteIndex = offenderPhotoForm
					.getNoteItems().size(); 
		} else {
			offenderPhotoAssociationNoteIndex = 0;
		}
		mav.addObject(NOTE_ITEM_INDEX_MODEL_KEY, 
				offenderPhotoAssociationNoteIndex);
		this.addOffenderSummary(mav, offender);
		return mav;
	}
	
	// Adds offender summary to model and view
	private void addOffenderSummary(
			final ModelAndView mav, final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	// Prepares redisplay of screen to edit offender photo information
	private ModelAndView prepareRedisplayMav(final Offender offender,
			final OffenderPhotoForm offenderPhotoForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(offender, offenderPhotoForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ OFFENDER_PHOTO_FORM_MODEL_KEY, result);
		return mav;
	}
	
	/*
	 * Processes the specified list of offender photo association note items
	 * according to their operation values.
	 *  
	 * @param items offender photo note items
	 * @param association offender photo association
	 * @throws DuplicateEntityFoundException thrown when a duplicate offender
	 * photo association note is found
	 */
	private void processOffenderPhotoAssociationNoteItems(
			final List<OffenderPhotoAssociationNoteItem> items,
			final OffenderPhotoAssociation association)
		throws DuplicateEntityFoundException {
		if (items != null) {
			for (OffenderPhotoAssociationNoteItem item : items) {
				if (OffenderPhotoAssociationNoteItemOperation.CREATE
						.equals(item.getOperation())) {
					this.offenderPhotoService
					.addAssociationNote(association, item.getDate(),
							item.getValue());
				} else if (OffenderPhotoAssociationNoteItemOperation.UPDATE
						.equals(item.getOperation())) {
					if (!item.getValue().equals(item.getNote().getValue())
							|| item.getDate() != item.getNote().getDate())
					this.offenderPhotoService
						.updateAssociationNote(item.getNote(), item.getDate(),
								item.getValue());
				} if (OffenderPhotoAssociationNoteItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.offenderPhotoService.removeAssociationNote(
							item.getNote());
				}
			}
		}
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/offenderPhotosListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportOffenderPhotosListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(OFFENDER_PHOTO_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				OFFENDER_PHOTO_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offender photo association.
	 * 
	 * @param association offender photo association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/offenderPhotosDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportOffenderPhotosDetails(@RequestParam(
			value = "association", required = true)
			final OffenderPhotoAssociation association,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(OFFENDER_PHOTO_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(association.getId()));
		byte[] doc = this.reportRunner.runReport(
				OFFENDER_PHOTO_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	
	/* Init binder. */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerPropertyEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(OffenderPhotoAssociation.class,
				this.offenderPhotoAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(
				OffenderPhotoAssociationNote.class,
				this.offenderPhotoAssociationNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}