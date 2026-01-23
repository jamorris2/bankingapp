window.onload = function() {
    const params = new URLSearchParams(window.location.search);
    if (params.get('error') === 'insufficient_funds') {
        const popup = document.getElementById('errorPopup');
        if (popup) popup.style.display = 'flex';
    }
};

function closePopup() {
    const popup = document.getElementById('errorPopup');
    if (popup) popup.style.display = 'none';
    window.history.replaceState({}, document.title, window.location.pathname);
}