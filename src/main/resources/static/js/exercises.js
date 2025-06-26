// --- Работа только с занятиями пользователя ---

document.addEventListener('DOMContentLoaded', function() {
    const dateInput = document.getElementById('dateInput');
    const exerciseEntriesList = document.getElementById('exerciseEntriesList');
    const addEntryBtn = document.getElementById('addEntryBtn');
    const addEntryModal = document.getElementById('addEntryModal');
    const cancelAddEntryBtn = document.getElementById('cancelAddEntryBtn');
    const addEntryForm = document.getElementById('addEntryForm');
    const exerciseSearch = document.getElementById('exerciseSearch');
    const exerciseSearchResults = document.getElementById('exerciseSearchResults');

    if (dateInput && !dateInput.value) {
        const today = new Date().toISOString().slice(0, 10);
        dateInput.value = today;
    }

    // --- Редактирование записи ---
    let editMode = false;
    let editingEntryId = null;

    function openEditEntryModal(entry) {
        editMode = true;
        editingEntryId = entry.entryId;
        addEntryModal.classList.remove('hidden');
        addEntryModal.classList.add('flex');
        addEntryForm.reset();
        exerciseSearch.value = entry.exerciseName || '';
        exerciseSearch.dataset.selectedId = entry.exerciseId;
        document.getElementById('duration').value = entry.durationMinutes;
        document.getElementById('entryDateTime').value = entry.dateTime.slice(0, 16);
        exerciseSearchResults.innerHTML = '';
        exerciseSearchResults.classList.add('hidden');
    }

    function closeEntryModal() {
        editMode = false;
        editingEntryId = null;
        addEntryModal.classList.add('hidden');
        addEntryModal.classList.remove('flex');
    }

    async function loadExerciseEntries() {
        const date = dateInput.value;
        exerciseEntriesList.innerHTML = '';
        if (!date) {
            exerciseEntriesList.innerHTML = '<p class="text-red-600 font-medium text-center">Пожалуйста, выберите дату.</p>';
            return;
        }
        const start = date + 'T00:00:00';
        const end = date + 'T23:59:59';
        try {
            const res = await fetch(`/api/exercise/entry?start=${start}&end=${end}`);
            if (!res.ok) throw new Error('Ошибка при загрузке занятий');
            const entries = await res.json();
            if (!entries || entries.length === 0) {
                exerciseEntriesList.innerHTML = '<p class="text-gray-600 text-center">Нет занятий за выбранную дату.</p>';
                return;
            }
            entries.forEach(entry => {
                const card = document.createElement('div');
                card.className = 'bg-gray-50 border border-gray-200 rounded-xl p-6 shadow-sm flex flex-col md:flex-row md:items-center md:justify-between gap-4 hover:shadow-lg transition-shadow duration-300 animate-fade-in';
                const time = new Date(entry.dateTime).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' });
                card.innerHTML = `
                    <div>
                        <div class="text-lg font-bold text-primary mb-1" id="exname-${entry.entryId}">Загрузка...</div>
                        <div class="text-gray-600 text-sm mb-1">Время: <b>${time}</b></div>
                        <div class="text-gray-600 text-sm">Длительность: <b>${entry.durationMinutes}</b> мин</div>
                        <div class="text-gray-600 text-sm">Калории: <b>${entry.caloriesBurned}</b> ккал</div>
                    </div>
                    <div class="flex gap-2 mt-2 md:mt-0">
                        <button class="edit-entry-btn text-black hover:text-blue-800 p-1" title="Редактировать">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 -960 960 960"><path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/></svg>
                        </button>
                    </div>
                `;
                exerciseEntriesList.appendChild(card);
                fetch(`/api/exercises/${entry.exerciseId}`)
                    .then(r => r.ok ? r.json() : null)
                    .then(data => {
                        document.getElementById(`exname-${entry.entryId}`).textContent = data ? data.name : 'Неизвестно';
                        entry.exerciseName = data ? data.name : '';
                        card.querySelector('.edit-entry-btn').addEventListener('click', () => openEditEntryModal({...entry, exerciseName: entry.exerciseName}));
                    });
            });
        } catch (e) {
            exerciseEntriesList.innerHTML = `<p class="text-red-600 text-center">${e.message}</p>`;
        }
    }

    if (dateInput) {
        dateInput.addEventListener('change', loadExerciseEntries);
        loadExerciseEntries();
    }

    if (addEntryBtn) {
        addEntryBtn.addEventListener('click', () => {
            addEntryModal.classList.remove('hidden');
            addEntryModal.classList.add('flex');
            addEntryForm.reset();
            exerciseSearchResults.innerHTML = '';
            exerciseSearchResults.classList.add('hidden');
            exerciseSearch.dataset.selectedId = '';
        });
    }
    if (cancelAddEntryBtn) {
        cancelAddEntryBtn.addEventListener('click', closeEntryModal);
    }

    let entrySearchTimeout;
    if (exerciseSearch) {
        exerciseSearch.addEventListener('input', function(e) {
            clearTimeout(entrySearchTimeout);
            const query = e.target.value.trim();
            if (!query) {
                exerciseSearchResults.innerHTML = '';
                exerciseSearchResults.classList.add('hidden');
                return;
            }
            entrySearchTimeout = setTimeout(async () => {
                try {
                    const res = await fetch(`/api/exercises/search?name=${encodeURIComponent(query)}&page=0&size=10`);
                    if (!res.ok) throw new Error('Ошибка поиска');
                    const data = await res.json();
                    const results = data.content || [];
                    if (results.length === 0) {
                        exerciseSearchResults.innerHTML = '<div class="p-2 text-gray-500">Ничего не найдено</div>';
                        exerciseSearchResults.classList.remove('hidden');
                        return;
                    }
                    exerciseSearchResults.innerHTML = results.map(ex => `<div class="p-2 hover:bg-primary hover:text-white cursor-pointer" data-id="${ex.exerciseId}" data-name="${ex.name}">${ex.name} (${ex.caloriesPerHour} ккал/ч)</div>`).join('');
                    exerciseSearchResults.classList.remove('hidden');
                } catch (err) {
                    exerciseSearchResults.innerHTML = `<div class="p-2 text-red-500">${err.message}</div>`;
                    exerciseSearchResults.classList.remove('hidden');
                }
            }, 300);
        });
        exerciseSearchResults.addEventListener('click', function(e) {
            const target = e.target.closest('[data-id]');
            if (target) {
                exerciseSearch.value = target.dataset.name;
                exerciseSearch.dataset.selectedId = target.dataset.id;
                exerciseSearchResults.innerHTML = '';
                exerciseSearchResults.classList.add('hidden');
            }
        });
    }

    if (addEntryForm) {
        addEntryForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const exerciseId = exerciseSearch.dataset.selectedId;
            const duration = document.getElementById('duration').value;
            const dateTime = document.getElementById('entryDateTime').value;
            if (!exerciseId || !duration || !dateTime) {
                alert('Пожалуйста, выберите упражнение, укажите длительность и дату/время.');
                return;
            }
            const payload = {
                exerciseId: parseInt(exerciseId),
                durationMinutes: parseInt(duration),
                dateTime: dateTime
            };
            try {
                if (editMode && editingEntryId) {
                    payload.entryId = editingEntryId;
                    const res = await fetch('/api/exercise/entry', {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    if (!res.ok) {
                        const msg = await res.text();
                        throw new Error(msg || 'Ошибка при обновлении занятия');
                    }
                    alert('Занятие успешно обновлено!');
                } else {
                    const res = await fetch('/api/exercise/entry', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    if (!res.ok) {
                        const msg = await res.text();
                        throw new Error(msg || 'Ошибка при добавлении занятия');
                    }
                    alert('Занятие успешно добавлено!');
                }
                closeEntryModal();
                addEntryForm.reset();
                loadExerciseEntries();
            } catch (err) {
                alert(err.message);
            }
        });
    }

    const manageExercisesBtn = document.getElementById('manageExercisesBtn');
    if (manageExercisesBtn) {
        manageExercisesBtn.addEventListener('click', function() {
            window.location.href = '/manage_exercises';
        });
    }
}); 