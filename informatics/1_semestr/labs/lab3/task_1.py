import re
from pathlib import Path

TEXTS_PATH = Path(__file__).parent / "texts" / "task_1"
EMOJI_PATTERN = re.compile(r"\[</")

CORRECT_COUNTS = (1, 3, 1, 2, 4)


def count_emoji(text: str) -> int:
    return len(EMOJI_PATTERN.findall(text))


def main() -> None:
    for i in range(1, 6):
        text = (TEXTS_PATH / f"text_{i}").read_text()
        emoji_count = count_emoji(text)

        if emoji_count != CORRECT_COUNTS[i - 1]:
            print(
                f"Ошибка в тесте {i}. "
                f"Ожидалось - {CORRECT_COUNTS[i - 1]}, "
                f"найдено - {emoji_count}"
            )
        else:
            print(f"Текст {i}: смайликов - {emoji_count}")
        print("=" * 100)


if __name__ == '__main__':
    main()
