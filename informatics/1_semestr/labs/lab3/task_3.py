import re
from pathlib import Path

GROUP = "P3115"

TEXTS_PATH = Path(__file__).parent / "texts" / "task_3"
PATTERN = re.compile(
    r"^(\w)\w+\s+(?:\1(?:\.|\w+\s+))+\s+{group}$".format(group=GROUP),
    flags=re.IGNORECASE | re.MULTILINE
)

CORRECT_ANSWERS = (
    "Петров П.П. P0000\nАнищенко А.Z. P3115",
    "Петров П.П. P3114\nВасильев В.И. P3115",
    "Кузнецов А.А. P3115\nЛебедев К.Н. P3116",
    "Сафонов С.Ю. P3115\nСоловьев В.А. P3115",
    "Романов Р.С. P3115"
)


def find_words(text: str) -> set[str]:
    return set(PATTERN.findall(text))


def main() -> None:
    for i in range(1, 6):
        text = (TEXTS_PATH / f"text_{i}").read_text(encoding="utf-8")
        processed_text = PATTERN.sub("", text).strip()
        processed_text = re.sub(r"\n{2,}", "\n", processed_text)
        correct_answer = CORRECT_ANSWERS[i - 1]

        if processed_text != correct_answer:
            print(
                f"Ошибка в тесте {i}\n"
                f"Ожидалось - {correct_answer}\n"
                f"Результат - {processed_text}"
            )
        else:
            print(f"Текст {i}: результат - {processed_text}")
        print("=" * 100)



if __name__ == '__main__':
    main()
