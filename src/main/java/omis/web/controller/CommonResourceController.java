package omis.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import omis.media.domain.Photo;
import omis.media.io.PhotoRetriever;
import omis.user.domain.UserAccount;
import omis.user.service.UserAccountService;
import omis.userpreference.domain.UserPreference;
import omis.userpreference.service.UserPreferenceService;
import omis.userpreference.web.UserAppearance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for common resources.
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.3 (November 16, 2015)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/resources/common")
public class CommonResourceController {
	
	/* View names. */
	
	private static final String SERVER_CONFIG_SCRIPT_VIEW_NAME 
		= "common/scripts/js/ServerConfig";
	
	private static final String SESSION_CONFIG_SCRIPT_VIEW_NAME 
		= "common/scripts/js/SessionConfig";
	
	private static final String RESOLVE_BUNDLE_SCRIPT_VIEW_NAME
		= "common/scripts/js/resolveBundle";
	
	private static final String COLORS_CSS_VIEW_NAME 
		= "common/style/colors";
	
	private static final String FONTS_CSS_VIEW_NAME = "common/style/fonts";
	
	private static final String USER_PREFERENCE_CSS_VIEW_NAME = "common/style/userPreference";
	
	/* Model keys. */
	
	private static final String USERNAME_MODEL_KEY = "username";
	
	private static final String USER_ACCOUNT_ID_MODEL_KEY = "userAccountId";
	
	private static final String USER_ACCOUNT_LABEL_MODEL_KEY
		= "userAccountLabel";
	
	private static final String MESSAGES_MODEL_KEY = "messages";
	
	private static final String USER_APPEARANCE_MODEL_KEY = "userAppearance";
	
	/* Session keys. */
	
	private static final String USER_APPEARANCE_SESSION_ATTRIBUTE_KEY
		= "userAppearance";
	
	/* String formats. */
	
	private static final String USER_ACCOUNT_LABEL_FORMAT = "%s, %s (%s)";
	
	/* Services. */
	
	@Autowired
	@Qualifier("userAccountService")
	private UserAccountService userAccountService;
	
	@Autowired
	@Qualifier("userPreferenceService")
	private UserPreferenceService userPreferenceService;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("userPreferencePhotoRetriever")
	private PhotoRetriever userPreferencePhotoRetriever;

	/** Instantiates a default controller for common resources. */
	public CommonResourceController() {
		// Default instantiation
	}
	
	/**
	 * Returns a model and view to the script for the ServerConfig server
	 * configuration properties object.
	 * @return model and view to script for server configuration properties
	 * object
	 */
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@RequestMapping("/scripts/ServerConfig.js")
	public ModelAndView showServerConfigScript() {
		return new ModelAndView(SERVER_CONFIG_SCRIPT_VIEW_NAME);
	}
	
	/**
	 * Returns a model and view to the script for the SessionConfig 
	 * configuration properties object.
	 * @return model and view to script for session configuration properties
	 * object
	 */
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@RequestMapping("/scripts/SessionConfig.js")
	public ModelAndView showSessionConfigScript() {
		UserAccount userAccount = this.userAccountService.findByUsername(
				SecurityContextHolder.getContext().getAuthentication()
					.getName());
		ModelAndView mav = new ModelAndView(SESSION_CONFIG_SCRIPT_VIEW_NAME);
		mav.addObject(USERNAME_MODEL_KEY, userAccount.getUsername());
		mav.addObject(USER_ACCOUNT_ID_MODEL_KEY, userAccount.getId());
		mav.addObject(USER_ACCOUNT_LABEL_MODEL_KEY,
				String.format(USER_ACCOUNT_LABEL_FORMAT,
				userAccount.getUser().getName().getLastName(),
				userAccount.getUser().getName().getFirstName(),
				userAccount.getUsername()));
		return mav;
	}
	
	/**
	 * Returns a message resolver for the specified base name.
	 * 
	 * @param baseName base name
	 * @param locale locale
	 * @return model and view for message resolver for specified base name
	 */
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@RequestMapping("/scripts/resolveBundle.json")
	public ModelAndView getMessageResolver(
			@RequestParam(value = "baseName", required = true)
				final String baseName,
			final Locale locale) {
		ResourceBundle resourceBundle =
				ResourceBundle.getBundle(baseName, locale);
		Map<String, String> messages = new HashMap<String, String>();
		for (String key : resourceBundle.keySet()) {
			messages.put(key, resourceBundle.getString(key));
		}
		ModelAndView mav = new ModelAndView(RESOLVE_BUNDLE_SCRIPT_VIEW_NAME);
		mav.addObject(MESSAGES_MODEL_KEY, messages);
		return mav;
	}
	
	/**
	 * Returns the colors style sheet.
	 * @return model and view for colors style sheet
	 */
	@RequestMapping("/style/colors.css")
	public ModelAndView showColorsStylesheet(
			final HttpServletRequest request) {
		ModelMap map = new ModelMap();
		UserAppearance userAppearance = (UserAppearance) request.getSession()
				.getAttribute(USER_APPEARANCE_SESSION_ATTRIBUTE_KEY);
		if((UserAppearance) request.getSession()
				.getAttribute(USER_APPEARANCE_SESSION_ATTRIBUTE_KEY) != null) {
			map.addAttribute(USER_APPEARANCE_MODEL_KEY, userAppearance);
		} else if(SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal() instanceof UserDetails) {
			UserPreference userPreference
				= this.userPreferenceService.findByUserAccount();
			UserAppearance appearance = new UserAppearance();
			if (userPreference != null) {
				this.populateUserAppearance(appearance, userPreference);
			}
			appearance.setDate(new Date());
			map.addAttribute(USER_APPEARANCE_MODEL_KEY, appearance);
			request.getSession().setAttribute(
					USER_APPEARANCE_SESSION_ATTRIBUTE_KEY, appearance);
		}
		return new ModelAndView(COLORS_CSS_VIEW_NAME, map);
	}
	
	/**
	 * Returns the user preference style sheet.
	 * 
	 * @param request http servlet request
	 * @return model and view for user preference style sheet
	 */
	@RequestMapping("/style/userPreference.css")
	public ModelAndView showUserPreferenceStylesheet(
			final HttpServletRequest request) {
		ModelMap map = new ModelMap();
		UserAppearance userAppearance = (UserAppearance) request.getSession()
				.getAttribute(USER_APPEARANCE_SESSION_ATTRIBUTE_KEY);
		if((UserAppearance) request.getSession()
				.getAttribute(USER_APPEARANCE_SESSION_ATTRIBUTE_KEY) != null) {
			map.addAttribute(USER_APPEARANCE_MODEL_KEY, userAppearance);
		} else if(SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal() instanceof UserDetails) {
			UserPreference userPreference
				= this.userPreferenceService.findByUserAccount();
			UserAppearance appearance = new UserAppearance();
			if (userPreference != null) {
				this.populateUserAppearance(appearance, userPreference);
			}
			appearance.setDate(new Date());
			map.addAttribute(USER_APPEARANCE_MODEL_KEY, appearance);
			request.getSession().setAttribute(
					USER_APPEARANCE_SESSION_ATTRIBUTE_KEY, appearance);
		}
		return new ModelAndView(USER_PREFERENCE_CSS_VIEW_NAME, map);
	}
	
	/**
	 * Returns the fonts stylesheet.
	 * @return model and view for colors style sheet
	 */
	@RequestMapping("/style/fonts.css")
	public ModelAndView showFontsStylesheet() {
		return new ModelAndView(FONTS_CSS_VIEW_NAME);
	}
	
	/**
	 * Displays the photo data of the specified content type for the specified
	 * photo.
	 * 
	 * @param photo photo
	 * @param contentType content type
	 * @return response entity byte array
	 */
	@RequestMapping(value = "/backgroundPhoto.jpg")
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
	 * Populates a user appearance with the specified user preference.
	 *  
	 * @param userPreference user preference
	 * @return populated user appearance
	 */
	private UserAppearance populateUserAppearance(final UserAppearance appearance, final UserPreference userPreference) {
		appearance.setBackgroundColorValue(
				userPreference.getBackgroundColorValue());
		appearance.setForegroundColorValue(
				userPreference.getForegroundColorValue());
		appearance.setAccentColorValue(
				userPreference.getAccentColorValue());
		appearance.setWhiteBackground(
				userPreference.getWhiteBackground());
		appearance.setBorderRadius(userPreference.getBorderRadius());
		appearance.setShadows(userPreference.getShadows());
		appearance.setBackgroundPhoto(userPreference.getBackgroundPhoto());
		return appearance;
	}
}