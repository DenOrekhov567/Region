# Region
Region — это плагин для Nukkit и PowerNukkitX, добавляющий на Ваш сервер систему создания регионов блоками

## Команды  
<table>
  <tr>
    <th style="text-align:center">Команда</th>
    <th style="text-align:center">Следующий аргумент</th>
    <th style="text-align:center">Следующий аргумент</th>
    <th style="text-align:center">Следующий аргумент</th>
    <th style="text-align:center">Описание</th>
  </tr>
  <tr>
    <td rowspan="3" style="vertical-align:middle; text-align:center">/rg</td>
    <td style="text-align:center">addmember</td>
    <td style="text-align:center">регион: string</td>
    <td style="text-align:center">игрок: target</td>
    <td style="text-align:center">Добавляет игрока в регион</td>
  </tr>
  <tr>
    <td style="text-align:center">remmember</td>
    <td style="text-align:center">игрок: target</td>
    <td style="text-align:center">игрок: target || string</td>
    <td style="text-align:center">Удаляет игрока из региона</td>
  </tr>
  <tr>
    <td style="text-align:center">info</td>
    <td colspan="2" style="text-align:center">регион: string или void</td>
    <td style="text-align:center">Показывает информацию о регионе</td>
  </tr>
</table>

## Примеры использования
- /rg addmember регион игрок
- /rg remmember regionName игрок||никнейм
- /rg info регион||пусто

## Зависимости
- [DataManager](github.com/DenOrekhov567/Database)
- [Database](github.com/DenOrekhov567/Database)
- [FormAPI](github.com/DenOrekhov567/Database)
- [Rank](github.com/DenOrekhov567/Database)

## FAQ
Чтобы создать регион следуй этим шагам.
- Выбери блок, создающий регион(железный, золотой, алмазный)
- Поставь его на ту позицию, от радиуса которого ты хочешь установть регион
- Откроется форма, в форму введите название региона
- Отправьте форму  
Всё, Вы успешно создали регион
