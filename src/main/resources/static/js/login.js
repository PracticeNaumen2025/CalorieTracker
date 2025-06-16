const urlParams = new URLSearchParams(window.location.search);
if (urlParams.get('error') === 'true') {
    document.getElementById('error-message').style.display = 'block';
}

if (urlParams.get('logout') === 'true') {
    document.getElementById('logout-message').style.display = 'block';
}

if (document.querySelectorAll) {
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('focus', function() {
            this.parentNode.style.transform = 'scale(1.02)';
        });
        input.addEventListener('blur', function() {
            this.parentNode.style.transform = 'scale(1)';
        });
    });
} 