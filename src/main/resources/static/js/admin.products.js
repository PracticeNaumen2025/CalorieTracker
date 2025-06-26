function formatDate(date) {
    return date.toISOString().split("T")[0];
}

function getDefaultDateRange() {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    return [formatDate(firstDay), formatDate(lastDay)];
}

document.addEventListener("DOMContentLoaded", () => {
    const fromInput = document.getElementById("fromDate");
    const toInput = document.getElementById("toDate");

    const [fromDate, toDate] = getDefaultDateRange();
    fromInput.value = fromDate;
    toInput.value = toDate;

    document.getElementById("loadStats").addEventListener("click", () => {
        loadAllStats();
    });

    loadAllStats();
});

function loadAllStats() {
    const from = document.getElementById("fromDate").value;
    const to = document.getElementById("toDate").value;

    loadPopularProducts(from, to);
    loadAveragePortions(from, to);
    loadNutritionSummary(from, to);
    loadPopularCategories(from, to);
    loadAverageCalories(from, to);
}

function loadPopularProducts(from, to) {
    fetch(`/api/admin/products/popular?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("popularProducts");
            container.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-semibold">${item.productName}</span> — ${item.usageCount} использований`;
                container.appendChild(li);
            });
        });
}

function loadAveragePortions(from, to) {
    fetch(`/api/admin/products/average-portion?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("averagePortions");
            container.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-semibold">${item.productName}</span> — ${item.averagePortionGrams.toFixed(1)} г`;
                container.appendChild(li);
            });
        });
}

function loadNutritionSummary(from, to) {
    fetch(`/api/admin/products/nutrition-summary?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("nutritionSummary");
            container.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <div class="space-y-1">
                        <span class="font-semibold">${item.productName}</span><br>
                        <span>Калории: ${item.totalCalories} ккал</span><br>
                        <span>Б: ${item.totalProtein} г, Ж: ${item.totalFat} г, У: ${item.totalCarb} г</span>
                    </div>
                `;
                container.appendChild(li);
            });
        });
}

function loadPopularCategories(from, to) {
    fetch(`/api/admin/products/popular-categories?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("popularCategories");
            container.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-semibold">${item.categoryName}</span> — ${item.usageCount} использований`;
                container.appendChild(li);
            });
        });
}

function loadAverageCalories(from, to) {
    fetch(`/api/admin/products/average-daily-calories?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(calories => {
            document.getElementById("averageCalories").textContent = `${Number(calories).toFixed(2)} ккал/день`;
        });
}
