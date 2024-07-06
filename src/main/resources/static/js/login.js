
function clearErrorAndParam(element) {
    var input = document.getElementById(element.id);
    let id = element.id;
    var errorSpan = document.getElementById('userError');

    if (input.value.trim() === '') {
        if (errorSpan) {
            errorSpan.parentNode.removeChild(errorSpan);
        }
        var urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('error')) {
            urlParams.delete('error');
            var newUrl = window.location.pathname + '?' + urlParams.toString();
            history.replaceState({}, document.title, newUrl);
        }
    }
}