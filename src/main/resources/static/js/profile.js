document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/profile', {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(async res => {
            if (!res.ok) {
                throw new Error('Ошибка загрузки профиля');
            }
            return res.json();
        })
        .then(data => {
            document.getElementById('username').textContent = data.username ?? 'не указано';
            document.getElementById('dob').textContent = data.dateOfBirth ?? 'не указано';
            document.getElementById('gender').textContent = data.gender
                ? (data.gender === 'MALE' ? 'Мужской' : 'Женский')
                : 'не указано';
            document.getElementById('height').textContent = data.heightCm ?? 'не указано';
            document.getElementById('weight').textContent = data.weightKg ?? 'не указано';
            document.getElementById('activity').textContent = data.activityLevel
                ? mapActivityLevel(data.activityLevel)
                : 'не указано';
        })
        .catch(err => {
            document.getElementById('profile-info').innerHTML =
                `<p class="text-red-500 font-medium">Не удалось загрузить данные профиля</p>`;
            console.error(err);
        });
});

function mapActivityLevel(level) {
    switch (level) {
        case 'LOW': return 'Низкий';
        case 'MEDIUM': return 'Средний';
        case 'HIGH': return 'Высокий';
        default: return level;
    }
}
