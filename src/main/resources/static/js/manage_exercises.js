// ... весь код управления упражнениями из старого exercises.js ...
// ... весь код управления упражнениями из старого exercises.js ...
// (копировать весь функционал поиска, добавления, редактирования, удаления упражнений, пагинации)

document.addEventListener('DOMContentLoaded', function() {
    const exerciseList = document.getElementById('exerciseList');
    const searchInput = document.getElementById('searchInput');
    const addExerciseBtn = document.getElementById('addExerciseBtn');
    const exerciseModal = document.getElementById('exerciseModal');
    const cancelBtn = document.getElementById('cancelBtn');
    const exerciseForm = document.getElementById('exerciseForm');
    const modalTitle = document.getElementById('modalTitle');
    const pagination = document.getElementById('pagination');

    const API_URL = '/api/exercises';
    let currentPage = 0;
    let pageSize = 9;
    let totalPages = 1;
    let lastSearch = '';
    let isAdmin = false;
    let currentUserId = null;

    // Получаем информацию о пользователе (для отображения кнопок редактирования/удаления)
    Promise.all([
        fetch('/api/user/is_admin').then(r => r.ok ? r.json() : false),
        fetch('/api/user/current').then(r => r.ok ? r.json() : null)
    ]).then(([isAdminFlag, userId]) => {
        isAdmin = isAdminFlag;
        currentUserId = userId;
        fetchExercises();
    });

    // Отображение упражнений
    const renderExercises = (exercises) => {
        exerciseList.innerHTML = '';
        if (!exercises || exercises.length === 0) {
            exerciseList.innerHTML = `<p class="text-gray-500 col-span-full text-center">Упражнения не найдены.</p>`;
            return;
        }
        exercises.forEach(exercise => {
            const card = document.createElement('div');
            card.className = 'bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow duration-300';
            const canModify = isAdmin || (exercise.userId && exercise.userId === currentUserId);
            const headerHTML = `
                <div class="flex justify-between items-start gap-4 mb-2">
                    <h3 class="text-xl font-bold text-gray-800 break-words line-clamp-2" title="${exercise.name}">${exercise.name}</h3>
                    ${canModify ? `<div class="actions-container flex-shrink-0 flex gap-1"></div>` : ''}
                </div>
            `;
            const bodyHTML = `
                <div class="mb-4 text-gray-600">Сжигается калорий в час: ${exercise.caloriesPerHour} ккал</div>
            `;
            card.innerHTML = headerHTML + bodyHTML;
            if (canModify) {
                const actionsContainer = card.querySelector('.actions-container');
                const editBtn = document.createElement('button');
                editBtn.title = 'Редактировать';
                editBtn.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 -960 960 960"><path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/></svg>`;
                editBtn.className = 'text-black hover:text-blue-800 p-1';
                editBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    openEditModal(exercise);
                });
                actionsContainer.appendChild(editBtn);
                const deleteBtn = document.createElement('button');
                deleteBtn.title = 'Удалить';
                deleteBtn.innerHTML = `<svg class="w-5 h-5" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960"><path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/></svg>`;
                deleteBtn.className = 'text-black hover:text-red-600 p-1';
                deleteBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    confirmDeleteExercise(exercise.exerciseId);
                });
                actionsContainer.appendChild(deleteBtn);
            }
            exerciseList.appendChild(card);
        });
    };

    // Пагинация
    const renderPagination = () => {
        if (!pagination) return;
        pagination.innerHTML = '';
        if (totalPages <= 1) return;
        for (let i = 0; i < totalPages; i++) {
            const btn = document.createElement('button');
            btn.textContent = i + 1;
            btn.className = 'px-3 py-1 rounded-md mx-1 ' + (i === currentPage ? 'bg-primary text-white' : 'bg-gray-200 text-gray-700 hover:bg-gray-300');
            btn.addEventListener('click', () => {
                if (i !== currentPage) {
                    currentPage = i;
                    fetchExercises(lastSearch, currentPage);
                }
            });
            pagination.appendChild(btn);
        }
    };

    // Загрузка упражнений
    const fetchExercises = async (search = '', page = 0) => {
        currentPage = page;
        lastSearch = search;
        let url;
        if (search && search.length > 0) {
            url = `${API_URL}/search?name=${encodeURIComponent(search)}&page=${page}&size=${pageSize}`;
        } else {
            url = `${API_URL}?page=${page}&size=${pageSize}`;
        }
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Ошибка загрузки упражнений');
            const data = await response.json();
            renderExercises(data.content);
            totalPages = data.totalPages || 1;
            renderPagination();
        } catch (error) {
            console.error(error);
            exerciseList.innerHTML = `<p class="text-red-500 col-span-full text-center">${error.message}</p>`;
        }
    };

    // Поиск
    let searchTimeout;
    searchInput.addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            fetchExercises(e.target.value, 0);
        }, 300);
    });

    // Модальное окно
    function openModal() {
        modalTitle.textContent = 'Добавить новое упражнение';
        exerciseForm.reset();
        document.getElementById('exerciseId').value = '';
        exerciseModal.classList.remove('hidden');
        exerciseModal.classList.add('flex');
    }
    function closeModal() {
        exerciseModal.classList.add('hidden');
        exerciseModal.classList.remove('flex');
    }
    function openEditModal(exercise) {
        modalTitle.textContent = 'Редактировать упражнение';
        exerciseForm.reset();
        document.getElementById('exerciseId').value = exercise.exerciseId;
        document.getElementById('name').value = exercise.name;
        document.getElementById('calories_lost_per_hour').value = exercise.caloriesPerHour;
        exerciseModal.classList.remove('hidden');
        exerciseModal.classList.add('flex');
    }
    addExerciseBtn.addEventListener('click', openModal);
    cancelBtn.addEventListener('click', closeModal);

    // Обработка формы
    exerciseForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const id = document.getElementById('exerciseId').value;
        const name = document.getElementById('name').value;
        const caloriesLostPerHour = document.getElementById('calories_lost_per_hour').value;
        const exerciseData = {
            name,
            caloriesPerHour: parseInt(caloriesLostPerHour, 10)
        };
        let url = API_URL;
        const method = id ? 'PUT' : 'POST';
        if (id) {
            exerciseData.exerciseId = id;
        }
        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(exerciseData)
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка сохранения');
            }
            closeModal();
            fetchExercises(lastSearch, currentPage);
        } catch (error) {
            console.error(error);
            alert(`Ошибка: ${error.message}`);
        }
    });

    // Удаление
    function confirmDeleteExercise(id) {
        if (confirm('Вы уверены, что хотите удалить это упражнение?')) {
            deleteExercise(id);
        }
    }
    async function deleteExercise(id) {
        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            if (!response.ok) throw new Error('Ошибка удаления');
            fetchExercises(lastSearch, currentPage);
        } catch (error) {
            console.error(error);
            alert(error.message);
        }
    }
}); 