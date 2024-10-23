EYES = ("8", ";", "X", ":", "=", "[")
NOSE = ("-", "<", "-{", "<{")
MOUTH = ("(", ")", "P", "|", "\\", "/", "O", "=")


def get_isu() -> int:
    raw = input("Введите свой ISU: ")
    if not raw.isdigit():
        raise ValueError("ISU должен состьять из цифр")
    if len(raw) != 6:
        raise ValueError("ISU должен состоять из 6 цифр")

    return int(raw)


def calculate_emoji(isu: int) -> str:
    emoji = EYES[isu % 6] + NOSE[isu % 4] + MOUTH[isu % 8]

    return emoji


def main() -> None:
    isu = get_isu()
    emoji = calculate_emoji(isu)

    print(f"Ваш смайлик: {emoji}")


if __name__ == "__main__":
    main()
