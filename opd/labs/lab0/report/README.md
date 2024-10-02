# LatexTemplate

## Установка
Для работы LaTeX, необходимо скачать сам [LaTeX](https://miktex.org/download), скачать python и установить pygmentize(python -m pip install Pygments).

Далее необходимо обновить LaTeX до последней версии. Для этого открываем MiKTeX Console от имени администратора и скачиваем все пакеты и обновления. 
<span style="color:red"> Консоль может ругаться, но продолжит обновлять пакеты - **дождитесь обновления пакетов**.</span>

![image](https://user-images.githubusercontent.com/86663719/226546812-cee77cee-b2e5-440e-a901-532f247520f3.png)


Перейдите в командную строку windows и наберите:

```java
xelatex -version
```

Командная строка затупит, а потом доустановит нужные пакеты.

## Компиляция
Для компиляции в консоли набираем следующую команду: ```xelatex -shell-escape -halt-on-error -8bit _имя_файла_```

## Программа для работы
Для работы с LaTeX документами удобно использовать [TeXstudio](https://texstudio.sourceforge.net/).
Сразу после ее установки в настройках необходимо установить компилятор, который мы используем. Для этого переходим в Options→Configure TeXstudio…→Build→Default Compiler:

![image](https://user-images.githubusercontent.com/86663719/199741267-bfe09e86-5b81-4ea3-840b-f605b8ac88d7.png)

Далее идем в Commands и изменяем команду запуска нашего компилятора:

![image](https://user-images.githubusercontent.com/86663719/199741408-719dbdd5-56c0-4ebc-92f1-5ea7d56ac7d0.png)

Так же нужно включить проверку орфографии по [данному гайду](https://harrix.dev/blog/2013/spell-check-in-texstudio/).

## Скрипт создания нового LaTeX документа
Для создания LaTeX документа в один клик в можно написать следующий скрипт:
### Windows
```CreateLatexDoc.bat```
```
git clone https://github.com/BeetleRat/LatexTemplate.git
robocopy "LatexTemplate" "LatexTemplate\.." /e
RD /s "LatexTemplate" /Q
```
В Windows данный скрипт можно добавить в контекстное меню(ПКМ). Для этого открываем Редактор реестра(Win+R; Regedit). Переходим по пути HKEY_CLASSES_ROOT\directory\background\shell. В данном разделе создаем подраздел LaTeX. В параметре (По умолчанию) прописываем имя пункта меню:

![image](https://user-images.githubusercontent.com/86663719/200126990-7300bfbe-62bc-44ab-9977-6aa0858e9d50.png)

Создаем подраздел command. В параметре (По умолчанию) данного подраздела прописываем путь до скрипта ```CreateLatexDoc.bat```:

![image](https://user-images.githubusercontent.com/86663719/200127101-b1d48d5b-39ae-46cc-82a3-602de93e8d36.png)

Если все действия выполнены правильно, то в контекстном меню появится новый пункт, по нажатии на который в папку будет перенесено содержимое данного репозитория:

![image](https://user-images.githubusercontent.com/86663719/200127198-6d13cc80-6669-46c8-986d-8577f8ee03fb.png)
