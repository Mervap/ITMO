SET home=D:\ITMO\JavaHomework
SET link=https://docs.oracle.com/en/java/javase/11/docs/api/
SET impPackage=modules\info.kgeorgiy.java.advanced.implementor\info\kgeorgiy\java\advanced\implementor
SET package=ru.ifmo.rain.teplyakov.implementor

cd %home%

javadoc -d javadoc -p lib;modules;myModules -link %link% -private -version -author --module-source-path modules;myModules --module %package% %impPackage%\Impler.java %impPackage%\JarImpler.java %impPackage%\ImplerException.java