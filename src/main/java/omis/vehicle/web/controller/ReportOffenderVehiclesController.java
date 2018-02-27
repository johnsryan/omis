package omis.vehicle.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.vehicle.domain.VehicleAssociation;
import omis.vehicle.report.OffenderVehicleSummary;
//import omis.person.domain.Person;
import omis.vehicle.report.OffenderVehicleSummaryReportService;

/**
 * Vehicle association report controller.
 * @author Yidong Li
 * @author Ryan Johns
 * @version 0.1.1 (Jun 8, 2016)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/vehicle")
@PreAuthorize("hasRole('USER')")
public class ReportOffenderVehiclesController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME
		= "vehicle/list";
	
	private static final String VEHICLES_ACTION_MENU_VIEW_NAME
		= "vehicle/includes/vehiclesActionMenu";

	private static final String VEHICLE_ROW_ACTION_MENU_VIEW_NAME
		= "vehicle/includes/vehicleRowActionMenu";
	
	/* Model keys. */
	
	private static final String OFFENDER_VEHICLE_SUMMARIES_MODEL_KEY
		= "offenderVehicleSummaries";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String VEHICLE_ASSOCIATION_MODEL_KEY 
		= "vehicleAssociation";
	
	/* Report services. */
	
	@Autowired
	@Qualifier("offenderVehicleSummaryReportService")
	private OffenderVehicleSummaryReportService 
		offenderVehicleSummaryReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private PropertyEditorFactory vehicleAssociationPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Report names. */
	
	private static final String VEHICLE_LISTING_REPORT_NAME 
		= "/CaseManagement/Vehicles/Vehicle_Listing";

	private static final String VEHICLE_DETAILS_REPORT_NAME 
		= "/CaseManagement/Vehicles/Vehicle_Details";

	/* Report parameter names. */
	
	private static final String VEHICLE_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String VEHICLE_DETAILS_ID_REPORT_PARAM_NAME 
		= "VA_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates a controller to report offender vehicle. */
	public ReportOffenderVehiclesController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Displays a list of offender vehicles.
	 * 
	 * @param offender offender
	 * @return view to display the list of offender vehicles
	 */
	@RequestMapping(value="/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('VEHICLE_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
		@RequestParam(value = "offender", required = true)
			final Offender offender) {
			ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			List<OffenderVehicleSummary> offenderVehicleSummaries 
				= this.offenderVehicleSummaryReportService
				.findByOffender(offender); 
			mav.addObject(OFFENDER_VEHICLE_SUMMARIES_MODEL_KEY,
				offenderVehicleSummaries);
			mav.addObject(OFFENDER_MODEL_KEY, offender);
			return mav;
	}

	/**
	 * Returns a view for an vehicles action menu pertaining to the specified
	 * offender.
	 * 
	 * @param offender offender
	 * @return model and view for alerts action menu
	 */
	@RequestMapping(value = "/vehiclesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView vehiclesActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(VEHICLES_ACTION_MENU_VIEW_NAME, map);
	}
	
	/** Returns row action menu for vehicles.
	 * @param vehicleAssociation - vehicle association.
	 * @return row action menu. */
	@RequestMapping(value="/vehicleRowActionMenu.html",
			method=RequestMethod.GET)
	public ModelAndView vehicleRowActionMenu(
			@RequestParam(value="vehicleAssociation", required=true) 
			final VehicleAssociation vehicleAssociation) {
		ModelMap modelMap = new ModelMap();
		modelMap.put(VEHICLE_ASSOCIATION_MODEL_KEY, vehicleAssociation);
		return new ModelAndView(VEHICLE_ROW_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}

	/* Reports. */
	
	/**
	 * Returns the report for chronological note listing.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/vehicleListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VEHICLE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportChronologicalNoteListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VEHICLE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VEHICLE_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified vehicle association.
	 * 
	 * @param vehicleAssociation vehicle association
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/vehicleDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VEHICLE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportVehicleDetails(@RequestParam(
			value = "vehicleAssociation", required = true)
			final VehicleAssociation vehicleAssociation,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(VEHICLE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(vehicleAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
				VEHICLE_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder init binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(VehicleAssociation.class,
			this.vehicleAssociationPropertyEditorFactory.createPropertyEditor());
	}
}