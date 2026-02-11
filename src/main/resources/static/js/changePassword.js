document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('changePasswordForm');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');

    form.addEventListener('submit', function(event) {
        const val = password.value;
        let errorMessage = "";

        if (val.length < 6 || val.length > 20) {
            errorMessage = "Password must be between 6 and 20 characters.";
        }
        else if (!/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])/.test(val)) {
            errorMessage = "Password must include a digit, lowercase, uppercase, and special character (@#$%^&+=!).";
        }
        else if (val !== confirmPassword.value) {
            errorMessage = "Passwords do not match.";
        }

        if (errorMessage !== "") {
            event.preventDefault(); // Stop form submission
            alert(errorMessage);
            confirmPassword.value = "";
            password.focus();
        }
    });
});