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
                    <h2 class="text-2xl font-bold text-primary">${mealTypeMap[meal.mealType] || meal.mealType}</h2>
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



// Открытие формы добавления еды
document.getElementById("addFoodEntryBtn").addEventListener("click", async () => {
    const date = document.getElementById("dateInput").value;
    console.log("Выбранная дата:", date); // Лог даты перед загрузкой

    if (!date) {
        alert("Сначала выберите дату.");
        return;
    }

    try {
        // Загрузка приёмов пищи по дате
        const mealsRes = await fetch(`/api/diary/meals?date=${date}`);
        console.log("Статус ответа meals:", mealsRes.status);
        if (!mealsRes.ok) throw new Error("Ошибка при загрузке приёмов пищи");
        const meals = await mealsRes.json();
        console.log("Загружены приёмы пищи:", meals);

        const mealSelect = document.getElementById("mealSelect");
        mealSelect.innerHTML = meals.map(meal => {
            const time = new Date(meal.dateTime).toLocaleTimeString("ru-RU", {
                hour: "2-digit", minute: "2-digit"
            });
            const type = meal.mealType.charAt(0).toUpperCase() + meal.mealType.slice(1);
            return `<option value="${meal.mealId}">${type} — ${time}</option>`;
        }).join("");
        console.log("Заполнен mealSelect:", mealSelect.innerHTML);

        // Загрузка продуктов
        const productsRes = await fetch(`/api/products`);
        console.log("Статус ответа products:", productsRes.status);
        if (!productsRes.ok) throw new Error("Ошибка при загрузке продуктов");
        const products = await productsRes.json();
        console.log("Загружены продукты:", products);

        const productSelect = document.getElementById("productSelect");
        productSelect.innerHTML = products.map(p => `<option value="${p.productId}">${p.name}</option>`).join("");
        console.log("Заполнен productSelect:", productSelect.innerHTML);

        // Показать оверлей
        document.getElementById("foodEntryOverlay").classList.remove("hidden");
    } catch (err) {
        alert(err.message || "Ошибка при загрузке данных");
        console.error(err);
    }
});

// Обработка формы добавления еды
document.getElementById("foodEntryForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const mealId = document.getElementById("mealSelect").value;
    const productId = document.getElementById("productSelect").value;
    const portionGrams = document.getElementById("portionGrams").value;

    console.log("Отправляемые данные:", { mealId, productId, portionGrams });

    if (!mealId || !productId || !portionGrams) {
        alert("Пожалуйста, заполните все поля.");
        return;
    }

    const payload = {
        mealId: parseInt(mealId),
        productId: parseInt(productId),
        portionGrams: parseFloat(portionGrams)
    };

    console.log("Формируем payload:", payload);

    try {
        const res = await fetch("/api/diary/food-entries", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        console.log("Ответ от сервера:", res.status);

        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Ошибка при добавлении еды");
        }

        alert("Еда успешно добавлена!");
        document.getElementById("foodEntryOverlay").classList.add("hidden");
        document.getElementById("foodEntryForm").reset();
        loadDiary();
    } catch (err) {
        alert(err.message);
        console.error(err);
    }
});

document.getElementById("closeFoodOverlay").addEventListener("click", () => {
    document.getElementById("foodEntryOverlay").classList.add("hidden");
});


document.addEventListener("DOMContentLoaded", () => {
    const dateInput = document.getElementById("dateInput");
    if (!dateInput.value) {
        const today = new Date().toISOString().slice(0, 10);
        dateInput.value = today;
        loadDiary(); // Загрузить дневник за сегодня
    }
});

