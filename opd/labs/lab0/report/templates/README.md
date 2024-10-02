
# Папка для шаблонов

В данной папке храняться шаблоны частей документа.

Шаблон это документ формата .tex(или любого текстового), который не будет непосредственно включен в документ. В шаблоне пишеться часть документа, в которую вписаны переменные. В дальнейшем данный шаблон будет обработан программой. В программе текст данного шаблона будет обработан и все переменные заменены на конкретные значения, вычесленные в результате работы программы. Программа не будет изменять сам шаблон, а лишь перепишет его текст, с подстановкой переменных, в новый файл. Новосозданный файл будет включен в итоговый документ командой ```\input{}```.


Обычно шаблон имеет имя: nameTemplate.tex. После обработки, программа сохраняет готовый файл под именем name.tex в папку с самим документом.

## Пример программы обрабатывающей шаблон:
```
def createReport(TemplateFileName, Ji1, Ji2, Ji3, Ji1PractFull, Ji2PractFull, Ji3PractFull, M1, M2, M3, dispM1Full1, dispM2Full1, dispM3Full1, avgSqrt1Full1, avgSqrt2Full1, avgSqrt3Full1, XIcrit, X1Full, X2Full, X3Full, dt, T1, T2):
    # Открытие шаблона
    f = open(r'../../../Лаба 3/Templates/' + TemplateFileName,'r',encoding='utf8')    
    try:
        # работа с файлом
        report = f.read()
        # Замена переменных в файле
        report = report.replace('Ji3errorFull',str(abs(Ji3PractFull - (Ji1 + Ji2))))
        report = report.replace('Ji1PractFull',str(Ji1PractFull))
        report = report.replace('Ji2PractFull',str(Ji2PractFull))
        report = report.replace('Ji3PractFull',str(Ji3PractFull))
        report = report.replace('dispM1Full1',str(dispM1Full1))
        report = report.replace('dispM2Full1',str(dispM2Full1))
        report = report.replace('dispM3Full1',str(dispM3Full1))
        report = report.replace('avgSqrt1Full1',str(avgSqrt1Full1))
        report = report.replace('avgSqrt2Full1',str(avgSqrt2Full1))
        report = report.replace('avgSqrt3Full1',str(avgSqrt3Full1))
        report = report.replace('Ji1error',str(abs(Ji1 - Ji1PractFull)))
        report = report.replace('Ji2error',str(abs(Ji2 - Ji2PractFull)))
        report = report.replace('Ji3error',str(abs(Ji3 - Ji3PractFull)))
        report = report.replace('X1Full',str(X1Full))
        report = report.replace('X2Full',str(X2Full))
        report = report.replace('X3Full',str(X3Full))
        report = report.replace('XIcrit',str(XIcrit))
        report = report.replace('Ji1',str(Ji1))
        report = report.replace('Ji2',str(Ji2))
        report = report.replace('Ji3',str(Ji3))
        report = report.replace('M1',str(M1))
        report = report.replace('M2',str(M2))
        report = report.replace('M3',str(M3))
        report = report.replace('dt',str(dt))
        report = report.replace('T2',str(T2))
        report = report.replace('T1',str(T1))
        
    finally:
        f.close()
    
    # Сохранение нового файла в папке с документом
    newFileName = TemplateFileName.replace('Template','') # Убираем из имени файла "Template"
    # Создаем новый файл, в который запишем измененный шаблон
    f = open(r'../../../Лаба 3/' + newFileName,'w',encoding='utf8')
    try:
        # работа с файлом
        f.write(report)        
    finally:
        f.close()
```