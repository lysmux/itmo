#!/usr/bin/bash

cd lab0

# Cкопировать рекурсивно директорию parasect2 в директорию lab0/crawdaunt7/nincada
chmod -R u+r parasect2/
chmod u+w crawdaunt7/nincada/
cp -r parasect2 crawdaunt7/nincada
chmod 335 parasect2/
chmod 752 parasect2/flaaffy/
chmod 640 parasect2/seel
chmod 440 parasect2/vaporeon
chmod 752 parasect2/darumaka/
chmod 046 parasect2/pikachu
chmod 044 parasect2/accelgor

# Скопировать файл camerupt4 в директорию lab0/crawdaunt7/duosion
chmod u+r camerupt4
chmod u+w crawdaunt7/duosion
cp camerupt4 crawdaunt7/duosion
chmod u-r camerupt4
chmod u-w crawdaunt7/duosion

# Создать жесткую ссылку для файла conkeldurr6 с именем lab0/crawdaunt7/slakingconkeldurr
ln conkeldurr6 crawdaunt7/slakingconkeldurr

# Создать символическую ссылку c именем Copy_5 на директорию zorua3 в каталоге lab0
ln -s zorua3/ Copy_5

# Объеденить содержимое файлов lab0/zorua3/golduck, lab0/parasect2/accelgor, в новый файл lab0/camerupt4_76
chmod u+r parasect2/accelgor
cat zorua3/golduck parasect2/accelgor > camerupt4_76
chmod u-r parasect2/accelgor

# Скопировать содержимое файла conkeldurr6 в новый файл lab0/parasect2/accelgorconkeldurr
cp conkeldurr6 parasect2/accelgorconkeldurr

# Создать символическую ссылку для файла camerupt4 с именем lab0/crawdaunt7/slakingcamerupt
ln -s camerupt4 crawdaunt7/slakingcamerupt
