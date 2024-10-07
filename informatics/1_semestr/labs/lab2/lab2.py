SYMBOLS = ("r1", "r2", "i1", "r3", "i2", "i3", "i4")


def validate_input(cipher: str) -> bool:
    if len(cipher) != 7:
        return False

    if set(cipher) - {"1", "0"}:
        return False

    return True


def main() -> None:
    cipher = input("Введите код Хэмминга из 7 символов: ")
    if not validate_input(cipher):
        print("[Ошибка]. Шифр должен состоять из 0 и 1 и быть длиной 7 символов")
        return

    bits = list(map(int, cipher))
    s1 = (bits[0] + bits[2] + bits[4] + bits[6]) % 2
    s2 = (bits[1] + bits[2] + bits[5] + bits[6]) % 2
    s3 = (bits[3] + bits[4] + bits[5] + bits[6]) % 2
    syndrome = (s1, s2, s3)

    if syndrome != (0, 0, 0):
        num = s3 * 4 + s2 * 2 + s1 * 1
        print(f"Найдена ошибка в символе: {SYMBOLS[num - 1]}")
        bits[num - 1] = 1 - bits[num - 1]
        result = "".join(map(str, bits))
        print(f"Правильное сообщение: {result[2]}{result[4]}{result[5]}{result[6]}")
    else:
        result = "".join(map(str, bits))
        print(f"Сообщение корректно: {result[2]}{result[4]}{result[5]}{result[6]}")

if __name__ == "__main__":
    main()
