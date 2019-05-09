SET home=D:\ITMO\JavaHomework
SET package=ru.ifmo.rain.teplyakov.implementor
SET out=out\production\JavaHomework

cd %home%\src
javac -d ..\%out% ru\ifmo\rain\teplyakov\test\%~1.java

cd %home%