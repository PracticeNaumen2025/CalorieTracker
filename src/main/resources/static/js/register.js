document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const errorDiv = document.getElementById('error-message');
    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const data = {
        username: this.username.value.trim(),
        email: this.email.value.trim(),
        password: this.password.value
    };

    try {
        const resp = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (resp.ok) {
            window.location.href = '/login?registered';
        } else {
            const errorText = await resp.text();
            errorDiv.textContent = errorText || 'Ошибка регистрации';
            errorDiv.style.display = 'block';
        }
    } catch (err) {
        errorDiv.textContent = 'Сервер недоступен. Попробуйте позже.';
        errorDiv.style.display = 'block';
    }
});
