"""
Переводит число из системы счислений `-10` в систему счисления `10`
"""


def main() -> None:
    number = int(input("Введите число в системе счисления `-10`: "))

    pointer = 0
    result = 0

    while number:
        current_digit = number % 10
        result += current_digit * ((-10 ) ** pointer)
        number //= 10
        pointer += 1

    print(f"Результат: {result}")


if __name__ == "__main__":
    main()
