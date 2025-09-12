from pathlib import Path
from typing import Any


def xml2dict(xml: str) -> dict[str, Any]:
    def get_tag_content(xml_str, tag) -> tuple[str | None, str]:
        start_tag = f"<{tag}>"
        end_tag = f"</{tag}>"
        start_idx = xml_str.find(start_tag) + len(start_tag)
        end_idx = xml_str.find(end_tag)
        if start_idx == -1 or end_idx == -1:
            return None, xml_str

        content = xml_str[start_idx:end_idx].strip()
        remaining_xml = xml_str[end_idx + len(end_tag):]

        return content, remaining_xml

    def parse_element(xml_str) -> dict[str, Any]:
        result = {}
        while "<" in xml_str and ">" in xml_str:
            start = xml_str.find("<") + 1
            end = xml_str.find(">", start)
            tag = xml_str[start:end]
            content, xml_str = get_tag_content(xml_str, tag)

            if "<" in content and ">" in content:
                content = parse_element(content)

            if tag in result:
                if not isinstance(result[tag], list):
                    result[tag] = [result[tag]]
                result[tag].append(content)
            else:
                result[tag] = content
        return result

    root_tag = xml[xml.find("<") + 1:xml.find(">")]
    root_content, _ = get_tag_content(xml, root_tag)

    return {root_tag: parse_element(root_content)}


def dict2json(data: dict[str, Any]) -> str:
    def convert(value):
        if isinstance(value, dict):
            items = [f'"{key}": {convert(val)}' for key, val in value.items()]
            return "{" + ", ".join(items) + "}"
        elif isinstance(value, list):
            items = [convert(item) for item in value]
            return "[" + ", ".join(items) + "]"
        elif isinstance(value, str):
            return f'"{value}"'
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
    json_path = Path(__file__).parent.joinpath("schedule_1.json")

    xml_schedule = xml_path.read_text(encoding="utf-8")
    json_schedule = xml2json(xml_schedule)

    json_path.write_text(json_schedule, encoding="utf-8")


if __name__ == "__main__":
    main()
