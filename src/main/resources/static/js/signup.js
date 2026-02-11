document.getElementById('signupForm').onsubmit = function(event) {
    const password = document.getElementById('password').value;
    const confirm = document.getElementById('confirmPassword').value;
    let errorMessage = "";

    if (password.length < 6 || password.length > 20) {
        errorMessage = "Password must be between 6 and 20 characters.";
    }
    else if (!/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])/.test(password)) {
        errorMessage = "Password must include a digit, lowercase, uppercase, and special character (@#$%^&+=!).";
    }
    else if (password !== confirm) {
        errorMessage = "Passwords do not match!";
    }

    if (errorMessage !== "") {
        alert(errorMessage);
        document.getElementById('password').focus();
        return false;
    }

    return true;
};