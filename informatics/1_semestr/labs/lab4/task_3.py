import re
from pathlib import Path


def xml2dict(xml):
    def parse_element(element):
        result = {}
        pattern = r"<(\w+)>(.*?)</\1>|<(\w+)>(.*?)</\3>|<(\w+)/>"

        for match in re.finditer(pattern, element, re.DOTALL):
            tag = match.group(1) or match.group(3) or match.group(5)
            content = match.group(2) or match.group(4) or ''
            if re.search(r"<\w+>", content):
                content = parse_element(content)

            if tag in result:
                if not isinstance(result[tag], list):
                    result[tag] = [result[tag]]
                result[tag].append(content)
            else:
                result[tag] = content
        return result

    root_match = re.match(r"<(\w+)>(.*?)</\1>", xml, re.DOTALL)
    if root_match:
        root_tag = root_match.group(1)
        root_content = root_match.group(2)
        return {root_tag: parse_element(root_content)}
    else:
        return {}


def dict2json(data):
    def convert(value):
        if isinstance(value, dict):
            items = [f'"{key}": {convert(val)}' for key, val in value.items()]
            return "{" + ", ".join(items) + "}"
        elif isinstance(value, list):
            items = [convert(item) for item in value]
            return "[" + ", ".join(items) + "]"
        elif isinstance(value, str):
            # Экранирование кавычек внутри строк
            escaped_str = re.sub(r'(["\\])', r'\\\1', value)
            return f'"{escaped_str}"'
        elif isinstance(value, (int, float)):
            return str(value).lower()
        elif value is None:
            return "null"
        elif isinstance(value, bool):
            return "true" if value else "false"
        else:
            raise TypeError(f"Unsupported data type: {type(value)}")

    return convert(data)


def xml2json(xml: str) -> str:
    parsed_data = xml2dict(xml)
    json_data = dict2json(parsed_data)

    return json_data


def main() -> None:
    xml_path = Path(__file__).parent.joinpath("schedule.xml")
    json_path = Path(__file__).parent.joinpath("schedule_3.json")

    xml_schedule = xml_path.read_text(encoding="utf-8")
    json_schedule = xml2json(xml_schedule)

    json_path.write_text(json_schedule, encoding="utf-8")


if __name__ == "__main__":
    main()
