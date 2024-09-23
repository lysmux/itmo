"""
Переводит число из системы счислений `10` в систему счисления `-10`
"""


def main() -> None:
    number = int(input("Введите число в системе счисления `10`: "))

    result = ""

    while number:
        remainder = number % -10
        number //= -10

        # корректировки, если остаток меньше 0
        if remainder < 0:
            remainder += 10
            number += 1

        result += str(remainder)

    print(f"Результат: {result[::-1]}")


if __name__ == "__main__":
    main()
