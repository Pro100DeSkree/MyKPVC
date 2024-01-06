import io
import json
from PIL import Image, ImageDraw, ImageFont

# Встановлення шрифту та розміру тексту
fontsDir = ""
font_name = 'Roboto-Regular.ttf'
bold_font_name = 'Roboto-Regular.ttf'
font_size = 40

# Ширина лінії таблиці
line_thickness_table = 2

# відступ від краю зображення
padding = 0

# Встановлення кольорів
text_color = (138, 138, 138,)
table_border = "gray"
background = (28, 27, 31,)
blue = (0, 0, 179,)
yellow = (163, 163, 0,)


# def set_font(font_name_l=font_name):
#     global font_name
#     font_name = font_name_l
#     return True
#
#
# def set_font_size(font_size_l=font_size):
#     global font_size
#     font_size = font_size_l
#     return True
#
#
# def set_background(*background_l):
#     global background
#     background = background_l
#     return True
#
#
# def set_text_color(text_color_l=text_color):
#     global text_color
#     text_color=text_color_l
#     return True
#
#
# def set_table_border(table_border_l=table_border):
#     global table_border
#     table_border = table_border_l
#     return True
#
#
# def set_colors_flag(blue_l=blue, yellow_l=yellow):
#     global blue, yellow
#     blue=blue_l
#     yellow=yellow_l
#     return True



def get_img_table_schedule_ch(changes, fonts_dir):
    global fontsDir
    fontsDir = fonts_dir

    changes = json.loads(changes)
    head = changes["head"]
    body = changes["body"]
    multi_column_raw = body["multi_column"]

    headers = ['Гр.', 'Пара', 'За розкладом', 'За зміною', 'Авд.']
    one_column = body["one_column"]
    multi_column = []
    title = f"На {head['date']} ({head['weekday']}, {head['numerator_or_denominator']})"

    one_column.insert(0, title)
    multi_column.insert(0, headers)

    for row in multi_column_raw:
        row = list(row.items())
        multi_column.append(
            [str(row[0][1]), str(row[1][1]), row[2][1], row[3][1], str(row[4][1])]
        )

    if head["is_changes"]:
        return __get_table(multi_column, headers, one_column)
    else:
        return None


def __get_table(data: list, headers, one_column):
    # Встановлення шрифту та розміру тексту
    font = ImageFont.truetype(f"{fontsDir}/{font_name}", font_size)
    bold_font = ImageFont.truetype(f"{fontsDir}/{bold_font_name}", font_size)

    # --------------------------РОЗРАХУНКИ РОЗМІРІВ ТАБЛИЦІ І ВІДПОВІДНО ЗОБРАЖЕННЯ---------------------

    # Якщо data_practice не порожній то визначаємо найдовший рядок
    max_len_data_practice = 0
    if one_column:
        max_len_data_practice = (max(int(font.getlength(text)) for text in one_column)) + 20

    # Визначення максимальної довжини тексту у кожному стовпці
    max_col_widths = []
    for i in range(len(headers)):
        col_texts = [header[i] for header in [headers] + data]
        max_col_widths.append(max([int(font.getlength(text)) for text in col_texts]))

    # Визначення ширини та висоти клітинок
    sum_cell_width = sum(list([max_col_widths[i] for i in range(len(headers))]))
    if max_len_data_practice > sum_cell_width:
        len_cell = (max_len_data_practice - sum_cell_width) // int(len(headers))
        cell_widths = list([max_col_widths[i] + len_cell for i in range(len(headers))])
    else:
        cell_widths = list([max_col_widths[i] + 20 for i in range(len(headers))])
    word_height = font.getbbox('A')[-1]
    cell_height = word_height + 20

    # Визначення кількості рядків
    row_count = len(data) + len(one_column)

    # Визначення ширини та висоти таблиці
    table_width = sum(cell_widths)
    table_height = row_count * cell_height

    # Визначення розміру зображення з урахуванням падінгу та товщини ліній таблиці
    img_width = table_width + (padding * 2) + (line_thickness_table * 2)
    img_height = table_height + (padding * 2) + (line_thickness_table * 2)
    img = Image.new('RGB', (img_width, img_height), color=background)
    # Створення об'єкта ImageDraw для малювання на зображенні
    d = ImageDraw.Draw(img)
    # -------------------------------------МАЛЮВАННЯ ТАБЛИЦІ І ЗАПОВНЕННЯ ЇЇ--------------------------------------

    d.rectangle((padding, padding, img_width, img_height), outline=table_border,
                        width=5)

    # Жовто-блакитний
    for i in range(1, 14):
        if i <= 6:
            d.line(xy=((table_width + padding, line_thickness_table + padding + i - 1),
                       (5, line_thickness_table + padding + i - 1)), fill=blue)
        else:
            d.line(xy=((table_width + padding, line_thickness_table + padding + i - 1),
                       (5, line_thickness_table + padding + i - 1)), fill=yellow)

    first_point_x = padding
    first_point_y = padding
    second_point_y = padding

    # Малювання тайтла та "однорядкові" зміни
    for row_index, text in enumerate(one_column):
        second_point_x = table_width + padding
        second_point_y = second_point_y + cell_height

        d.rectangle((second_point_x, second_point_y, first_point_x, first_point_y), outline=table_border,
                    width=line_thickness_table)

        first_point_y = first_point_y + cell_height

        title_length = font.getlength(text)
        title_point_x = (table_width // 2) - (title_length // 2) + padding
        title_point_y = (second_point_y - cell_height // 2) - (word_height // 2) - padding
        d.text(xy=(title_point_x, title_point_y+1), text=text, font=font, fill=text_color)

    for row_index, row in enumerate(data):
        second_point_y = second_point_y + cell_height
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
            cell_center_y = second_point_y - cell_height // 2

            # Визначення зміщення тексту відносно центру
            text_point_x = cell_center_x - (word_length // 2)
            text_point_y = cell_center_y - (word_height // 2) - 10

            if row_index == 0:
                d.text((text_point_x, text_point_y+4), text=text, font=bold_font, fill=text_color)
            else:
                d.text((text_point_x, text_point_y+4), text=text, font=font, fill=text_color)

    image_bytes = io.BytesIO()
    img.save(image_bytes, format="JPEG")
    image_bytes.seek(0)
    return image_bytes.getvalue()

#TODO: Якщо зобр меньше за 1080пх то підганяти його під цей розмір