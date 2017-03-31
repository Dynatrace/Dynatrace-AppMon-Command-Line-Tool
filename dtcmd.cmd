@echo off
rem Dynatrace Command Line Tool

rem Hand over DT_HOME environment variable as system property for Java 1.4
java -DDT_HOME="%DT_HOME%" -jar dynatrace-cmd-7.0.0.jar %*
