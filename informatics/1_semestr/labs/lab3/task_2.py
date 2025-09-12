import re
from pathlib import Path

TEXTS_PATH = Path(__file__).parent / "texts" / "task_2"
PATTERN = re.compile(
    r"\b\w*[аеёиоуыэюя]{2}\w*\b(?!\s+(?:[аеёиоуыэюя]*[бвгджзйклмнпрстфхцчшщ]){4,})",
    re.IGNORECASE
)
CORRECT_ANSWERS = (
    {"гуляет"},
    {"создание"},
    {"Синее", "животное"},
    {"тёплую"},
    {"зелёную"}
)


def find_words(text: str) -> set[str]:
    return set(PATTERN.findall(text))


def main() -> None:
    for i in range(1, 6):
        text = (TEXTS_PATH / f"text_{i}").read_text(encoding="utf-8")
        words = find_words(text)

        if words != CORRECT_ANSWERS[i - 1]:
            print(
                f"Ошибка в тесте {i}. "
                f"Ожидалось - {CORRECT_ANSWERS[i - 1]}, "
                f"Результат - {words}"
            )
        else:
            print(f"Текст {i}: слова - {words}")
        print("=" * 100)


if __name__ == '__main__':
    main()
