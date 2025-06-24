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
            window.currentUserId = data.userId;

            const avatarImg = document.querySelector('img[alt="Аватар пользователя"]');
            if (data.photoUrl && data.photoUrl.trim() !== '') {
                avatarImg.src = data.photoUrl;
            } else {
                avatarImg.src = '/img/gigaChad.jpeg';
            }

            document.getElementById('username').textContent = data.username ?? 'не указано';
            document.getElementById('dob').textContent = formatDate(data.dateOfBirth) ?? 'не указано';
            document.getElementById('gender').textContent = data.gender
                ? (data.gender === 'MALE' ? 'Мужской' : 'Женский')
                : 'не указано';
            document.getElementById('height').textContent = data.heightCm ?? 'не указано';
            document.getElementById('weight').textContent = data.weightKg ?? 'не указано';
            document.getElementById('activity').textContent = data.activityLevel
                ? mapActivityLevel(data.activityLevel)
                : 'не указано';

            loadGoalsByUserId(data.userId);
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


// Функция форматирования даты из "YYYY-MM-DD" в "ДД.MM.ГГГГ"
function formatDate(dateStr) {
    if (!dateStr) return 'не указана';
    const [year, month, day] = dateStr.split('-');
    return `${day}.${month}.${year}`;
}

// Функция для форматирования процентов с одним знаком после запятой
function formatPercent(value) {
    return value != null ? value.toFixed(1) + '%' : 'не указано';
}

// Функция для форматирования веса и калорий (с одним знаком после запятой)
function formatNumber(value) {
    return value != null ? value.toFixed(1) : 'не указано';
}

// Загрузка текущей цели на сегодня
loadActiveGoal();

// Загрузка всех целей
function loadGoalsByUserId(userId) {
    fetch(`/api/goals/${userId}?page=0&size=2`, {
        headers: { 'Accept': 'application/json' }
    })
        .then(res => res.json())
        .then(pageResponse => {
            const goals = pageResponse.content;

            if (!goals || goals.length === 0) {
                document.getElementById('all-goals').innerHTML = '<p>Цели не найдены</p>';
                return;
            }

            document.getElementById('all-goals').innerHTML = goals.map(goal => `
            <div class="border border-gray-200 p-4 rounded-lg shadow-sm relative">
                <div class="flex justify-between">
                    <div>
                        <p><strong>Период:</strong> ${formatDate(goal.startDate)} — ${formatDate(goal.endDate)}</p>
                        <p><strong>Целевой вес:</strong> ${formatNumber(goal.targetWeightKg)} кг</p>
                        <p><strong>Калории:</strong> ${formatNumber(goal.dailyCalorieGoal)} ккал</p>
                        <p><strong>БЖУ:</strong> Б ${formatPercent(goal.proteinPercentage)}, Ж ${formatPercent(goal.fatPercentage)}, У ${formatPercent(goal.carbPercentage)}</p>
                    </div>
                    <div class="flex flex-col space-y-2 items-end">
                        <button class="text-black hover:text-red-600 p-1 delete-goal-btn" 
                                title="Удалить" data-goal-id="${goal.goalId}">
                            <svg class="w-5 h-5" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                <path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        `).join('');
        })
        .catch(err => {
            document.getElementById('all-goals').innerHTML =
                `<p class="text-red-500 font-medium">Ошибка загрузки целей</p>`;
            console.error(err);
        });
}


document.addEventListener('DOMContentLoaded', () => {
    const addGoalModal = document.getElementById('addGoalModal');
    const openAddGoalBtn = document.getElementById('openAddGoalBtn');
    const closeAddGoalModalBtn = document.getElementById('closeAddGoalModalBtn');
    const cancelAddGoalBtn = document.getElementById('cancelAddGoalBtn');
    const addGoalForm = document.getElementById('addGoalForm');

    // Открыть модалку
    openAddGoalBtn.addEventListener('click', () => {
        addGoalModal.classList.remove('hidden');
    });

    // Закрыть модалку
    function closeModal() {
        addGoalModal.classList.add('hidden');
        addGoalForm.reset();
    }
    closeAddGoalModalBtn.addEventListener('click', closeModal);
    cancelAddGoalBtn.addEventListener('click', closeModal);

    // Обработка отправки формы
    addGoalForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Получаем данные из формы
        const formData = new FormData(addGoalForm);
        const goalData = Object.fromEntries(formData.entries());

        // Преобразуем проценты и числа
        goalData.startDate = goalData.startDate || null;
        goalData.endDate = goalData.endDate || null;
        goalData.targetWeightKg = parseFloat(goalData.targetWeightKg);
        goalData.dailyCalorieGoal = parseInt(goalData.dailyCalorieGoal);
        goalData.proteinPercentage = parseFloat(goalData.proteinPercentage);
        goalData.fatPercentage = parseFloat(goalData.fatPercentage);
        goalData.carbPercentage = parseFloat(goalData.carbPercentage);

        goalData.userId = window.currentUserId || 0;

        // Валидация сумм БЖУ
        const sumBju = goalData.proteinPercentage + goalData.fatPercentage + goalData.carbPercentage;
        if (sumBju !== 100) {
            alert('Сумма процентов белков, жиров и углеводов должна быть равна 100%');
            return;
        }

        // Отправляем POST-запрос
        try {
            const res = await fetch('/api/goals', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(goalData),
            });
            if (!res.ok) {
                throw new Error('Ошибка при сохранении цели');
            }
            const savedGoal = await res.json();
            closeModal();
            loadGoalsByUserId(window.currentUserId);
            loadActiveGoal();
            alert('Цель успешно добавлена!');
        } catch (error) {
            alert(error.message);
            console.error(error);
        }
    });
});

document.addEventListener('click', e => {
    if (e.target.closest('.delete-goal-btn')) {
        const goalId = e.target.closest('.delete-goal-btn').dataset.goalId;

        if (confirm('Вы действительно хотите удалить эту цель?')) {
            fetch(`/api/goals/${goalId}`, {
                method: 'DELETE'
            })
                .then(res => {
                    if (!res.ok) throw new Error('Ошибка при удалении цели');
                    loadGoalsByUserId(window.currentUserId);
                    loadActiveGoal();
                })
                .catch(err => {
                    alert(err.message);
                    console.error(err);
                });
        }
    }
});

function loadActiveGoal() {
    fetch('/api/goals/active?date=' + new Date().toISOString().split('T')[0], {
        headers: { 'Accept': 'application/json' }
    })
        .then(res => res.ok ? res.json() : Promise.reject('Нет активной цели'))
        .then(goal => {
            document.getElementById('current-goal').innerHTML = `
                <p><strong>Целевая дата:</strong> с ${formatDate(goal.startDate)} по ${formatDate(goal.endDate)}</p>
                <p><strong>Целевой вес:</strong> ${formatNumber(goal.targetWeightKg)} кг</p>
                <p><strong>Цель калорий:</strong> ${formatNumber(goal.dailyCalorieGoal)} ккал</p>
                <p><strong>БЖУ (%):</strong> Б ${formatPercent(goal.proteinPercentage)}, Ж ${formatPercent(goal.fatPercentage)}, У ${formatPercent(goal.carbPercentage)}</p>
            `;
        })
        .catch(() => {
            document.getElementById('current-goal').innerHTML =
                `<p class="text-red-500 font-medium">Нет текущей цели</p>`;
        });
}



document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/measurements/me')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.date);
            const weightData = data.map(item => item.weightKg);
            const heightData = data.map(item => item.heightCm);

            const ctx = document.getElementById('measurementsChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Вес (кг)',
                            data: weightData,
                            borderColor: 'rgb(75, 192, 192)',
                            fill: false,
                            tension: 0.1
                        },
                        {
                            label: 'Рост (см)',
                            data: heightData,
                            borderColor: 'rgb(255, 159, 64)',
                            fill: false,
                            tension: 0.1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    interaction: {
                        mode: 'index',
                        intersect: false,
                    },
                    stacked: false,
                    plugins: {
                        title: {
                            display: true,
                            text: 'Динамика роста и веса'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Ошибка при загрузке измерений:', error);
        });
});


document.getElementById('view-all-goals-btn').addEventListener('click', () => {
    const overlay = document.getElementById('allGoalsOverlay');
    overlay.classList.remove('hidden');

    const userId = window.currentUserId

    fetch(`/api/goals/${userId}?page=0&size=100`, {
        headers: { 'Accept': 'application/json' }
    })
        .then(res => res.json())
        .then(pageResponse => {
            const goals = pageResponse.content;
            const container = document.getElementById('allGoalsContent');
            if (goals.length === 0) {
                container.innerHTML = '<p>Цели не найдены</p>';
                return;
            }
            container.innerHTML = goals.map(goal => `
        <div class="border border-gray-200 p-4 rounded-lg shadow-sm">
          <p><strong>Период:</strong> ${goal.startDate} — ${goal.endDate}</p>
          <p><strong>Целевой вес:</strong> ${goal.targetWeightKg} кг</p>
          <p><strong>Калории:</strong> ${goal.dailyCalorieGoal} ккал</p>
          <p><strong>БЖУ:</strong> Б ${goal.proteinPercentage}%, Ж ${goal.fatPercentage}%, У ${goal.carbPercentage}%</p>
        </div>
      `).join('');
        })
        .catch(err => {
            document.getElementById('allGoalsContent').innerHTML =
                '<p class="text-red-500 font-medium">Ошибка загрузки целей</p>';
            console.error(err);
        });
});

// Закрытие оверлея
document.getElementById('closeAllGoalsBtn').addEventListener('click', () => {
    document.getElementById('allGoalsOverlay').classList.add('hidden');
})

document.getElementById('avatarUpload').addEventListener('change', async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/api/profile/photo', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Ошибка загрузки фото');
        }

        // После успешной загрузки фото делаем fetch профиля заново
        const profileResponse = await fetch('/api/profile', {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!profileResponse.ok) {
            throw new Error('Ошибка загрузки профиля');
        }

        const data = await profileResponse.json();

        // Обновляем UI:
        const avatarImg = document.querySelector('img[alt="Аватар пользователя"]');
        if (data.photoUrl && data.photoUrl.trim() !== '') {
            avatarImg.src = data.photoUrl;
        } else {
            avatarImg.src = '/img/gigaChad.jpeg';
        }

        document.getElementById('username').textContent = data.username ?? 'не указано';
        document.getElementById('dob').textContent = formatDate(data.dateOfBirth) ?? 'не указано';
        document.getElementById('gender').textContent = data.gender
            ? (data.gender === 'MALE' ? 'Мужской' : 'Женский')
            : 'не указано';
        document.getElementById('height').textContent = data.heightCm ?? 'не указано';
        document.getElementById('weight').textContent = data.weightKg ?? 'не указано';
        document.getElementById('activity').textContent = data.activityLevel
            ? mapActivityLevel(data.activityLevel)
            : 'не указано';

        loadGoalsByUserId(data.userId);

    } catch (error) {
        alert(error.message);
        console.error(error);
    }
});


