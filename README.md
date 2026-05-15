# Software-testing
Тестирование программного обеспечения

## Лабораторные

- `lab1`: `docs/tasks/lab1.md`
- `lab2`: `docs/tasks/lab2/lab2.md`
- `lab3`: `docs/tasks/lab3/report.md`

## Лабораторная работа №3

Вариант №36958: `Tutu.ru` (`https://www.tutu.ru/`).

### Задание

Сформировать варианты использования сайта, разработать на их основе тестовое покрытие и провести функциональное тестирование интерфейса с автоматизацией на `Selenium WebDriver`.

### Артефакты

- отчёт: `docs/tasks/lab3/report.md`
- use-case диаграмма: `docs/tasks/lab3/use-cases.puml`
- сохранённый HTML-снимок главной страницы: `docs/tasks/lab3/site/main.html`
- UI page object'ы и тесты: `src/test/java/org/example/ui`
- шаблон Selenium IDE: `src/test/resources/selenium-ide/tutu-lab3.side`

### Сценарии

| № | Сценарий | Автотест |
| --- | --- | --- |
| 1 | Проверка основных сервисов и ссылок главной страницы | `TutuMainServicesUiTest` |
| 2 | Проверка ссылки на раздел `Справочная` | `TutuHelpNavigationUiTest` |
| 3 | Ввод направления в поиске отелей | `TutuHotelSearchUiTest` |
| 4 | Проверка элементов блока подписки | `TutuSubscriptionUiTest` |

### Почему используется snapshot

Живой `tutu.ru` активно зависит от клиентской гидрации, внешних ресурсов и антиботной защиты, из-за чего headless-прогон в учебной среде нестабилен. Для воспроизводимого Selenium-тестирования используется локально сохранённый HTML-снимок главной страницы из `docs/tasks/lab3/site/main.html`, при этом проверки остаются привязанными к реальному DOM сайта и используют только XPath-селекторы.

### Запуск

```bash
./gradlew testClasses
./gradlew uiChromeTest
./gradlew uiFirefoxTest
```

Чтобы прогнать сценарии против живого сайта, можно явно переопределить URL:

```bash
./gradlew uiChromeTest -Dui.baseUrl=https://www.tutu.ru/
```
