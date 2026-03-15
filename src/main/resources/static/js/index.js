/**
 * Used to generate date strings suitable for html input field of type date
 * @param date - the Date class that is used to get current date
 * @param n  - how many days to add to current date (used to set the max value of the input field)
 * Returns date in the form of "yyyy-mm-dd"
 */
function getDateString(date, n) {
	var day = String(date.getDate() + n).padStart(2, '0'); // String must be 2 characters long. Pad start with '0' to get "03" instead of "3" for example
	var month = String(date.getMonth() + 1).padStart(2, '0');
	var year = date.getFullYear();
	return `${year}-${month}-${day}`;
}
/**
 * Used to generate time strings suitable for html input field of type time
 * @param date - the Date class that is used to get current time
 * Returns time in the form of "hh:mm"
 */
function getTimeString(date) {
	var hours = String(date.getHours()).padStart(2, '0'); // String must be 2 characters long. Pad start with '0' when necessary (for example the time 01:05)
	var minutes = String(date.getMinutes()).padStart(2, '0');
	return `${hours}:${minutes}`;
}
/**
 * Used to check if form can be submitted.
 * Function checks that the start and end time differ by atleast 1 hour.
 * Alert popup is displayed by the browser that informs the user if form is not suitable.
 * Returns true if form is suitable. Returns false if form is not suitable
 */
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
		window.alert("Reservation must be atleast 1 hour long.");
		return false;
	}
	return true;
}

/**
 * Used to set rules to form input elements
 * Date can only be from today to +n days (specified at the start of the program)
 * Time can't be smaller than cuurent time
 */
document.addEventListener("DOMContentLoaded", function() {
		var date = new Date();
		var n = 2;

		var dateField = document.getElementById("dateField");
		var startTimeField = document.getElementById("startTime");
		var endTimeField = document.getElementById("endTime");

  		dateField.setAttribute("min", getDateString(date, 0));
		dateField.setAttribute("max", getDateString(date, n));

		startTimeField.setAttribute("min", getTimeString(date));
		endTimeField.setAttribute("min", getTimeString(date));
});

// Main logic for limiting the date selection is from:
// https://www.freecodecamp.org/news/javascript-get-current-date-todays-date-in-js/