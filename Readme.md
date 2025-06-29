<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=120&section=header&text=CalorieTracker&fontSize=35&fontAlignY=35" width="100%" alt="CalorieTracker Banner"/>
  <img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build Status"/>
  <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License"/>
  <img src="https://img.shields.io/badge/Java-21-blue" alt="Java Version"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-success" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/docker--compose-enabled-blue" alt="Docker Compose"/>
</p>

<p align="center">
  <strong>Система для учёта калорий, продуктов и физической активности с аналитикой и графиками</strong>
</p>

<table>
  <tr align="center">
    <td rowspan="2" width="70%">
      <img src="assets/main-page.png" alt="Главная страница" width="90%"/>
    </td>
  </tr>
</table>

---

## 📚 Содержание

- [Описание](#-описание)
- [Функциональность](#-функциональность)
- [Технологии](#-технологии)
- [Интерфейс приложения](#-интерфейс-приложения)
- [Быстрый старт](#-быстрый-старт)
- [Авторы](#-авторы)
- [Лицензия](#-лицензия)

---

## 📝 Описание

**CalorieTracker** — это веб-приложение, позволяющее пользователям отслеживать потребление калорий, управлять списком продуктов и упражнений, вести дневники питания и тренировок, а также просматривать подробную статистику и графики по рациону и активности.

---

## ⚙️ Функциональность

### 🔐 Аутентификация

- Регистрация с хешированием паролей
- Авторизация пользователей

### 🍎 Каталог продуктов

- Добавление/редактирование/удаление продуктов (админ)
- Поиск по названию
- Расчёт КБЖУ на 100г

### 📘 Дневник питания

- Добавление приёмов пищи с порцией (в граммах)
- Автоматический расчёт КБЖУ
- Просмотр общей дневной сводки: суммарные КБЖУ за текущий день
- Редактирование и удаление записей

### 🏋️‍♂️ Каталог упражнений

- Просмотр списка упражнений
- Добавление/редактирование/удаление упражнений (админ)

### 📓 Дневник тренировок

- Учёт продолжительности упражнений
- Автоматический подсчёт сожжённых калорий

### 👤 Профиль пользователя

- Просмотр личной информации
- Отображение аватара пользователя и возможность его изменения
- Отображение целей: целевой вес, калории, БЖУ, период действия
- Управление целями: добавление, редактирование, удаление
- Просмотр истории измерений: рост, вес, изменения со временем

### 📊 Аналитика и отчёты

- Статистика за день, неделю, месяц или произвольный период
- Визуализация данных на графиках
- Админ-аналитика: популярные продукты, активные пользователи, калорийность и т.д.

---

## 🧰 Технологии

<table align="center" style="background-color:#f9f9f9; border-radius:10px; padding:10px;">
  <tr>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" alt="PostgreSQL" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" alt="Docker" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/html5/html5-original.svg" alt="HTML5" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="assets/logo/tailwind.png" alt="Tailwind CSS" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" alt="JavaScript" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/elasticsearch/elasticsearch-original.svg" alt="Elasticsearch" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://www.vectorlogo.zone/logos/redis/redis-icon.svg" alt="Redis" height="48" />
    </td>
    <td align="center" style="padding:10px;">
      <img src="https://avatars.githubusercontent.com/u/695951?s=200&v=4" alt="MinIO" height="48" />
    </td>
  </tr>
</table>

---

## 📸 Интерфейс приложения

<table>
  <thead>
    <tr>
      <th>Раздел</th>
      <th>Скриншот</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Главная страница</td>
      <td><img src="assets/main-page.png" alt="Главная страница" width="600" /></td>
    </tr>
    <tr>
      <td>Дневник питания</td>
      <td><img src="assets/diary.png" alt="Дневник питания" width="400" /></td>
    </tr>
    <tr>
      <td>Продукты</td>
      <td><img src="assets/products.png" alt="Продукты" width="600" /></td>
    </tr>
    <tr>
      <td>Упражнения</td>
      <td><img src="assets/exercises.png" alt="Упражнения" width="500" /></td>
    </tr>
    <tr>
      <td>Профиль пользователя</td>
      <td><img src="assets/profile.png" alt="Профиль пользователя" width="400" /></td>
    </tr>
    <tr>
      <td>Аналитика</td>
      <td><img src="assets/reports.png" alt="Аналитика" width="600" /></td>
    </tr>
    <tr>
      <td>Админ панель</td>
      <td><img src="assets/admin.png" alt="Админ панель" width="500" /></td>
    </tr>
  </tbody>
</table>

---

## 🚀 Быстрый старт

```bash
git clone https://github.com/PracticeNaumen2025/CalorieTracker.git

cd CalorieTracker

cp .env.example .env
# Отредактируйте .env с вашими настройками

docker compose up -d
```

> ⚠️ **Обратите внимание:**  
> Сервис **Elasticsearch** может требовать подключения к VPN при первом запуске,  
> так как его образ или плагины загружаются с ресурсов, недоступных напрямую из некоторых регионов (например, из РФ).

После запуска система будет автоматически наполнена **примерными тестовыми данными**.
В базу добавлены **2 пользователя** с разными ролями:

| Имя                   | Пароль   | Роль  |
| --------------------- | -------- | ----- |
| [admin](mailto:admin) | admin123 | admin |
| [user](mailto:user)   | user123  | user  |

Вы можете использовать эти учётные данные для входа и тестирования приложения.

Чтобы остановить:

```bash
docker-compose down
```

### 🌐 Сервисы после запуска

| Сервис        | URL                                                                                        | Доступ                               |
| ------------- | ------------------------------------------------------------------------------------------ | ------------------------------------ |
| Приложение    | [http://localhost:8080](http://localhost:8080)                                             | Аутентификация                       |
| Swagger UI    | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) | Пользователь с ролью admin           |
| pgAdmin       | [http://localhost:5050](http://localhost:5050)                                             | `admin@naumen.com` / `admin123`      |
| Java Melody   | [http://localhost:8080/monitoring](http://localhost:8080/monitoring)                       | Пользователь с ролью admin           |
| Redis         | [http://localhost:6379](http://localhost:6379)                                             | —                                    |
| MinIO Console | [http://localhost:9001](http://localhost:9001)                                             | `minioadmin` / `minioadmin123`       |
| MinIO API     | [http://localhost:9000](http://localhost:9000)                                             | — (иконки доступны в бакете `icons`) |
| ElasticSearch | [http://localhost:9200](http://localhost:9200)                                             | —                                    |
| ElasticVue    | [http://localhost:8081](http://localhost:8081)                                             | — (UI для ElasticSearch)             |
| Logstash      | [http://localhost:5044](http://localhost:5044)                                             | — (для сбора логов)                  |

---

## 👥 Авторы

[<img src="https://img.shields.io/badge/GitHub-Dbatr-181717?style=flat&logo=github&logoColor=white" />](https://github.com/Dbatr)

[<img src="https://img.shields.io/badge/GitHub-GeorgiiMalishev-181717?style=flat&logo=github&logoColor=white" />](https://github.com/GeorgiiMalishev)

---

## 📄 Лицензия

Проект распространяется под лицензией [MIT](LICENSE).

> ⭐ Не забудь поставить звёздочку на репозиторий, если проект тебе понравился!
