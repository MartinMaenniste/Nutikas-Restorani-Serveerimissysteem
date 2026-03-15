function getDateString(date, n) {
	var day = String(date.getDate() + n).padStart(2, '0');
	var month = String(date.getMonth() + 1).padStart(2, '0');
	var year = date.getFullYear();
	return `${year}-${month}-${day}`;
}
function getTimeString(date) {
	var hours = String(date.getHours()).padStart(2, '0');
	var minutes = String(date.getMinutes()).padStart(2, '0');
	return `${hours}:${minutes}`;
}

function checkReservationLength() {
	var errorMessageDiv = document.getElementById("errorMessage");
	var startField = document.getElementById("startTime");
	var endField = document.getElementById("endTime");

	// Convert values to minutes
	var startValues	= startField.value.split(':');
	var endValues = endField.value.split(':');

	var start = Number(startValues[0]) * 60 + Number(startValues[1]);
	var end = Number(endValues[0]) * 60 + Number(endValues[1]);

	if (end - start < 60) {
		window.alert("Reservation must be atleast 1 hour long!");
		return false;
	}
	return true;
}


document.addEventListener("DOMContentLoaded", function() {
		var date = new Date();

		var dateField = document.getElementById("dateField");
		var startTimeField = document.getElementById("startTime");
		var endTimeField = document.getElementById("endTime");

  		dateField.setAttribute("min", getDateString(date, 0));
		dateField.setAttribute("max", getDateString(date, 2)); // Can only reserve max 2 days into the future

		startTimeField.setAttribute("min", getTimeString(date));
		endTimeField.setAttribute("min", getTimeString(date));
});

// Main logic for limiting the date selection is from:
// https://www.freecodecamp.org/news/javascript-get-current-date-todays-date-in-js/