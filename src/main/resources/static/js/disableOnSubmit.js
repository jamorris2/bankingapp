document.addEventListener('submit', function (event) {
    const form = event.target;
    const submitBtn = form.querySelector('button[type="submit"]');

    if (!submitBtn) return;

    setTimeout(() => {
        if (!event.defaultPrevented) {
            submitBtn.disabled = true;
            submitBtn.innerText = "Loading...";
        }
    }, 1);
});