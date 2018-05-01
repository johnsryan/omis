/** Offender AJAXified lookup.
 * version 1.0 (Apr 29, 2013)
 * By Ryan Johns
 * dependencies: JQuery (version 1.7), JQuery UI (version 1.8.21), 
 * configServer.js, MessageResolver.js, lookup.js
 *
 * usage: $(element).offenderSearch(); 
 * @Deprecated use applySearch(input, hidden, searchImplementation, [?options]) */

(function( $ ) {
	/** @Deprecated */
	$.fn.offenderSearch = function (options) {
		var element = this;
		
		resultArea = document.createElement('div');
		$(resultArea).addClass('menu');
		$(element).after(resultArea);
		$(resultArea).position({
			of:$(element),
			my:"left top",
			at:"left bottom"
		});
		$(resultArea).hide();
		
		
		/* Settings what the plug-in uses to operate.
		 * returnParams - name - the name of what to expect back from service.
		 *                label - what to label it.
		 * url - URL of service being called.
		 * searchParams - paramName - request parameter required by service.
		 *                label - what to display to the user for the input.
		 * findByLabel - label to display for the search dialog. 
		 * callBackFunction - function to perform set.
		 * callBackId - optional ID value for call back to use.
		 * callBackString - optional String for call back to use. */
		 settings = $.extend( {
			
			
			bundle : "omis.person.msgs.person",
			returnParams : [{name:'offenderNumber', label:'offenderNumber'},
			                {name:'firstName',label:'firstName'},
			                {name:'middleName',label:'middleName'},
			                {name:'lastName', label:'lastName'}],
			url : config.ServerConfig.getContextPath() 
					+ '/personSearch/searchOffenderByNonSpecified.html',
			searchParam : {label:'searchCriteria', 
				             paramName:'searchCriteria'},
			callBackString : ['firstName', 'lastName'],
			callBackID : 'personId',
			callBackFunction : function(json, element) {
				newTab(config.ServerConfig.getContextPath()+'/offender/profile.html?offender='+ json.personId);
			}, 
			resultArea : resultArea
		},	options);
		
		
		var lookUp = $(settings.resultArea).lookup(settings);
		
		searchFunction = function(e) {
			searchCriteria = $(element).val();
			
			url = settings.url + '?' + settings.searchParam.paramName + '=' + searchCriteria;
			
			 if (searchCriteria.length > 4) {
				clearTimeout(element.data('timer'));
				var wait = setTimeout(function() {
					lookUp.runLookup(url);
				},105);
				$(element).data('timer', wait);
			};
		};
		
		searchFunctionEnter = function(e) {
			alert(e.shiftKey);
			 if(e.which == 13 && !e.shiftKey) {
				 searchCriteria = $(element).val();
			
				 url = settings.url + '?' + settings.searchParam.paramName + '=' + searchCriteria;
			
				 lookUp.runLookup(url, true);
				 
				 $(element).focus();
				 return false;
			 	} 
		};

		focusFunction = function() { $(settings.resultArea).fadeIn(); };
		unFocusFunction = function() { $(settings.resultArea).fadeOut(); };
		
		$(element).keyup(searchFunction);
		$(element).keydown(searchFunctionEnter);
	};
})( jQuery );

