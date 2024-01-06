import io
import json
import textwrap
from PIL import Image, ImageDraw, ImageFont

# Встановлення шрифту та розміру тексту
font_name = 'Roboto-Regular.ttf'
bold_font_name = 'Roboto-Regular.ttf'
font_size = 40

# Ширина лінії таблиці
line_thickness_table = 2

# відступ від краю зображення
padding = 5

# Встановлення кольорів
text_color = (138, 138, 138,)
table_border = "gray"
background = (28, 27, 31,)


def get_img_table_schedule(schedule, group, day: str, fonts_dir):
    global fontsDir
    fontsDir = fonts_dir

    schedule = json.loads(schedule)
    group_and_day = f"{group} - {day.capitalize()}"
    table_data = []
    headers = ["№", "Предмет", "Авд."]
    table_data.append(headers)

    for row_index in schedule:
        table_data.append([row_index, schedule[row_index]["lesson_and_teacher"], schedule[row_index]["classroom"]])
    return __get_table(table_data, headers, group_and_day)


def __get_table(data: list, headers, group_and_day):
    # Встановлення шрифту та розміру тексту
    font = ImageFont.truetype(f"{fontsDir}/{font_name}", font_size)
    bold_font = ImageFont.truetype(f"{fontsDir}/{bold_font_name}", font_size)

    # --------------------------РОЗРАХУНКИ РОЗМІРІВ ТАБЛИЦІ І ВІДПОВІДНО ЗОБРАЖЕННЯ---------------------

    # Визначення довжини першого рядка(№ групи, і день)
    len_group_and_day = int(font.getlength(group_and_day)) + 20

    # Визначення максимальної довжини тексту у кожному стовпці
    max_col_widths = []
    for i in range(len(headers)):

        # col_texts = [header[i] for header in [headers] + data]
        col_texts = []
        for header in [headers] + data:
            col_texts.append(header[i])

        # max_col_widths.append(max([int(font.getlength(text)) for text in col_texts]))
        max_width = 0
        for text in col_texts:
            width = int(font.getlength(str(text)))
            if width >= 1100:
                column_width = int(len(text) // 1.5)
                wrapped_text = textwrap.fill(text, column_width)
                half_line = wrapped_text.split("\n")
                width = max(
                    int(font.getlength(half_line[0])),  # Довжина першої половини рядка
                    int(font.getlength(half_line[1]))  # Довжина другої половини рядка
                )
            max_width = max(max_width, width)

        max_col_widths.append(max_width)

    # Визначення ширини та висоти клітинок
    # sum_cell_width = sum(list([max_col_widths[i] for i in range(len(headers))]))
    sum_cell_width = 0
    cell_widths = []
    for i in range(len(headers)):
        sum_cell_width += max_col_widths[i]

    if len_group_and_day > sum_cell_width:
        len_cell = (len_group_and_day - sum_cell_width) // int(len(headers))
        # cell_widths = list([max_col_widths[i] + len_cell for i in range(len(headers))])
        for i in range(len(headers)):
            cell_widths.append(max_col_widths[i] + len_cell)
    else:
        # cell_widths = list([max_col_widths[i] + 20 for i in range(len(headers))])
        for i in range(len(headers)):
            cell_widths.append(max_col_widths[i] + 20)

    word_height = font.getbbox('A')[-1]
    default_cell_height = word_height + 20

    # Визначення висоти кожного рядку
    cell_height = []
    for row_index, row in enumerate(data):
        if row_index == 0:
            cell_height.append(default_cell_height)
        else:
            width = int(font.getlength(row[1]))
            if width >= 1100:
                cell_height.append(default_cell_height * 2)
            else:
                cell_height.append(default_cell_height)

    # Визначення кількості рядків
    # row_count = len(data) + 1
    cell_height.append(default_cell_height)

    # Визначення ширини та висоти таблиці
    table_width = sum(cell_widths)
    table_height = sum(cell_height)

    # Визначення розміру зображення з урахуванням падінгу та товщини ліній таблиці
    img_width = table_width + (padding * 2) + (line_thickness_table * 2)
    img_height = table_height + (padding * 2) + (line_thickness_table * 2)
    img = Image.new('RGB', (img_width, img_height), color=background)
    # Створення об'єкта ImageDraw для малювання на зображенні
    d = ImageDraw.Draw(img)
    # -------------------------------------МАЛЮВАННЯ ТАБЛИЦІ І ЗАПОВНЕННЯ ЇЇ----------------------------------

    d.rectangle((padding, padding, img_width-padding, img_height-padding), outline=table_border,
                            width=5)

    first_point_x = padding
    first_point_y = padding
    second_point_y = padding

    # Малювання тайтла
    second_point_x = table_width + padding
    second_point_y = second_point_y + default_cell_height

    d.rectangle((second_point_x, second_point_y, first_point_x, first_point_y), outline=table_border,
                width=line_thickness_table)

    first_point_y = first_point_y + default_cell_height

    title_length = font.getlength(group_and_day)
    title_point_x = (table_width // 2) - (title_length // 2) + padding
    title_point_y = (second_point_y - default_cell_height // 2) - (word_height // 2) - padding
    d.text(xy=(title_point_x, title_point_y), text=group_and_day, font=font, fill=text_color)

    for row_index, row in enumerate(data):
        second_point_y = second_point_y + cell_height[row_index]
        second_point_x = padding
        first_point_x = padding
        for index, text in enumerate(row):
            # Визначення координат малювання клітинки
            second_point_x = second_point_x + cell_widths[index]

            d.rectangle((second_point_x, second_point_y, first_point_x, first_point_y), outline=table_border,
                        width=line_thickness_table)

            first_point_x = first_point_x + cell_widths[index]
            word_length = font.getlength(text)

            # Визначення центру клітинки
            cell_center_x = first_point_x - cell_widths[index] // 2
            cell_center_y = second_point_y - cell_height[row_index] // 2

            # Визначення зміщення тексту відносно центру
            text_point_x = cell_center_x - (word_length // 2)
            text_point_y = cell_center_y - (word_height // 2) - 10

            if cell_height[row_index] > 70 and word_length > 1100:
                column_width = int(len(text) // 1.5)
                text = textwrap.fill(text, column_width)
                half_line = text.split("\n")
                width = max(
                    int(font.getlength(half_line[0])),  # Довжина першої половини рядка
                    int(font.getlength(half_line[1]))  # Довжина другої половини рядка
                )

                text_point_x = cell_center_x - (width // 2)
                text_point_y = cell_center_y - default_cell_height + 10

            if row_index == 0:
                d.text((text_point_x, text_point_y+3), text=text, font=bold_font, fill=text_color, align="center")
            else:
                d.text((text_point_x, text_point_y), text=text, font=font, fill=text_color, align="center")

    image_bytes = io.BytesIO()
    img.save(image_bytes, format="JPEG")
    img.show()
    image_bytes.seek(0)
    return image_bytes.getvalue()
