
function randomString() {
	return Math.random().toString(36).slice(2);
}

function randomColorString() {
	return "#"+Math.floor(Math.random()*16777215).toString(16);
}

function getDateString(milliseconds) {
	var x = new Date(milliseconds);
	return x.getFullYear() + '-' + x.getMonth() + '-' + x.getDate() + ' ' +
		   x.getHours() + ':' + x.getMinutes() + ':' + x.getSeconds() + '.' + x.getMilliseconds();
}