# xmlParser
u need file fields.xml out of project (jar)

<Поля>
    <Общее>
        <Улица xpath="//Address/Street">Type,Name</Улица>
        <Дом xpath="//Address/Level1/@Value"/>
        <Корпус xpath="//Address/Level2/@Value"/>
        <Литера xpath="//Address/Level3/@Value"/>
        <Этаж xpath="//Level/@Number"/>
        <Квартира xpath="//Address/Apartment/@Value"/>
        <Площадь_квартиры xpath="//Area/text()" entity="String" type="text"/>
    </Общее>
    <Права xpath="//ExtractObject/ObjectRight/Right">
        <ФИО xpath="/Owner"/>
        <Номер_свидетельства xpath="/Registration/RegNumber/text()"/>
        <Дата_свидетельства xpath="/Registration/RegDate/text()"/>
        <Доля_собственника xpath="/Registration/ShareText/text()"/>
        <Тип_собственности xpath="/Registration/Name/text()"/>
    </Права>
    <Последовательность>
        Улица,
        ФИО,
        Дом,
        Корпус,
        Литера,
        Этаж,
        Квартира,
        Площадь дома,
        Площадь_квартиры,
        Номер_свидетельства,
        Дата_свидетельства,
        Доля_собственника,
        Тип_собственности
    </Последовательность>
</Поля>
