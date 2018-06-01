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
package omis.offender.web.controller.delegate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;

import omis.offender.domain.Offender;
import omis.offender.web.summary.SummaryItem;
import omis.offender.web.summary.SummaryItemRegistry;

/**
 * Delegate to manage offender summaries in a model.
 * 
 * @author Stephen Abson
 * @author Ryan Johns
 * @author Annie Jacques
 * @version 0.1.1 (Sep 18, 2017)
 * @since OMIS 3.0
 */
public class OffenderSummaryModelDelegate {
	
	/* Summary Item Registry Model Keys */
	
	private static final String 
		BASIC_INFORMATION_SUMMARY_ITEM_REGISTRY_MODEL_KEY
			= "basicInformationSummaryItemRegistry";
	
	/*private static final String PLACEMENT_SUMMARY_ITEM_REGISTRY_MODEL_KEY =
			"placementSummaryItemRegistry";*/
	
	private static final String LEGACY_PLACEMENT_SUMMARY_ITEM_REGISTRY_MODEL_KEY
		= "legacyPlacementSummaryItemRegistry";

	private static final String OFFENDER_FLAG_SUMMARY_ITEM_REGISTRY_MODEL_KEY =
			"offenderFlagSummaryItemRegistry";

	private static final String FACILITY_SUMMARY_ITEM_REGISTRY_MODEL_KEY = 
			"facilitySummaryItemRegistry";

	private static final String
		COMMUNITY_SUPERVISION_SUMMARY_ITEM_REGISTRY_MODEL_KEY =
			"communitySupervisionSummaryItemRegistry";

	private static final String CONTACT_SUMMARY_ITEM_REGISTRY_MODEL_KEY = 
			"contactSummaryItemRegistry";
	
	private static final String DISCHARGE_DATA_SUMMARY_ITEM_REGISTRY_MODEL_KEY
		= "dischargeDataSummaryItemRegistry";
	
	private static final String IDENTIFICATION_SUMMARY_ITEM_REGISTRY_MODEL_KEY =
			"identificationNumberSummaryItemRegistry";
	
	private static final String OFFENDER_PROFILE_ITEMS_PROPERTIES_MODEL_KEY =
			"offenderProfileItemsProperties";
	
	/* Summary Item Registries */
	
	@Autowired
	@Qualifier("basicInformationSummaryItemRegistry")
	private SummaryItemRegistry basicInformationSummaryItemRegistry;
	
	@Autowired
	@Qualifier("offenderFlagSummaryItemRegistry")
	private SummaryItemRegistry offenderFlagSummaryItemRegistry;
	
	@Autowired
	@Qualifier("placementSummaryItemRegistry")
	private SummaryItemRegistry placementSummaryItemRegistry;
	
	@Autowired
	@Qualifier("legacyPlacementSummaryItemRegistry")
	private SummaryItemRegistry legacyPlacementSummaryItemRegistry;
	
	@Autowired
	@Qualifier("offenderContactSummaryItemRegistry")
	private SummaryItemRegistry contactSummaryItemRegistry;
	
	@Autowired
	@Qualifier("identificationNumbersSummaryItemRegistry")
	private SummaryItemRegistry identificationSummaryItemRegistry;

	@Autowired
	@Qualifier("communitySupervisionSummaryItemRegistry")
	private SummaryItemRegistry communitySupervisionSummaryItemRegistry;

	@Autowired
	@Qualifier("facilitySummaryItemRegistry")
	private SummaryItemRegistry facilitySummaryItemRegistry;
	
	@Autowired
	@Qualifier("dischargeDataSummaryItemRegistry")
	private SummaryItemRegistry dischargeDataSummaryItemRegistry;

	@Autowired
	@Qualifier("offenderProfileItemsProperties")
	private Properties offenderProfileItemsProperties;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate to manage offender summaries in a model.
	 * 
	 */
	public OffenderSummaryModelDelegate() {
		//this.summaryItemRegistry = summaryItemRegistry;
	}
	
	/* Model management methods. */
	
	/**
	 * Adds offender summary to model.
	 * 
	 * @param map model map
	 * @param offender offender
	 */
	public void add(final Map<String, Object> map, final Offender offender) {
		ModelMap modelMap = (ModelMap) map;
		Date currentDate = new Date();
		
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.basicInformationSummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.offenderFlagSummaryItemRegistry.getItems());
		/*this.buildSummaryItems(modelMap, offender, currentDate,
				this.placementSummaryItemRegistry.getItems());*/
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.legacyPlacementSummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.contactSummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.communitySupervisionSummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.facilitySummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.identificationSummaryItemRegistry.getItems());
		this.buildSummaryItems(modelMap, offender, currentDate,
				this.dischargeDataSummaryItemRegistry.getItems());
		
		/*modelMap.put(PLACEMENT_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.placementSummaryItemRegistry);*/
		modelMap.put(LEGACY_PLACEMENT_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.legacyPlacementSummaryItemRegistry);
		modelMap.put(OFFENDER_FLAG_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.offenderFlagSummaryItemRegistry);
		modelMap.put(FACILITY_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.facilitySummaryItemRegistry);
		modelMap.put(COMMUNITY_SUPERVISION_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.communitySupervisionSummaryItemRegistry);
		modelMap.put(CONTACT_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.contactSummaryItemRegistry);
		modelMap.put(IDENTIFICATION_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.identificationSummaryItemRegistry);
		modelMap.put(DISCHARGE_DATA_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.dischargeDataSummaryItemRegistry);
		modelMap.put(BASIC_INFORMATION_SUMMARY_ITEM_REGISTRY_MODEL_KEY,
				this.basicInformationSummaryItemRegistry);
		modelMap.put(OFFENDER_PROFILE_ITEMS_PROPERTIES_MODEL_KEY,
				this.offenderProfileItemsProperties);
	}
	
	/**
	 * @param modelMap
	 * @param offender
	 * @param currentDate
	 * @param summaryItems
	 */
	private void buildSummaryItems(final Map<String, Object> modelMap,
			final Offender offender,
			final Date currentDate,
			final List<SummaryItem> summaryItems) {
		Iterator<SummaryItem> summaryItemIterator = summaryItems.iterator();
		while (summaryItemIterator.hasNext()) {
			SummaryItem summaryItem = summaryItemIterator.next();
			if (summaryItem.getEnabled()) {
				summaryItem.build(modelMap, offender, currentDate);
			}
		}
	}
}