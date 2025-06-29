document.addEventListener('DOMContentLoaded', function() {
    const productList = document.getElementById('productList');
    const searchInput = document.getElementById('searchInput');
    const addProductBtn = document.getElementById('addProductBtn');
    const productModal = document.getElementById('productModal');
    const cancelBtn = document.getElementById('cancelBtn');
    const productForm = document.getElementById('productForm');
    const modalTitle = document.getElementById('modalTitle');
    const pagination = document.getElementById('pagination');

    const API_URL = '/api/products';
    let currentPage = 0;
    let pageSize = 9;
    let totalPages = 1;
    let lastSearch = '';

    // --- Категории ---
    const categorySearch = document.getElementById('categorySearch');
    const categoryDropdown = document.getElementById('categoryDropdown');
    let selectedCategory = null;
    let categoryPage = 0;
    let categoryTotalPages = 1;
    let lastCategorySearch = '';
    const CATEGORY_API_URL = '/api/category';

    let currentUserId = null;
    let isAdmin = false;

    // Получаем userId и isAdmin при загрузке страницы
    fetch('/api/user/current')
      .then(r => r.json())
      .then(id => { currentUserId = id; });
    fetch('/api/user/is_admin')
      .then(r => r.json())
      .then(flag => { isAdmin = flag; });

    /**
     * Отображает продукты в виде карточек.
     * @param {Array<Object>} products - Массив объектов продуктов.
     */
    const renderProducts = (products) => {
        productList.innerHTML = '';
        if (!products || products.length === 0) {
            productList.innerHTML = `<p class="text-gray-500 col-span-full text-center">Продукты не найдены.</p>`;
            return;
        }
        products.forEach(product => {
            const card = document.createElement('div');
            card.className = 'bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow duration-300';
            
            const headerHTML = `
                <div class="flex justify-between items-start gap-4 mb-2">
                    <h3 class="text-xl font-bold text-gray-800 break-words line-clamp-2" title="${product.name}">${product.name}</h3>
                    <div class="actions-container flex-shrink-0 flex gap-1"></div>
                </div>
            `;
            
            const bodyHTML = `
                <div class="mb-4 text-gray-600">Калорийность: ${product.caloriesPer100g} ккал</div>
                <div class="grid grid-cols-3 gap-2 text-center text-sm mb-2">
                    <div class="bg-emerald-100 text-emerald-800 rounded-full px-2 py-1">Б: ${product.proteinPer100g}г</div>
                    <div class="bg-amber-100 text-amber-800 rounded-full px-2 py-1">Ж: ${product.fatPer100g}г</div>
                    <div class="bg-sky-100 text-sky-800 rounded-full px-2 py-1">У: ${product.carbsPer100g}г</div>
                </div>
                <div class="text-xs text-gray-400">Категория: ${product.categoryName || '-'}</div>
            `;
            
            card.innerHTML = headerHTML + bodyHTML;

            if ((product.userId && product.userId === currentUserId) || isAdmin) {
                const actionsContainer = card.querySelector('.actions-container');
                
                const editBtn = document.createElement('button');
                editBtn.title = 'Редактировать';
                editBtn.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 -960 960 960"><path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/></svg>`;
                editBtn.className = 'text-black hover:text-blue-800 p-1';
                editBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    openEditModal(product);
                });
                actionsContainer.appendChild(editBtn);

                if (isAdmin) {
                    const deleteBtn = document.createElement('button');
                    deleteBtn.title = 'Удалить';
                    deleteBtn.innerHTML = `<svg class="w-5 h-5" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960"><path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/></svg>`;
                    deleteBtn.className = 'text-black hover:text-red-600 p-1';
                    deleteBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        confirmDeleteProduct(product.productId);
                    });
                    actionsContainer.appendChild(deleteBtn);
                }
            }
            productList.appendChild(card);
        });
    };

    /**
     * Рендерит пагинацию.
     */
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
                    fetchProducts(lastSearch, currentPage);
                }
            });
            pagination.appendChild(btn);
        }
    };

    /**
     * Загружает продукты с сервера с учётом поиска и пагинации.
     * @param {string} search - Поисковый запрос.
     * @param {number} page - Номер страницы.
     */
    const fetchProducts = async (search = '', page = 0) => {
        let url;
        if (search && search.length > 0) {
            url = `${API_URL}/search?name=${encodeURIComponent(search)}&page=${page}&size=${pageSize}`;
        } else {
            url = `${API_URL}?page=${page}&size=${pageSize}`;
        }
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Ошибка загрузки продуктов');
            const data = await response.json();
            // Ожидается, что backend возвращает объект { content, totalPages }
            if (Array.isArray(data)) {
                renderProducts(data);
                totalPages = 1;
            } else {
                renderProducts(data.content);
                totalPages = data.totalPages || 1;
            }
            renderPagination();
        } catch (error) {
            console.error(error);
            productList.innerHTML = `<p class="text-red-500 col-span-full text-center">${error.message}</p>`;
        }
    };

    /**
     * Загружает категории с сервера по поисковому запросу и пагинации.
     */
    const fetchCategories = async (search = '', page = 0) => {
        if (!categoryDropdown) return;
        let url = `${CATEGORY_API_URL}?name=${encodeURIComponent(search)}&page=${page}&size=7`;
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Ошибка загрузки категорий');
            const data = await response.json();
            renderCategoryDropdown(data.content, data.totalPages || 1, page);
        } catch (error) {
            categoryDropdown.innerHTML = `<div class='text-red-500 px-4 py-2'>${error.message}</div>`;
            categoryDropdown.classList.remove('hidden');
        }
    };

    /**
     * Рендерит выпадающий список категорий с пагинацией.
     */
    const renderCategoryDropdown = (categories, totalPages, page) => {
        categoryDropdown.innerHTML = '';
        if (!categories || categories.length === 0) {
            categoryDropdown.innerHTML = `<div class='px-4 py-2 text-gray-500 w-full truncate rounded-md'>Ничего не найдено</div>`;
        } else {
            categories.forEach(cat => {
                const item = document.createElement('div');
                item.className = 'px-4 py-2 hover:bg-primary hover:text-white cursor-pointer';
                item.textContent = cat.categoryName;
                item.addEventListener('click', () => {
                    selectedCategory = cat;
                    categorySearch.value = cat.categoryName;
                    categoryDropdown.classList.add('hidden');
                });
                categoryDropdown.appendChild(item);
            });
        }
        // Пагинация
        if (totalPages > 1) {
            const pag = document.createElement('div');
            pag.className = 'flex justify-center gap-1 py-2 border-t mt-2';
            for (let i = 0; i < totalPages; i++) {
                const btn = document.createElement('button');
                btn.textContent = i + 1;
                btn.className = 'px-2 py-1 rounded ' + (i === page ? 'bg-primary text-white' : 'bg-gray-200 text-gray-700 hover:bg-gray-300');
                btn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    categoryPage = i;
                    fetchCategories(lastCategorySearch, categoryPage);
                });
                pag.appendChild(btn);
            }
            categoryDropdown.appendChild(pag);
        }
        categoryDropdown.classList.remove('hidden');
    };

    // Обработка поиска категории
    let categorySearchTimeout;
    categorySearch.addEventListener('input', (e) => {
        clearTimeout(categorySearchTimeout);
        lastCategorySearch = e.target.value;
        categoryPage = 0;
        selectedCategory = null;
        categorySearchTimeout = setTimeout(() => {
            if (lastCategorySearch.length > 0) {
                fetchCategories(lastCategorySearch, categoryPage);
            } else {
                categoryDropdown.classList.add('hidden');
            }
        }, 300);
    });
    categorySearch.addEventListener('focus', (e) => {
        if (categorySearch.value.length > 0) {
            fetchCategories(categorySearch.value, categoryPage);
        }
    });
    document.addEventListener('click', (e) => {
        if (!categoryDropdown.contains(e.target) && e.target !== categorySearch) {
            categoryDropdown.classList.add('hidden');
        }
    });

    // --- Модальное окно ---
    function openModal() {
        modalTitle.textContent = 'Добавить новый продукт';
        productForm.reset();
        document.getElementById('productId').value = '';
        selectedCategory = null;
        categorySearch.value = '';
        categoryDropdown.classList.add('hidden');
        productModal.classList.remove('hidden');
        productModal.classList.add('flex');
    }

    function closeModal() {
        productModal.classList.add('hidden');
        productModal.classList.remove('flex');
    }

    function handleFormSubmit(e) {
        e.preventDefault();
        if (!selectedCategory) {
            alert('Пожалуйста, выберите категорию');
            return;
        }
        const product = {
            name: document.getElementById('name').value,
            caloriesPer100g: parseFloat(document.getElementById('calories').value),
            proteinPer100g: parseFloat(document.getElementById('protein').value),
            fatPer100g: parseFloat(document.getElementById('fat').value),
            carbsPer100g: parseFloat(document.getElementById('carbs').value),
            categoryId: selectedCategory.categoryId
        };
        const productId = document.getElementById('productId').value;
        if (productId) {
            // Редактирование
            fetch(`${API_URL}/${productId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            }).then(r => {
                closeModal();
                fetchProducts(lastSearch, currentPage);
            });
        } else {
            // Добавление
            fetch(`${API_URL}/add`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            }).then(r => {
                closeModal();
                fetchProducts(lastSearch, currentPage);
            });
        }
    }

    // Открытие модального окна для редактирования
    function openEditModal(product) {
        modalTitle.textContent = 'Редактировать продукт';
        productModal.classList.remove('hidden');
        productModal.classList.add('flex');
        document.getElementById('productId').value = product.productId;
        document.getElementById('name').value = product.name;
        document.getElementById('calories').value = product.caloriesPer100g;
        document.getElementById('protein').value = product.proteinPer100g;
        document.getElementById('fat').value = product.fatPer100g;
        document.getElementById('carbs').value = product.carbsPer100g;
        selectedCategory = { categoryId: product.categoryId, categoryName: product.categoryName };
        categorySearch.value = product.categoryName || '';
        categoryDropdown.classList.add('hidden');
    }

    // Удаление продукта (только для админа)
    function confirmDeleteProduct(productId) {
        if (!confirm('Вы действительно хотите удалить этот продукт?')) return;
        fetch(`/api/products/${productId}`, {
            method: 'DELETE'
        }).then(async r => {
            if (!r.ok) {
                const msg = await r.text();
                alert(msg || 'Ошибка при удалении');
            } else {
                alert('Продукт удалён');
                fetchProducts(lastSearch, currentPage);
            }
        });
    }

    // Инициализация
    fetchProducts();

    // Назначение обработчиков событий
    let searchTimeout;
    searchInput.addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            lastSearch = e.target.value;
            currentPage = 0;
            fetchProducts(lastSearch, currentPage);
        }, 300);
    });

    addProductBtn.addEventListener('click', openModal);
    cancelBtn.addEventListener('click', closeModal);
    productForm.addEventListener('submit', handleFormSubmit);
}); 