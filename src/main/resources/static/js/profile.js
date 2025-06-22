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


document.addEventListener('DOMContentLoaded', () => {
    // ...твой существующий fetch профиля...

    const editBtn = document.getElementById('editProfileBtn');
    const modal = document.getElementById('editProfileModal');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const cancelEditBtn = document.getElementById('cancelEditBtn');
    const form = document.getElementById('editProfileForm');

    function openModal() {
        // Заполнить форму текущими значениями
        form.dob.value = document.getElementById('dob').textContent !== 'не указано' ? document.getElementById('dob').textContent : '';
        form.gender.value = {
            'Мужской': 'MALE',
            'Женский': 'FEMALE'
        }[document.getElementById('gender').textContent] || '';
        form.height.value = document.getElementById('height').textContent !== 'не указано' ? document.getElementById('height').textContent : '';
        form.weight.value = document.getElementById('weight').textContent !== 'не указано' ? document.getElementById('weight').textContent : '';
        form.activity.value = {
            'Низкий': 'LOW',
            'Средний': 'MEDIUM',
            'Высокий': 'HIGH'
        }[document.getElementById('activity').textContent] || '';

        modal.classList.remove('hidden');
    }

    function closeModal() {
        modal.classList.add('hidden');
    }

    editBtn.addEventListener('click', openModal);
    closeModalBtn.addEventListener('click', closeModal);
    cancelEditBtn.addEventListener('click', closeModal);

    form.addEventListener('submit', async e => {
        e.preventDefault();

        const updateData = {};

        if (form.dob.value) updateData.dateOfBirth = form.dob.value;
        if (form.gender.value) updateData.gender = form.gender.value;
        if (form.height.value) updateData.heightCm = parseFloat(form.height.value);
        if (form.weight.value) updateData.weightKg = parseFloat(form.weight.value);
        if (form.activity.value) updateData.activityLevel = form.activity.value;

        try {
            const response = await fetch('/api/profile', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(updateData)
            });

            if (!response.ok) {
                throw new Error('Ошибка обновления профиля');
            }

            const updatedProfile = await response.json();

            // Обновляем данные на странице
            document.getElementById('dob').textContent = updatedProfile.dateOfBirth ?? 'не указано';
            document.getElementById('gender').textContent = updatedProfile.gender === 'MALE' ? 'Мужской' : 'Женский';
            document.getElementById('height').textContent = updatedProfile.heightCm ?? 'не указано';
            document.getElementById('weight').textContent = updatedProfile.weightKg ?? 'не указано';
            document.getElementById('activity').textContent = mapActivityLevel(updatedProfile.activityLevel) ?? 'не указано';

            closeModal();
        } catch (err) {
            alert(err.message);
            console.error(err);
        }
    });
});

