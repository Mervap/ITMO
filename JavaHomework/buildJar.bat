SET home=D:\ITMO\JavaHomework
SET package=ru.ifmo.rain.teplyakov.implementor
SET out=out\production\JavaHomework

cd %home%
javac -d %out% -p lib;artifacts; --module-source-path myModules --module %package%

cd %out%\%package%
jar -c -f %home%\artifacts\%package%.jar -e %package%.Implementor .

cd %home%