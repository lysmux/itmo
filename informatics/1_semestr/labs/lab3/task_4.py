import re
from collections import Counter


def compute_text(word_num: int, text: str) -> str:
    repeated = re.findall(r"\b(\w+)(?:ая|яя|ое|ее|ие|ые|ого|его|ому|ему|ом|ем|их|ых|ими|ыми|им|ым|ую|юю|щй|ей|ый)\b",
                          text)
    adjective_pattern = Counter(repeated).most_common(1)[0][0]

    adjectives = re.findall(
        r"\b{adjective_pattern}\w+\b".format(adjective_pattern=adjective_pattern), text,
        flags=re.IGNORECASE
    )
    needed_form = adjectives[word_num - 1]

    replaced_text = re.sub(
        pattern=r"\b{adjective_pattern}\w+\b".format(adjective_pattern=adjective_pattern),
        repl=needed_form,
        string=text,
        flags=re.IGNORECASE
    )

    return replaced_text


def main() -> None:
    word_num = int(re.search())
    text = input()

    print(compute_text(
        text=text,
        word_num=word_num
    ))


if __name__ == '__main__':
    main()
