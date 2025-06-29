async function loadDiary() {
    const mealTypeMap = {
        breakfast: "Завтрак",
        lunch: "Обед",
        dinner: "Ужин",
        snack: "Перекус"
    };

    const dateInput = document.getElementById("dateInput").value;
    const mealsContainer = document.getElementById("mealsContainer");
    const infoText = document.getElementById("infoText");
    const dailySummary = document.getElementById("dailySummary");

    mealsContainer.innerHTML = "";
    infoText.classList.add("hidden");
    dailySummary.classList.add("hidden");

    if (!dateInput) {
        mealsContainer.innerHTML = `<p class="text-red-600 font-medium text-center">Пожалуйста, выберите дату.</p>`;
        return;
    }

    try {
        // Загрузка приёмов пищи
        const response = await fetch(`/api/diary/meals-with-entries?date=${dateInput}`);
        console.log('meals response status:', response.status);
        if (!response.ok) {
            throw new Error("Ошибка при загрузке данных");
        }
        const mealsData = await response.json();

        if (mealsData.length === 0) {
            mealsContainer.innerHTML = `<p class="text-gray-600 text-center">Нет записей за выбранную дату.</p>`;
            return;
        }

        infoText.classList.remove("hidden");

        mealsData.forEach(meal => {
            const mealCard = document.createElement("div");
            mealCard.className = "bg-gray-50 border border-gray-200 rounded-xl p-6 shadow-sm";

            const mealTime = new Date(meal.dateTime).toLocaleTimeString("ru-RU", {
                hour: "2-digit", minute: "2-digit"
            });

            mealCard.innerHTML = `
                <div class="mb-4">
                    <div class="flex items-center gap-2">
                        <h2 class="text-2xl font-bold text-primary">
                            ${mealTypeMap[meal.mealType] || meal.mealType}
                        </h2>
                        <button onclick="confirmDeleteMeal(${meal.mealId})"
                                class="text-black hover:text-red-600 p-1" title="Удалить приём пищи">
                            <svg class="w-5 h-5" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                <path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
                            </svg>
                        </button>
                    </div>
                    <p class="text-sm text-gray-500 mt-1">Время приёма: <strong>${mealTime}</strong></p>
                </div>
                <ul class="divide-y divide-gray-200 text-sm">
                    ${meal.foodEntries.map(entry => `
                        <li class="py-3 flex justify-between items-start">
                            <div>
                                <p class="font-medium text-gray-800">${entry.productName}</p>
                                <p class="text-gray-500 text-xs mt-1">
                                    Порция: ${entry.portionGrams} г<br>
                                    Калории: ${entry.calories} ккал<br>
                                    Б: ${entry.proteinG} г, Ж: ${entry.fatG} г, У: ${entry.carbsG} г
                                </p>
                            </div>
                            
                            <div class="flex gap-2">
                                <button onclick="openEditFoodEntry(${entry.entryId}, ${entry.portionGrams})"
                                        class="text-black hover:text-blue-800 p-1" title="Редактировать">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 -960 960 960" height="24" width="24">
                                        <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                                    </svg>
                                </button>
                            
                                <button onclick="confirmDeleteFoodEntry(${entry.entryId})"
                                        class="text-black hover:text-red-600 p-1" title="Удалить">
                                    <svg class="w-5 h-5" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                        <path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
                                    </svg>
                                </button>
                            </div>
                        </li>
                    `).join("")}
                </ul>
            `;

            mealsContainer.appendChild(mealCard);
        });

        // Загрузка итогов за день (в отдельном try, чтобы не ломать основной вывод)
        try {
            const summaryResponse = await fetch(`/api/diary/day-summary?date=${dateInput}`);
            console.log('summary response status:', summaryResponse.status);
            if (!summaryResponse.ok) {
                throw new Error("Ошибка при загрузке итогов за день");
            }
            const summaryData = await summaryResponse.json();

            // Заполняем итоги
            document.getElementById("sumCalories").textContent = summaryData.totalCalories.toFixed(2);
            document.getElementById("sumProtein").textContent = summaryData.totalProtein.toFixed(2);
            document.getElementById("sumFat").textContent = summaryData.totalFat.toFixed(2);
            document.getElementById("sumCarbs").textContent = summaryData.totalCarbs.toFixed(2);

            dailySummary.classList.remove("hidden");
        } catch (summaryError) {
            console.warn("Не удалось загрузить итоги за день:", summaryError);
            dailySummary.classList.add("hidden");
        }

    } catch (error) {
        console.error("Ошибка загрузки дневника:", error);
        mealsContainer.innerHTML = `<p class="text-red-600 text-center">Не удалось загрузить данные. Попробуйте позже.</p>`;
        infoText.classList.add("hidden");
        dailySummary.classList.add("hidden");
    }
}


document.getElementById("addMealBtn").addEventListener("click", () => {
    document.getElementById("mealOverlay").classList.remove("hidden");
});

document.getElementById("closeOverlay").addEventListener("click", () => {
    document.getElementById("mealOverlay").classList.add("hidden");
});

// Обработка отправки формы
document.getElementById("mealForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const mealType = document.getElementById("mealType").value;
    const dateTime = document.getElementById("dateTime").value;

    if (!mealType || !dateTime) {
        alert("Пожалуйста, заполните все поля.");
        return;
    }

    const payload = {
        mealType,
        dateTime
    };

    try {
        const response = await fetch("/api/diary/meal", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || "Ошибка при создании приёма пищи");
        }

        alert("Приём пищи успешно добавлен!");
        document.getElementById("mealOverlay").classList.add("hidden");

        // Очистить форму
        document.getElementById("mealForm").reset();

        // Обновить дневник, подгрузив данные за выбранную дату
        const dateInput = document.getElementById("dateInput");
        if (!dateInput.value) {
            dateInput.value = new Date().toISOString().slice(0, 10);
        }
        loadDiary();

    } catch (error) {
        alert(error.message);
        console.error(error);
    }
});

let productSearchInited = false;
let selectedProduct = null;

// Открытие формы добавления еды
document.getElementById("addFoodEntryBtn").addEventListener("click", async () => {
    const date = document.getElementById("dateInput").value;
    if (!date) {
        alert("Сначала выберите дату.");
        return;
    }
    try {
        // Загрузка приёмов пищи по дате
        const mealsRes = await fetch(`/api/diary/meals?date=${date}`);
        if (!mealsRes.ok) throw new Error("Ошибка при загрузке приёмов пищи");
        const meals = await mealsRes.json();
        const mealSelect = document.getElementById("mealSelect");
        mealSelect.innerHTML = meals.map(meal => {
            const time = new Date(meal.dateTime).toLocaleTimeString("ru-RU", {
                hour: "2-digit", minute: "2-digit"
            });
            const type = meal.mealType.charAt(0).toUpperCase() + meal.mealType.slice(1);
            return `<option value="${meal.mealId}">${type} — ${time}</option>`;
        }).join("");

        // Показать оверлей
        document.getElementById("foodEntryOverlay").classList.remove("hidden");

        // Инициализация поиска продуктов только после появления элементов
        if (!productSearchInited) {
            const productSearch = document.getElementById('productSearch');
            const productDropdown = document.getElementById('productDropdown');
            const productInfo = document.getElementById('productInfo');
            selectedProduct = null;
            if (productSearch && productDropdown && productInfo) {
                let productSearchTimeout;
                productSearch.addEventListener('input', (e) => {
                    clearTimeout(productSearchTimeout);
                    const query = e.target.value.trim();
                    selectedProduct = null;
                    productInfo.textContent = '';
                    if (!query) {
                        productDropdown.classList.add('hidden');
                        return;
                    }
                    productSearchTimeout = setTimeout(async () => {
                        try {
                            const res = await fetch(`/api/products/search?name=${encodeURIComponent(query)}&size=7`);
                            if (!res.ok) throw new Error('Ошибка поиска продуктов');
                            const data = await res.json();
                            renderProductDropdown(data.content || data);
                        } catch (err) {
                            productDropdown.innerHTML = `<div class='text-red-500 px-4 py-2'>${err.message}</div>`;
                            productDropdown.classList.remove('hidden');
                        }
                    }, 300);
                });
                productSearch.addEventListener('focus', () => {
                    if (productSearch.value.trim().length > 0) {
                        productSearch.dispatchEvent(new Event('input'));
                    }
                });
                document.addEventListener('click', (e) => {
                    if (!productDropdown.contains(e.target) && e.target !== productSearch) {
                        productDropdown.classList.add('hidden');
                    }
                });
                function renderProductDropdown(products) {
                    productDropdown.innerHTML = '';
                    if (!products || products.length === 0) {
                        productDropdown.innerHTML = `<div class='px-4 py-2 text-gray-500 w-full truncate rounded-md'>Ничего не найдено</div>`;
                    } else {
                        products.forEach(prod => {
                            const item = document.createElement('div');
                            item.className = 'px-4 py-2 hover:bg-primary hover:text-white cursor-pointer';
                            item.textContent = prod.name;
                            item.addEventListener('click', () => {
                                selectedProduct = prod;
                                productSearch.value = prod.name;
                                productDropdown.classList.add('hidden');
                                productInfo.innerHTML = `Калории: <b>${prod.caloriesPer100g}</b> ккал, Б: <b>${prod.proteinPer100g}</b> г, Ж: <b>${prod.fatPer100g}</b> г, У: <b>${prod.carbsPer100g}</b> г`;
                            });
                            productDropdown.appendChild(item);
                        });
                    }
                    productDropdown.classList.remove('hidden');
                }
            }
            productSearchInited = true;
        }

        // Загрузка продуктов больше не требуется (поиск по мере ввода)
    } catch (err) {
        alert(err.message || "Ошибка при загрузке данных");
        console.error(err);
    }
});

// Сброс инициализации поиска при закрытии оверлея
const closeFoodOverlayBtn = document.getElementById("closeFoodOverlay");
if (closeFoodOverlayBtn) {
    closeFoodOverlayBtn.addEventListener("click", () => {
        document.getElementById("foodEntryOverlay").classList.add("hidden");
        productSearchInited = false;
    });
}

// Обработка формы добавления еды
document.getElementById("foodEntryForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const mealId = document.getElementById("mealSelect").value;
    const portionGrams = document.getElementById("portionGrams").value;

    if (!selectedProduct || !selectedProduct.productId) {
        alert("Пожалуйста, выберите продукт из списка поиска.");
        return;
    }
    const productId = selectedProduct.productId;

    if (!mealId || !productId || !portionGrams) {
        alert("Пожалуйста, заполните все поля.");
        return;
    }

    const payload = {
        mealId: parseInt(mealId),
        productId: parseInt(productId),
        portionGrams: parseFloat(portionGrams)
    };

    try {
        const res = await fetch("/api/diary/food-entries", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Ошибка при добавлении еды");
        }

        alert("Еда успешно добавлена!");
        document.getElementById("foodEntryOverlay").classList.add("hidden");
        document.getElementById("foodEntryForm").reset();
        selectedProduct = null;
        const productInfo = document.getElementById('productInfo');
        if (productInfo) productInfo.textContent = '';
        productSearchInited = false;
        loadDiary();
    } catch (err) {
        alert(err.message);
        console.error(err);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const dateInput = document.getElementById("dateInput");
    if (!dateInput.value) {
        const today = new Date().toISOString().slice(0, 10);
        dateInput.value = today;
        loadDiary(); // Загрузить дневник за сегодня
    }
});


function openEditFoodEntry(entryId, portionGrams) {
    document.getElementById("editEntryId").value = entryId;
    document.getElementById("editPortionGrams").value = portionGrams;
    document.getElementById("editFoodEntryOverlay").classList.remove("hidden");
}

document.getElementById("closeEditOverlay").addEventListener("click", () => {
    document.getElementById("editFoodEntryOverlay").classList.add("hidden");
});

document.getElementById("editFoodEntryForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const entryId = document.getElementById("editEntryId").value;
    const portionGrams = document.getElementById("editPortionGrams").value;

    try {
        const res = await fetch("/api/diary/food-entries", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                entryId: parseInt(entryId),
                portionGrams: parseFloat(portionGrams)
            })
        });

        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Ошибка при обновлении");
        }

        alert("Порция обновлена");
        document.getElementById("editFoodEntryOverlay").classList.add("hidden");
        loadDiary();
    } catch (err) {
        alert(err.message);
        console.error(err);
    }
});

async function confirmDeleteFoodEntry(entryId) {
    if (!confirm("Вы действительно хотите удалить эту запись о еде?")) return;

    try {
        const res = await fetch(`/api/diary/food-entries/${entryId}`, {
            method: "DELETE"
        });

        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Ошибка при удалении");
        }

        alert("Еда удалена");
        loadDiary();
    } catch (err) {
        alert(err.message);
        console.error(err);
    }
}

async function confirmDeleteMeal(mealId) {
    if (!confirm("Удалить весь приём пищи и связанные записи?")) return;

    try {
        const res = await fetch(`/api/diary/meals/${mealId}`, {
            method: "DELETE"
        });

        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Ошибка при удалении приёма пищи");
        }

        alert("Приём пищи удалён");
        loadDiary();
    } catch (err) {
        alert(err.message);
        console.error(err);
    }
}

