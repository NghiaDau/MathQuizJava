function clearError(errorId) {
    var input = document.getElementById(errorId.replace('Error', ''));
    var errorSpan = document.getElementById(errorId);
    if (input.value.trim() === '') {
        errorSpan.innerText = '';
    }
}