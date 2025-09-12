import re
from abc import ABC, abstractmethod
from dataclasses import dataclass
from enum import StrEnum, auto
from pathlib import Path
from typing import Any


class JsonItemType(StrEnum):
    OBJECT = auto()
    INTEGER = auto()
    FLOAT = auto()
    BOOLEAN = auto()
    STRING = auto()


@dataclass
class IJsonItem[T](ABC):
    name: str
    value: T

    @classmethod
    def parse(cls, content: list[str]) -> "IJsonItem[T]":
        name = get_tag_name(content[0])
        value = cls.parse_value(content[1])

        return cls(name=name, value=value)

    @staticmethod
    @abstractmethod
    def parse_value(value: Any) -> T: ...

    @abstractmethod
    def to_json(self, depth: int = 1) -> str: ...


@dataclass
class JsonIntegerNumber(IJsonItem[int]):
    @staticmethod
    def parse_value(value: Any) -> int:
        return int(value)

    def to_json(self, depth: int = 1) -> str:
        return "\t" * depth + f"\"{self.name}\": {self.value}"


@dataclass
class JsonFloatNumber(IJsonItem[float]):
    @staticmethod
    def parse_value(value: Any) -> float:
        return float(value)

    def to_json(self, depth: int = 1) -> str:
        return "\t" * depth + f"\"{self.name}\": {self.value}"


@dataclass
class JsonBoolean(IJsonItem[bool]):
    @staticmethod
    def parse_value(value: Any) -> bool:
        return value == "true"

    def to_json(self, depth: int = 1) -> str:
        return "\t" * depth + f"\"{self.name}\": {self.value}"


@dataclass
class JsonString(IJsonItem[str]):
    @staticmethod
    def parse_value(value: Any) -> str:
        return value

    def to_json(self, depth=1):
        return "\t" * depth + f"\"{self.name}\": \"{self.value}\""


@dataclass
class JsonArray(IJsonItem[list]):
    @staticmethod
    def parse_value(value: Any) -> list[Any]:
        return value

    def to_json(self, depth: int = 1) -> str:
        json_str = "\t" * depth + f"\"{self.name}\": [\n"
        array_len = len(self.value)

        for i in range(array_len):
            item: IJsonItem = self.value[i]

            if isinstance(item, JsonObject):
                tmp = item.to_json(depth + 1)
                json_str += "\t" * (depth + 1) + tmp[tmp.index("{"):]
            else:
                json_str += "\t" * (depth + 1) + str(item.value)

            if i < array_len - 1:
                json_str += ","
            json_str += "\n"

        json_str += "\t" * depth + "]"

        return json_str


@dataclass
class JsonObject(IJsonItem[list]):
    @classmethod
    def parse(cls, content: list[str]) -> "JsonObject":
        name = get_tag_name(content[0])
        value = cls.parse_value(content[1:-1])

        return cls(name=name, value=value)

    @staticmethod
    def parse_value(content: Any) -> list:
        result = []

        i = 0
        current_content = {}
        while i < len(content):
            if is_opening_tag(content[i]):
                opening_tag_name = get_tag_name(content[i])

                j = i + 1
                while j < len(content):
                    if is_closing_tag(content[j]) and get_tag_name(content[j]) == opening_tag_name:
                        break
                    j += 1

                if opening_tag_name in current_content:
                    current_content[opening_tag_name].append(parse_content(content[i:j + 1]))
                else:
                    current_content[opening_tag_name] = [parse_content(content[i:j + 1])]
                i = j
            i += 1

        for key, value in current_content.items():
            if len(current_content[key]) > 1:
                result.append(JsonArray(name=key, value=value))
            else:
                result.append(value[0])

        return result

    def to_json(self, depth: int = 1) -> str:
        json_str = "\t" * depth + f"\"{self.name}\": " + "{\n"
        content_len = len(self.value)

        for i in range(content_len):
            json_str += self.value[i].to_json(depth + 1)
            if i < content_len - 1:
                json_str += ","
            json_str += "\n"

        json_str += "\t" * depth + "}"

        return json_str


def get_tag_name(tag: str) -> str:
    return re.match(r"</?(.*)>", tag).group(1)


def is_opening_tag(tag: str) -> bool:
    return is_tag(tag) and tag[1] != "/"


def is_closing_tag(tag: str) -> bool:
    return is_tag(tag) and tag[1] == "/"


def is_tag(tag: str) -> bool:
    return tag[0] == "<" and tag[-1] == ">"


def get_content_type(content: list[str]) -> JsonItemType:
    if len(content) != 3:
        return JsonItemType.OBJECT

    value = content[1]
    if value in ("true", "false"):
        return JsonItemType.BOOLEAN
    elif re.match(r"^-?\d+$", value):
        return JsonItemType.INTEGER
    elif re.match(r"^-?\d+\.\d+$", value):
        return JsonItemType.FLOAT
    else:
        return JsonItemType.STRING


def parse_content(content: list[str]) -> IJsonItem:
    match get_content_type(content):
        case JsonItemType.INTEGER:
            return JsonIntegerNumber.parse(content)
        case JsonItemType.FLOAT:
            return JsonFloatNumber.parse(content)
        case JsonItemType.BOOLEAN:
            return JsonBoolean.parse(content)
        case JsonItemType.STRING:
            return JsonString.parse(content)
        case JsonItemType.OBJECT:
            return JsonObject.parse(content)


def split_into_tokens(xml: str) -> list[str]:
    return re.findall(r"</?[^<>/]*>|(?!\s)[^<>/]+?(?=\s*</)", xml)


def xml2json(xml: str) -> str:
    tokens = split_into_tokens(xml)
    json_object = JsonObject.parse(tokens).to_json()

    return "{\n" + json_object + "\n}"


def main() -> None:
    xml_path = Path(__file__).parent.joinpath("schedule.xml")
    json_path = Path(__file__).parent.joinpath("schedule_4.json")

    xml_schedule = xml_path.read_text(encoding="utf-8")
    json_schedule = xml2json(xml_schedule)

    json_path.write_text(json_schedule, encoding="utf-8")

if __name__ == "__main__":
    main()
