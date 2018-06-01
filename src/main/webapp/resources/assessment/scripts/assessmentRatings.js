window.onload = function() {
	var rows = document.getElementsByClassName('actionMenuItem');
	for(var i = 0; i < rows.length; i++){
		applyActionMenu(rows[i], function() {
			applyRemoveLinkConfirmation();
		});
	}
	if (graphData.length > 0) {
		var c=document.getElementById("assessmentGraph");
		var c2=document.getElementById("assessmentGraphScaled");
		
		var resolver = new common.MessageResolver("omis.assessment.msgs.assessmentRating");
		
		document.getElementById("toggleGraph").onclick = function(e){
			if (c.classList.contains("hiddenGraph")) {
				c.classList.remove("hiddenGraph");
				c2.classList.add("hiddenGraph");
				e.target.innerHTML = resolver.getMessage("togglePercentageLabel", null);
			} else {
				c.classList.add("hiddenGraph");
				c2.classList.remove("hiddenGraph");
				e.target.innerHTML = resolver.getMessage("toggleScoreLabel", null);
			}
		};
	
		document.getElementById("graphError").onclick = function(e){
			try {
				fillCanvas(c, graphData);
				fillCanvas(c2, graphData, true);
				document.getElementById("toggleGraph").classList.remove("hidden");
				document.getElementById("graphError").classList.add("hidden");
			} catch (err) {
				console.log("An error has occurred drawing canvas.")
			}
			
		}
		
		try{
			fillCanvas(c, graphData);
			fillCanvas(c2, graphData, true);
		} catch (e) {
			document.getElementById("toggleGraph").classList.add("hidden");
			document.getElementById("graphError").classList.remove("hidden");
		};
	}
}

function fillCanvas (c, data, scaled) {
	var ctx = c.getContext("2d")
	
	c.width = c.parentElement.clientWidth * .8;
	c.height = c.width;
	
	var xMargin = 20,
		yMargin = 30,
		maxX = c.width - xMargin,
		maxY = c.height - yMargin,
		minX = xMargin,
		minY = yMargin;
	ctx.beginPath();
	
	var maximumYValue = 0;
	if (!scaled) {
		for (var i = 0; i < data.length; i++) {
			for (var j = 0; j < data[i].ratings.length; j++) {
				if (maximumYValue < parseInt(data[i].ratings[j].max)) {
					maximumYValue = parseInt(data[i].ratings[j].max);
				}
			}
		}
	} else {
		maximumYValue = 100;
	}
	
//	Horizontal Axis
	ctx.moveTo(minX,maxY);
	ctx.lineTo(maxX,maxY);


//	Vertical Axis
	ctx.moveTo(minX,maxY);
	ctx.lineTo(minX,minY);
	ctx.stroke();
	
//	Min Ticks
	ctx.lineWidth = 0.25;
	var noOfGrids = 10;
	if (!scaled) {
		if (maximumYValue > 100 && maximumYValue % 20 == 0) {
			noOfGrids = maximumYValue/20;
		} else if (maximumYValue % 10 == 0) {
			noOfGrids = maximumYValue/10;
		} else if (maximumYValue % 5 == 0) {
			noOfGrids = maximumYValue/5;
		} else if (maximumYValue % 2 == 0) {
			noOfGrids = maximumYValue/2;
		}
	}
	
	
	var vGridDiff = (maxY - minY) / noOfGrids;
	ctx.save();
	for (var i = 1; i <= noOfGrids; i++) {
		ctx.moveTo(minX,maxY-i*vGridDiff);
		ctx.fillStyle = "rgba(0, 0, 0, .25)";
		ctx.lineTo(maxX,maxY-i*vGridDiff);
		ctx.font = "8px 'Arial'";
		ctx.fillStyle = "rgba(0, 0, 0, 1)";
		ctx.fillText((i*maximumYValue/noOfGrids), 5, maxY - i * vGridDiff);
	}
	ctx.stroke();
	ctx.restore();

//	Title
	ctx.textAlign = "center";
	ctx.fillText("Rankings", c.width / 2, 15);

//	Y Label
	var yAxisLabel;
	if (scaled) {
		yAxisLabel = "Percent";
	} else {
		yAxisLabel = "Score";
	}
	ctx.save();
	ctx.font="10px 'Arial'";
	ctx.translate(15, c.height);
	ctx.rotate(-Math.PI / 2);
	ctx.fillText(yAxisLabel,20,0);
	ctx.restore();

	var barWidth = (c.width - minX) / (data.length + 1) / 2,
		padding = ((c.width - minX) - (barWidth * data.length)) / (data.length + 1),
		maxHeight = Math.round((maxY - minY)*1.0),
		regions = [],
		regionIndex = 0;
	for (var i = 0; i < data.length; i++) {
		var maxScore = maximumYValue;
		if (scaled) {
			maxScore = 0;
			for (var j = 0; j < data[i].ratings.length; j++) {
				if (maxScore < parseInt(data[i].ratings[j].max)) {
					maxScore = parseInt(data[i].ratings[j].max);
				}
			}
		}
		var curTop = maxY,
			curHeight = 0,
			xBar = minX+(padding*(i+1))+(barWidth*i),
			fontSize = 10,
			scoreHeight = maxY - (parseInt(data[i].score) / maxScore * maxHeight),
			scoreRegion = {x:xBar-10, y:scoreHeight-10, w:barWidth+20, h:20},
			scoreColor = "gold",
			scoreTooltip = new ToolTip(c, scoreRegion,
				"Score: " + data[i].score,
				75, 3000, scoreColor);
		for (var j=0; j < data[i].ratings.length; j++) {
			var r = (255 * (j*10)),
				g = (100 - (j*40)) + 100,
				color = "rgba(" + r + ", " + g + ", 0, 1)",
				thisHeight = parseInt(data[i].ratings[j].max)/ maxScore * maxHeight;
			curTop = curTop - (thisHeight - curHeight);
			var region = {
						x: xBar,
						y: curTop,
						w: barWidth,
						h: (thisHeight - curHeight)
					};
			var tooltip = new ToolTip(c, region,
					"(" + data[i].ratings[j].rating + ") " + data[i].ratings[j].min + " - " + data[i].ratings[j].max,
					80, 1500, color, scoreRegion);
			ctx.fillStyle = color;
			ctx.lineWidth = .5;
			drawRect(ctx, region);
			ctx.stroke();
			ctx.fill();
			curHeight = thisHeight;
			regionIndex++;
		}
		
		//Score 
		ctx.fillStyle = "rgba(0, 0, 0, 1)"
			ctx.beginPath();
		ctx.lineWidth = 1.0;
		ctx.moveTo(xBar, scoreHeight);
		ctx.lineTo(xBar - 5, scoreHeight + 5);
		ctx.moveTo(xBar, scoreHeight);
		ctx.lineTo(xBar - 5, scoreHeight - 5);
		ctx.moveTo(xBar, scoreHeight);
		ctx.lineTo(xBar + barWidth, scoreHeight);
		ctx.lineTo(xBar + barWidth + 5, scoreHeight + 5);
		ctx.moveTo(xBar + barWidth, scoreHeight);
		ctx.lineTo(xBar + barWidth + 5, scoreHeight - 5);
		ctx.stroke();
		
		//Labels
		ctx.save();
		ctx.font = fontSize + "px 'Arial'";
		while (ctx.measureText(data[i].section).width >= (barWidth + padding) && fontSize > 8) {
				fontSize--;
				ctx.font = fontSize + "px 'Arial'";
		}
		var width = ctx.measureText(data[i].section).width;
		ctx.translate(xBar, maxY+5);
		ctx.textBaseline = 'middle';
		ctx.textAlign = "left";
		wrapText(ctx, data[i].section, 0, 0, (barWidth + padding), fontSize);
		ctx.restore();
	}
}

function drawRect(ctx, region) {
	ctx.beginPath();
	ctx.rect(region.x, region.y, region.w, region.h);
}

//By Character
function wrapText(context, text, x, y, maxWidth, lineHeight) {
	var words = text;
	var line = '';

	for(var n = 0; n < words.length; n++) {
		var testLine = line + words[n] + '';
		var metrics = context.measureText(testLine);
		var testWidth = metrics.width;
		if (testWidth > maxWidth && n > 0) {
			context.fillText(line, x, y);
			line = words[n] + '';
			y += lineHeight;
		}
		else {
			line = testLine;
		}
	}
	context.fillText(line, x, y);
}

//By word (proper word wrap)
//via https://www.html5canvastutorials.com/tutorials/html5-canvas-wrap-text-tutorial/
/*function wrapText(context, text, x, y, maxWidth, lineHeight) {
	var words = text.split(' ');
	var line = '';

	for(var n = 0; n < words.length; n++) {
		var testLine = line + words[n] + ' ';
		var metrics = context.measureText(testLine);
		var testWidth = metrics.width;
		if (testWidth > maxWidth && n > 0) {
			context.fillText(line, x, y);
			line = words[n] + ' ';
			y += lineHeight;
		}
		else {
			line = testLine;
		}
	}
	context.fillText(line, x, y);
}*/

//via https://stackoverflow.com/a/29490892, modified
//all hail the stackoverflow overlords. 
function ToolTip(canvas, region, text, width, timeout, color, excludingRegion) {
	
	var me = this,                                // self-reference for event handlers
		div = document.createElement("div"),      // the tool-tip div
		parent = canvas.parentNode,               // parent node for canvas
		visible = false;                          // current status
		
	// set some initial styles, can be replaced by class-name etc.
	div.style.cssText =
			"position: fixed;" +
			"padding: 7px;" +
			"border-style: solid;" +
			"border-color: black;" +
			"border-width: 1px;" +
			"background: " + color + ";" +
			"pointer-events: none;" +
			"width: " + width + "px";
	
	div.innerHTML = text;
	
	
	
	
	// show the tool-tip
	this.show = function(pos) {
		if (!visible && !canvas.classList.contains("hiddenGraph")) {// ignore if already shown (or reset time), only if the canvas is visible.
			visible = true;                           // lock so it's only shown once
			setDivPos(pos);                           // set position
			parent.appendChild(div);                  // add to parent of canvas
			//setTimeout(hide, timeout);                // timeout for hide
		}
	}

	// hide the tool-tip
	function hide() {
		visible = false;                            // hide it after timeout
		if (parent.contains(div)){
			parent.removeChild(div);                    // remove from DOM
		}
	}

	// check mouse position, add limits as wanted... just for example:
	function check(e) {
		var pos = getPos(e),
		posAbs = {x: e.clientX, y: e.clientY};  // div is fixed, so use clientX/Y
		
		if(!excludingRegion) {
			if (!visible &&
					pos.x >= region.x && pos.x < region.x + region.w &&
					pos.y >= region.y && pos.y < region.y + region.h) {
				me.show(posAbs);                          // show tool-tip at this pos
			}
			else if(pos.x >= region.x && pos.x < region.x + region.w &&
					pos.y >= region.y && pos.y < region.y + region.h) {
				setDivPos(posAbs);                     // otherwise, update position
			} else {
				hide();
			}
		} else {
			if (!visible &&
					pos.x >= region.x && pos.x < region.x + region.w &&
					pos.y >= region.y && pos.y < region.y + region.h &&
					!(pos.x >= excludingRegion.x && pos.x < excludingRegion.x + excludingRegion.w &&
					pos.y >= excludingRegion.y && pos.y < excludingRegion.y + excludingRegion.h)) {
				me.show(posAbs);                          // show tool-tip at this pos
			}
			else if(pos.x >= region.x && pos.x < region.x + region.w &&
					pos.y >= region.y && pos.y < region.y + region.h &&
					!(pos.x >= excludingRegion.x && pos.x < excludingRegion.x + excludingRegion.w &&
					pos.y >= excludingRegion.y && pos.y < excludingRegion.y + excludingRegion.h)) {
				setDivPos(posAbs);                     // otherwise, update position
			} else {
				hide();
			}
		}
	}

	// get mouse position relative to canvas
	function getPos(e) {
		var r = canvas.getBoundingClientRect();
		return {
			x: e.clientX - r.left,
			y: e.clientY - r.top}
	}

	// update and adjust div position if needed (anchor to a different corner etc.)
	function setDivPos(pos) {
		if (visible){
			if (pos.x < 0) pos.x = 0;
			if (pos.y < 0) pos.y = 0;
			// other bound checks here
			div.style.left = (pos.x + 10) + "px";
			div.style.top = pos.y + "px";
			if (pos.x + width > (canvas.offsetLeft + canvas.clientWidth)) {
				div.style.left = (pos.x - width - 15) + "px";
				div.style.top = pos.y + "px";
			} else {
				div.style.left = (pos.x + 10) + "px";
				div.style.top = pos.y + "px";
			}
		}
	}

	// we need to use shared event handlers:
	canvas.addEventListener("mousemove", check);
	canvas.addEventListener("click", check);
}