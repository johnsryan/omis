/** OMIS jquery ui.search implementations.
 * @author Ryan Johns
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 25, 2014)
 * @since OMIS 3.0 */

if (typeof ui === 'undefined') {
	var ui = new Object();
}

if (typeof ui.search === 'undefined') {
/** Implementation for search UI.
 * @Author Ryan Johns
 * @version 0.1.0 (Feb 24, 2014)
 * @Since OMIS 3.0 */
ui.search = new function () {
	
	if (typeof this.Autocomplete === 'undefined') {
		this.Autocomplete = new Object();
	}
	
	/** User autocomplete search.
	 * @param input input control.
	 * @param target target element.
	 * @param options options. */
	this.Autocomplete.USER_SEARCH = function(input, target, targetLabel, current, clear, options) {
		var msg = new common.MessageResolver("omis.search.msgs.search");
		if (!$(input).hasClass("lookup")) {
			var settings = options;
			settings = $.extend({ 
				onSelect: function(event, ui) {
					$(target).val(ui.item.value);
					displaySelection(ui.item.label, input, targetLabel);
					return false;
				}}, options);
			$(input).autocomplete({
				autoFocus: true,
				minLength: 2,
				source: function(request, response) {
					$.ajax({
						url: config.ServerConfig.getContextPath() + "/personSearch/searchUsersByNonSpecified.json?searchCriteria="+request.term,
						dataType: "json",
						cache:false,
						success: function(data) {
							response($.map( data, function( item ) {
								return {
											label: item.lastName + ", " + item.firstName + " (" + item.username + ")",
											value: item.personId
								 		};}));}});},select: settings.onSelect });
			$(input).addClass("lookup");
			
			if (typeof current != 'undefined') {
				$(current).on("click", function() {
					clearFields($(target), $(input), $(targetLabel));
				
					$.ajax({
						url: config.ServerConfig.getContextPath() + "/personSearch/currentUserAssignment.json",
						dataType: "json",
						cache:false,
						success: function(data) {
							if (data.personId != null && data.personId !== undefined) {
								$(target).val(data.personId);
								displaySelection(data.lastName + ", " + data.firstName + " (" + data.username + ")", input, targetLabel);
							} else {
								displaySelection(msg.getMessage("noResults"), input, targetLabel);
							}},
						error: function() {
							displaySelection(msg.getMessage("noResults"), input, targetLabel);
						}
					});
					return false;
			});}
			
			if (typeof clear != 'undefined') {
				$(clear).on("click", function() {
					clearFields($(target), $(input), $(targetLabel));
					return false;
			});}
			
			return this;
		}};
		
		/** User autocomplete search.
		 * @param input input control.
		 * @param target target element.
		 * @param options options. */
		this.Autocomplete.USERID_SEARCH = function(input, target, targetLabel, current, clear, options) {
			var msg = new common.MessageResolver("omis.search.msgs.search");
			if (!$(input).hasClass("lookup")) {
				var settings = options;
				settings = $.extend({ 
					onSelect: function(event, ui) {
						$(target).val(ui.item.value);
						displaySelection(ui.item.label, input, targetLabel);
						return false;
					}}, options);
				$(input).autocomplete({
					autoFocus: true,
					minLength: 4,
					source: function(request, response) {
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/personSearch/searchUsersByNonSpecified.json?searchCriteria="+request.term,
							dataType: "json",
							cache:false,
							success: function(data) {
								response($.map( data, function( item ) {
									return {
												label: item.lastName + ", " + item.firstName + " (" + item.username + ")",
												value: item.userId
									 		};}));}});},select: settings.onSelect });
				$(input).addClass("lookup");
				
				if (typeof current != 'undefined') {
					$(current).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
					
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/personSearch/currentUserAssignment.json",
							dataType: "json",
							cache:false,
							success: function(data) {
								if (data.userId != null && data.userId !== undefined) {
									$(target).val(data.userId);
									displaySelection(data.lastName + ", " + data.firstName + " (" + data.username + ")", input, targetLabel);
								} else {
									displaySelection(msg.getMessage("noResults"), input, targetLabel);
								}},
							error: function() {
								displaySelection(msg.getMessage("noResults"), input, targetLabel);
							}
						});
						return false;
				});}
				
				if (typeof clear != 'undefined') {
					$(clear).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
						return false;
				});}
				
				return this;
			}};
		
		/** Person autocomplete search.
		 * @param input input control.
		 * @param target target element.
		 * @param options options. */
		this.Autocomplete.PERSON_SEARCH = function(input, target,targetLabel, clear, options) {
			if (!$(input).hasClass("lookup")) {
				var settings = options;
				settings = $.extend({ 
					onSelect: function(event, ui) {
						$(target).val(ui.item.value);
						displaySelection(ui.item.label, input, targetLabel);
						return false;
					}}, options);
				$(input).autocomplete({
					autoFocus: true,
					minLength: 2,
					source: function(request, response) {
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/personSearch/searchByNonSpecified.json?searchCriteria="+request.term,
							dataType: "json",
							cache:false,
							success: function(data) {
								response($.map( data, function( item ) {
									return {
												label: item.lastName + ", " + item.firstName,
												value: item.personId
									 		};}));},
							error: function() {
								displaySelection(msg.getMessage("noResults"), input, targetLabel);
							}});},select: settings.onSelect });
				$(input).addClass("lookup");
				
				if (typeof clear != 'undefined') {
					$(clear).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
						return false;
				});}
				return this;
			}};
		
		/** User autocomplete search.
		 * @param input input control.
		 * @param target target element.
		 * @param options options. */
		this.Autocomplete.STAFF_SEARCH = function(input, target, targetLabel, current, clear, options) {
			var msg = new common.MessageResolver("omis.search.msgs.search");
			
			if (!$(input).hasClass("lookup")) {
				var settings = options;
				settings = $.extend({ 
					onSelect: function(event, ui) {
						$(target).val(ui.item.value);
						displaySelection(ui.item.label, input, targetLabel);
						return false;
					}}, options);
				$(input).autocomplete({
					autoFocus: true,
					minLength: 2,
					source: function(request, response) {
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/staffSearch/searchByNonSpecified.json?searchCriteria="+request.term,
							dataType: "json",
							cache:false,
							success: function(data) {
								response($.map( data, function( item ) {
									return {
												label: item.lastName + ", " + item.firstName + " "+ item.titleName,
												value: item.personId
									 		};}));},
							error: function() {
								displaySelection(msg.getMessage("noResults"), input, targetLabel);
							}});},
					select: settings.onSelect });
				$(input).addClass("lookup");
				if (current != null && current !== undefined) {
					$(current).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/staffSearch/currentStaffAssignment.json",
							dataType: "json",
							cache:false,
							success: function(data) {
								if (data != null && data.personId != null && data.personId !== undefined) {
									$(target).val(data.personId);
									displaySelection(data.lastName + "," + data.firstName + " " + data.titleName, input, targetLabel);
								} else {
									displaySelection(msg.getMessage("noResults"), input, targetLabel);
								}},
								error: function() {
									displaySelection(msg.getMessage("noResults"));							
								}
						});
						return false;
				});}
				
				if (typeof clear != 'undefined') {
					$(clear).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
						return false;
				});}
				return this;
			}};
		
		
	/** Offender autocomplete search.
	 * @param input input element.
	 * @param target target element.
	 * @param options options. */
	this.Autocomplete.OFFENDER_SEARCH = function(input, target, targetLabel, clear, onselect, options) {
		if (!$(input).hasClass("lookup")) {
			var settings = options;
			settings = $.extend({ 
				onSelect: function(event, ui) {
					$(target).val(ui.item.value);
					displaySelection(ui.item.label, input, targetLabel);
					var func = (typeof onselect == 'function') ?
			        onselect : new Function(onselect);
					func();
					return false;
				}}, options);
			
			$(input).addClass("offender_autocomplete");
			
			$(input).autocomplete({
				autoFocus: true,
				minLength: 2,
				source: function(request, response) {
				$.ajax({
					url: config.ServerConfig.getContextPath() + "/personSearch/searchOffenderByNonSpecified.json?searchCriteria="+request.term,
					dataType: "json",
					cache:"false",
					success: function(data) {
						 response($.map( data, function( item ) {
								 return {
									 		label: item.lastName + ", " + item.firstName + " "+ (item.middleName != null ? item.middleName : "") + " ("+item.offenderNumber+")",
									 		value: item.personId
									 	};}));}});},select: settings.onSelect });
			$(input).addClass("lookup");
			
			if (typeof clear != 'undefined') {
				$(clear).on("click", function() {
					clearFields($(target), $(input), $(targetLabel));
					return false;
			});}
			
			return this;
		}};	
	
		this.Autocomplete.EMPLOYER_SEARCH = function(input, target,targetLabel, clear, options) {
			var msg = new common.MessageResolver("omis.search.msgs.search");
			if (!$(input).hasClass("lookup")) {
				var settings = options;
				settings = $.extend({ 
					onSelect: function(event, ui) {
						$(target).val(ui.item.value);
						displaySelection(ui.item.label, input, targetLabel);
						return false;
					}}, options);
				$(input).autocomplete({
					autoFocus: true,
					minLength: 4,
					source: function(request, response) {
						$.ajax({
							url: config.ServerConfig.getContextPath() + "/employerSearch/searchByNonSpecified.json?searchCriteria="+request.term,
							dataType: "json",
							cache:false,
							success: function(data) {
								response($.map( data, function( item ) {
									return {
												label: item.employerName + ", " + item.streetNumber + " " + item.streetName + ", " + item.cityName + " " + item.stateName,
												value: item.employerId
									 		};}));},
							error: function() {
								displaySelection(msg.getMessage("noResults"), input, targetLabel);
							}});},select: settings.onSelect });
				$(input).addClass("lookup");
				
				if (typeof clear != 'undefined') {
					$(clear).on("click", function() {
						clearFields($(target), $(input), $(targetLabel));
						return false;
				});}
				return this;
			}};
			
			this.Autocomplete.LOCATION_SEARCH = function(input, target,targetLabel, clear, options) {
				var msg = new common.MessageResolver("omis.search.msgs.search");
				if (!$(input).hasClass("lookup")) {
					var settings = options;
					settings = $.extend({ 
						onSelect: function(event, ui) {
							$(target).val(ui.item.value);
							displaySelection(ui.item.label, input, targetLabel);
							return false;
						}}, options);
					$(input).autocomplete({
						autoFocus: true,
						minLength: 4,
						source: function(request, response) {
							$.ajax({
								url: config.ServerConfig.getContextPath() + "/location/search/searchByUnspecified.json?searchCriteria="+request.term,
								dataType: "json",
								cache:false,
								success: function(data) {
									response($.map( data, function( item ) {
										return {
													label: item.organizationName + " (" + item.streetNumber + " " + item.streetName + " " + item.streetSuffix+". " + item.cityName + " " + item.stateName +")",
													value: item.locationId
										 		};}));},
								error: function() {
									displaySelection(msg.getMessage("noResults"), input, targetLabel);
								}});},select: settings.onSelect });
					$(input).addClass("lookup");
					
					if (typeof clear != 'undefined') {
						$(clear).on("click", function() {
							clearFields($(target), $(input), $(targetLabel));
							return false;
					});}
					return this;
				}};
				
			this.Autocomplete.STAFF_ASSIGNMENT_SEARCH = function(input, target, targetLabel, current, clear, options) {
					var msg = new common.MessageResolver("omis.search.msgs.search");
					
					if (!$(input).hasClass("lookup")) {
						var settings = options;
						settings = $.extend({ 
							onSelect: function(event, ui) {
								$(target).val(ui.item.value);
								displaySelection(ui.item.label, input, targetLabel);
								return false;
							}}, options);
						$(input).autocomplete({
							autoFocus: true,
							minLength: 2,
							source: function(request, response) {
								$.ajax({
									url: config.ServerConfig.getContextPath() + "/staffSearch/searchByNonSpecified.json?searchCriteria="+request.term,
									dataType: "json",
									cache:false,
									success: function(data) {
										response($.map( data, function( item ) {
											return {
														label: item.lastName + ", " + item.firstName + " "+ item.titleName,
														value: item.staffId
											 		};}));},
									error: function() {
										displaySelection(msg.getMessage("noResults"), input, targetLabel);
									}});},
							select: settings.onSelect });
						$(input).addClass("lookup");
						if (current != null && current !== undefined) {
							$(current).on("click", function() {
								clearFields($(target), $(input), $(targetLabel));
								$.ajax({
									url: config.ServerConfig.getContextPath() + "/staffSearch/currentStaffAssignment.json",
									dataType: "json",
									cache:false,
									success: function(data) {
										if (data != null && data.personId != null && data.personId !== undefined) {
											$(target).val(data.staffId);
											displaySelection(data.lastName + "," + data.firstName + " " + data.titleName, input, targetLabel);
										} else {
											displaySelection(msg.getMessage("noResults"), input, targetLabel);
										}},
										error: function() {
											displaySelection(msg.getMessage("noResults"));							
										}
								});
								return false;
						});}
						
						if (typeof clear != 'undefined') {
							$(clear).on("click", function() {
								clearFields($(target), $(input), $(targetLabel));
								return false;
						});}
						return this;
					}};
		
		
if (typeof this.AjaxList === 'undefined') {
		this.AjaxList = new Object();
}
		
this.AjaxList.OFFENDER_SEARCH = function(input, target, x, y, onLoad) {
	$(input).on("keyup", keyPressOffenderSearch(target, onLoad));
	$(input).on("paste", pasteOffenderSearch(target, onLoad));
};
};}

function keyPressOffenderSearch (target, onLoad) {
	var msg = new common.MessageResolver("omis.search.msgs.search");
	return function(event) {
			var input = event.target;
			if (input.value.length > 3) {
				if(event.which != 13 && event.keyCode != 13) {
					clearTimeout($.data(input, 'timer'));	
					var wait = setTimeout(offenderSearchAjax(input, target, onLoad), 250);
					$(this).data('timer', wait);
				}
				if (event.which == 13) {
					var a = $(target).find("ul.ajaxList li:first div a.offenderProfileLink");
					if (a.length > 0) {
						//a[0].focus();
						a[0].click();
						a[0].blur();
						return false;
					} else {
						return false;
					}
				}
			}
			return false;
		}
	}


function pasteOffenderSearch(target, onLoad) {
	return function(event) {
		setTimeout(offenderSearchAjax(event.target, target, onLoad), 40);
	}
}

function offenderSearchAjax(input, target, onLoad) {
	var msg = new common.MessageResolver("omis.search.msgs.search");
	return function() {
		$.ajax({
			url: config.ServerConfig.getContextPath() + "/personSearch/loadOffender.html?searchCriteria="+$(input).val(),
			cache:false,
		}).success(function(html){
			if ($(html).find("li").length <= 0) {
				$(target).html("<div class=\"noResults\">"+msg.getMessage("noResults")+"</div>");
			} else {
			
				$(target).html(html);
				loadOffenderProfilePhotosOnDemand(target);
				if (typeof onLoad === 'function') {
					onLoad(target);
				}
			}
		}).error(function() {
			displaySelection(msg.getMessage("noResults"));							
		});
	}
}




function clearFields(input1, input2, label) {
	input1.val("");
	input2.val("");
	label.html("");
}

function loadOffenderProfilePhotosOnDemand(viewer) {
	$(viewer).find(".profileImage").slice(0,5).each(function() { loadImageResources(this);});
	$(viewer).on("scroll", function() {
		if ($(this).data('offenderSearchScroll')) {
			clearTimeout($(this).data('offenderSearchScroll'));
		}
		$(this).data('offenderSearchScroll',setTimeout(function() {
			var viewables = findViewablesInView(this, $(".viewable"));
			$(viewables).each(function() { loadImageResources(this);});
		}, 250));
	});
}

function displaySelection(selectionText, input, targetLabel) {
	if (typeof targetLabel == 'undefined' || targetLabel == null) {
		$(input).val(selectionText);
	} else {
		$(input).val("");
		$(targetLabel).html(selectionText);
	}
}

function loadImageResources(element) {
	var mElement = $(element);
	var src = mElement.attr('data-src');
	mElement.attr('src', src);
	mElement.removeClass('viewable');

}

function findViewablesInView(viewer, viewables) {
	var x = 0;
	var mViewables = $(viewables);
	var current = mViewables.get(x);
	var newViewables = [];
	while (x < mViewables.size() && $(current).position().top < $(viewer).outerHeight() + $(viewer).scrollTop()) {
		if ($(current).position().top >= $(viewer).scrollTop()) {		
			newViewables.push(current);
		}
		x++;
		current = mViewables.get(x);
	}
	return newViewables;
}