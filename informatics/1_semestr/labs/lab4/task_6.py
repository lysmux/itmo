import csv
from io import StringIO
from pathlib import Path

from xml.etree import ElementTree


def xml2csv(xml: str) -> str:
    root = ElementTree.fromstringlist(xml)
    io = StringIO()
    writer = csv.writer(io)

    headers = [
        "weekday",
        "lesson_name",
        "lesson_type",
        "location",
        "teacher",
        "start_at",
        "end_at"
    ]
    writer.writerow(headers)

    for weekday in root.iter("weekday"):
        day_name = weekday.find("name").text

        for lesson in weekday.iter("lesson"):
            lesson_name = lesson.find('name').text
            lesson_type = lesson.find('type').text
            location = lesson.find('location').text
            teacher = lesson.find('teacher').text
            start_at = lesson.find('start_at').text
            end_at = lesson.find('end_at').text

            writer.writerow([
                day_name,
                lesson_name,
                lesson_type,
                location,
                teacher,
                start_at,
                end_at
            ])


    return io.getvalue()



def main() -> None:
    xml_path = Path(__file__).parent.joinpath("schedule.xml")
    csv_path = Path(__file__).parent.joinpath("schedule.csv")

    xml_schedule = xml_path.read_text(encoding="utf-8")
    csv_schedule = xml2csv(xml_schedule)

    csv_path.write_text(csv_schedule, encoding="utf-8", newline="")


if __name__ == "__main__":
    main()
