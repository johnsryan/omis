package omis.userpreference.web.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import omis.io.FileRemover;
import omis.io.FilenameGenerator;
import omis.media.domain.Photo;
import omis.media.exception.PhotoExistsException;
import omis.media.io.PhotoPersister;
import omis.media.io.PhotoRetriever;
import omis.user.domain.UserAccount;
import omis.userpreference.domain.ColorValue;
import omis.userpreference.domain.UserPreference;
import omis.userpreference.service.UserPreferenceService;
import omis.userpreference.web.form.UserPreferenceForm;
import omis.userpreference.web.validator.UserPreferenceFormValidator;

/**
 * User preference controller.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Nov 20, 2015)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/userPreference")
@PreAuthorize("hasRole('USER')")
public class UserPreferenceController {

	/* Redirects. */
	
	private static final String EDIT_REDIRECT_URL
		= "redirect:/userPreference/edit.html?userPreference=%d";
	private static final String USER_PREFERENCE_CSS_VIEW_NAME 
		= "userPreference/style/userPreference";
	
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "userPreference/edit";
	
	/* Session attribute keys. */
	
	private static final String USER_APPEARANCE_SESSION_ATTRIBUTE_KEY
		= "userAppearance";
	
	/* Model keys. */
	
	private static final String USER_ACCOUNT_MODEL_KEY = "userAccount";
	private static final String EDIT_FORM_MODEL_KEY = "userPreferenceForm";
	private static final String FOREGROUND_HUE_MODEL_KEY = "foregroundHue";
	private static final String FOREGROUND_SAT_MODEL_KEY
		= "foregroundSaturation";
	private static final String BACKGROUND_HUE_MODEL_KEY = "backgroundHue";
	private static final String BACKGROUND_SAT_MODEL_KEY
		= "backgroundSaturation";
	private static final String USER_PREFERENCE_MODEL_KEY
		= "userPreference";
	private static final String USER_PREFERENCE_FORM_MODEL_KEY
		= "userPreferenceForm";
	private static final String FOREGROUND_COLOR_VALUE_MODEL_KEY = "foregroundColorValue";
	private static final String BACKGROUND_COLOR_VALUE_MODEL_KEY = "backgroundColorValue";
	private static final String ACCENT_COLOR_VALUE_MODEL_KEY = "accentColorValue";
	private static final String BACKGROUND_PHOTO_MODEL_KEY = "backgroundPhoto";
	
	/* String formats. */
	
	private static final String SATURATION_FORMAT = "%d%%";
	
	/* Services. */
	
	@Autowired
	@Qualifier("userPreferenceService")
	private UserPreferenceService userPreferenceService;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("userPreferenceFormValidator")
	private UserPreferenceFormValidator userPreferenceFormValidator;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("userPreferencePropertyEditorFactory")
	private PropertyEditorFactory userPreferencePropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("photoPropertyEditorFactory")
	private PropertyEditorFactory photoPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("userPreferencePhotoRetriever")
	private PhotoRetriever userPreferencePhotoRetriever;
	
	@Autowired
	@Qualifier("userPreferencePhotoRemover")
	private FileRemover userPreferencePhotoRemover;
	
	@Autowired
	@Qualifier("userPreferencePhotoPersister")
	private PhotoPersister userPreferencePhotoPersister;
	
	@Autowired
	@Qualifier("photoFilenameGenerator")
	private FilenameGenerator photoFilenameGenerator;
	
	/* Constructor. */
	
	/**
	 * Instantiates a default instance of user preference controller.
	 */
	public UserPreferenceController() {
		//Default controller.
	}
	
	/* URL mapped methods. */
	
	/**
	 * Returns model and view to create user preferences.
	 * 
	 * @return edit view for user preferences
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(value = "userAccount",
			required = true) final UserAccount userAccount) {
		ModelMap map = new ModelMap();
		map.addAttribute(USER_ACCOUNT_MODEL_KEY, userAccount);
		UserPreferenceForm form = new UserPreferenceForm();
		this.populateEditForm(form, null);
		map.addAttribute(USER_PREFERENCE_FORM_MODEL_KEY, form);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Saves user preferences for the specified user.
	 * 
	 * @return redirect to
	 * @throws PhotoExistsException Thrown when a duplicate photo exists.
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.POST)
	public ModelAndView save(@RequestParam(value = "userAccount",
			required = true) final UserAccount userAccount,
			final UserPreferenceForm form, final BindingResult result,
			final HttpServletRequest request) throws PhotoExistsException {
		this.userPreferenceFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ EDIT_FORM_MODEL_KEY, result);
			map.addAttribute(USER_ACCOUNT_MODEL_KEY, userAccount);
			return new ModelAndView(EDIT_VIEW_NAME, map);
		}
		final Photo photo;
		if (form.getChangeBackgroundPhoto()) {
			if (form.getBackgroundPhotoData() != null && form.getBackgroundPhotoData().length > 0) {
				String filename = this.photoFilenameGenerator.generate();
				photo = this.userPreferenceService.createPhoto(filename, new Date());
				this.userPreferencePhotoPersister.persist(photo, form.getBackgroundPhotoData());
			} else {
				photo = null;
			}
		} else {
			photo = form.getBackgroundPhoto();;
		}
		UserPreference preference = this.userPreferenceService.create(
				form.getForegroundHue(), form.getForegroundSaturation(),
				form.getForegroundLightness(), form.getForegroundOpacity(),
				form.getBackgroundHue(), form.getBackgroundSaturation(),
				form.getBackgroundLightness(), form.getBackgroundOpacity(),
				form.getAccentHue(), form.getAccentSaturation(),
				form.getAccentLightness(), form.getAccentOpacity(),
				form.getWhiteBackground(), form.getShadows(), form.getBorderRadius(),
				form.getDisplayTheme(), photo, userAccount);
		request.getSession().removeAttribute(
				USER_APPEARANCE_SESSION_ATTRIBUTE_KEY);
		return new ModelAndView(String.format(
				EDIT_REDIRECT_URL, preference.getId()));
	}
	
	/**
	 * Returns model and view to edit user preference.
	 * 
	 * @param userPreference user preference
	 * @return model and view to edit user preference
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "userPreference",
			required = true)final UserPreference userPreference) {
		ModelMap map = new ModelMap();
		UserPreferenceForm form = new UserPreferenceForm();
		this.populateEditForm(form, userPreference);
		map.addAttribute(USER_PREFERENCE_FORM_MODEL_KEY, form);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Updates the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param form user preference form
	 * @param result binding result
	 * @return redirect url
	 * @throws PhotoExistsException Thrown when a duplicate photo exists
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.POST)
	public ModelAndView update(@RequestParam(value = "userPreference",
			required = true)final UserPreference userPreference,
			final UserPreferenceForm form, final BindingResult result,
			final HttpServletRequest request) throws PhotoExistsException {
		this.userPreferenceFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ EDIT_FORM_MODEL_KEY, result);
			map.addAttribute(USER_PREFERENCE_MODEL_KEY, userPreference);
			return new ModelAndView(EDIT_VIEW_NAME, map);
		}
		final Photo photo;
		if (form.getChangeBackgroundPhoto()) {
			if (form.getBackgroundPhotoData() != null && form.getBackgroundPhotoData().length > 0) {
				String filename = this.photoFilenameGenerator.generate();
				photo = this.userPreferenceService.createPhoto(filename, new Date());
				this.userPreferencePhotoPersister.persist(photo, form.getBackgroundPhotoData());
			} else {
				photo = null;
			}
			if (userPreference.getBackgroundPhoto() != null) {
				this.userPreferenceService.removePhoto(userPreference.getBackgroundPhoto());
				this.userPreferencePhotoRemover.remove(userPreference.getBackgroundPhoto()
						.getFilename());
			}
		} else {
			photo = userPreference.getBackgroundPhoto();
		}
		this.userPreferenceService.update(userPreference,
				form.getForegroundHue(), form.getForegroundSaturation(),
				form.getForegroundLightness(), form.getForegroundOpacity(),
				form.getBackgroundHue(), form.getBackgroundSaturation(),
				form.getBackgroundLightness(), form.getBackgroundOpacity(),
				form.getAccentHue(), form.getAccentSaturation(),
				form.getAccentLightness(), form.getAccentOpacity(),
				form.getWhiteBackground(), form.getShadows(), form.getBorderRadius(),
				form.getDisplayTheme(), photo);
		request.getSession().removeAttribute(
				USER_APPEARANCE_SESSION_ATTRIBUTE_KEY);
		return new ModelAndView(String.format(
				EDIT_REDIRECT_URL, userPreference.getId()));
	}
	
	/**
	 * Returns the user preference style sheet.
	 * @return model and view for colors style sheet
	 */
	@RequestMapping(value = "/style/userPreference.css")
	public ModelAndView showUserPreferenceStylesheet() {
		ModelMap map = new ModelMap();
		UserPreference userPreference = this.userPreferenceService
				.findByUserAccount();
		final ColorValue backgroundColorValue;
		final ColorValue foregroundColorValue;
		final ColorValue accentColorValue;
		if(userPreference != null) {
			backgroundColorValue = userPreference.getBackgroundColorValue();
			foregroundColorValue = userPreference.getForegroundColorValue();
			accentColorValue = userPreference.getAccentColorValue();
			map.addAttribute(BACKGROUND_PHOTO_MODEL_KEY, userPreference.getBackgroundPhoto());
		} else {
			Short defaultHue = 0;
			Short defaultSaturation = 0;
			Short defaultBackgroundLightness = 100;
			Short defaultForegroundLightness = 90;
			Short defaultAccentHue = 230;
			Short defaultAccentSaturation = 100;
			Short defaultAccentLightness = 50;
			BigDecimal defaultOpacity = new BigDecimal(1.0);
			backgroundColorValue = new ColorValue(defaultHue, defaultSaturation, defaultBackgroundLightness, defaultOpacity); 
					//new ColorValue(defaultHue, defaultSaturation);
			foregroundColorValue =  new ColorValue(defaultHue, defaultSaturation, defaultForegroundLightness, defaultOpacity);
					//new ColorValue(defaultHue, defaultSaturation);
			accentColorValue =  new ColorValue(defaultAccentHue, defaultAccentSaturation, defaultAccentLightness, defaultOpacity);
		}
		map.addAttribute(FOREGROUND_HUE_MODEL_KEY,
				foregroundColorValue.getHue());
		map.addAttribute(FOREGROUND_SAT_MODEL_KEY,
				String.format(SATURATION_FORMAT, 
						foregroundColorValue.getSaturation()));
		map.addAttribute(BACKGROUND_HUE_MODEL_KEY, 
				backgroundColorValue.getHue());
		map.addAttribute(BACKGROUND_SAT_MODEL_KEY,
				String.format(SATURATION_FORMAT,
						backgroundColorValue.getSaturation()));
		map.addAttribute(FOREGROUND_COLOR_VALUE_MODEL_KEY, foregroundColorValue);
		map.addAttribute(BACKGROUND_COLOR_VALUE_MODEL_KEY, backgroundColorValue);
		map.addAttribute(ACCENT_COLOR_VALUE_MODEL_KEY, accentColorValue);
		return new ModelAndView(USER_PREFERENCE_CSS_VIEW_NAME, map);
	}

	/**
	 * Displays the photo data of the specified content type for the specified
	 * photo.
	 * 
	 * @param photo photo
	 * @param contentType content type
	 * @return response entity byte array
	 */
	@RequestMapping(value = "/displayPhoto.html")
	public ResponseEntity<byte[]> displayBackgroundPhoto(
			@RequestParam(value = "photo", 
			required = true) final Photo photo, 
			@RequestParam(value = "contentType", required = true) 
			final String contentType) {
		byte[] photoData;
		if (photo != null) {
			photoData = this.userPreferencePhotoRetriever
					.retrieve(photo);
		} else {
			photoData = this.userPreferencePhotoRetriever
					.retrieve("NotSet.jpg");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", contentType);
		return new ResponseEntity<byte[]>(photoData, headers, HttpStatus.OK);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the user preference form with values from the specified
	 * user preference.
	 * 
	 * @param form user preference form
	 * @param userPreference user preference
	 * @return populated user preference form
	 */
	private UserPreferenceForm populateEditForm(final UserPreferenceForm form,
			final UserPreference userPreference) {
		if (userPreference != null) {
			form.setForegroundHue(
					userPreference.getForegroundColorValue().getHue());
			form.setForegroundSaturation(
					userPreference.getForegroundColorValue().getSaturation());
			form.setForegroundOpacity(userPreference.getForegroundColorValue().getOpacity());
			form.setForegroundLightness(userPreference.getForegroundColorValue().getLightness());
			form.setBackgroundHue(
					userPreference.getBackgroundColorValue().getHue());
			form.setBackgroundSaturation(
					userPreference.getBackgroundColorValue().getSaturation());
			form.setBackgroundLightness(userPreference.getBackgroundColorValue().getLightness());
			form.setAccentHue(userPreference.getAccentColorValue().getHue());
			form.setAccentSaturation(userPreference.getAccentColorValue()
					.getSaturation());
			form.setBorderRadius(userPreference.getBorderRadius());
			form.setShadows(userPreference.getShadows());
		} else {
			Short defaultHue = 0;
			Short defaultSaturation = 0;
			Short defaultAccentSaturation = 100;
			Short defaultAccentHue = 230;
			Short defaultBackgroundLightness = 100;
			Short defaultForegroundLightness = 90;
			Short defaultBorderRadius = 0;
			Boolean defaultShadows = false;
			BigDecimal defaultOpacity = new BigDecimal(1.0);
			form.setForegroundHue(defaultHue);
			form.setForegroundSaturation(defaultSaturation);
			form.setForegroundOpacity(defaultOpacity);
			form.setForegroundLightness(defaultForegroundLightness);
			form.setBackgroundHue(defaultHue);
			form.setBackgroundSaturation(defaultHue);
			form.setBackgroundLightness(defaultBackgroundLightness);
			form.setAccentHue(defaultAccentHue);
			form.setAccentSaturation(defaultAccentSaturation);
			form.setBorderRadius(defaultBorderRadius);
			form.setShadows(defaultShadows);
		}
		return form;
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				UserPreference.class,
				this.userPreferencePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				UserAccount.class,
				this.userAccountPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Photo.class,
				this.photoPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}