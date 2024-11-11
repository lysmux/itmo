import json
from pathlib import Path

import xmltodict


def xml2json(xml: str) -> str:
    parsed_data = xmltodict.parse(xml)
    json_data = json.dumps(parsed_data, ensure_ascii=False)

    return json_data


def main() -> None:
    xml_path = Path(__file__).parent.joinpath("schedule.xml")
    json_path = Path(__file__).parent.joinpath("schedule_2.json")

    xml_schedule = xml_path.read_text(encoding="utf-8")
    json_schedule = xml2json(xml_schedule)

    json_path.write_text(json_schedule, encoding="utf-8")


if __name__ == "__main__":
    main()
