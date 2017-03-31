# Dynatrace Command Line Tool


### Table of Contents
- [Description](#anchor_description)
- [General options](#anchor_general-options)
- [Custom properties file](#anchor_custom-properties-file)
- [Server options](#anchor_server-options)
- [System profile options](#anchor_system-profile-options)
- [Building](#anchor_building) 
    - [Running tests](#anchor_tests)

#### <a name="anchor_description"></a> Description
AppMon offers an easy to use command line interface for basic server operations. You can execute basic operations without an installed client. It's a less powerful alternative to the [JMX Management](http://www.dynatrace.com/support/doc/appmon/shortlink/id_jmx_management). The command line tool operates via web services that you enable in the AppMon Server. For more information, see [Set up Communication Connections](http://www.dynatrace.com/support/doc/appmon/shortlink/id_set_up_communication_connections).

To connect to the AppMon Server, the host name and integrated web server port have to be specified. If they are not specified, AppMon uses *localhost:8020*, by default, or port *8021* for a SSL connection. For more information, see [General Options](#anchor_general-options). Starting with version 6.3.x the default connection type is *https*. Use the `-nossl` option to connect via *http*.

Windows batch file (_dtcmd.cmd_), bash script (_dtcmd.sh_), and a c-shell script (_dtcmd.csh_) to use on Windows, Linux and Unix systems, are provided in public repository. You can also run the program with following command:
```
java -jar dynatrace-cmd-7.0.0.jar
```

**dtcmd** is deprecated for upgrade or migration use. Use the [dynatrace-migration Tool](https://files.dynatrace.com/downloads/migrationtool/dynatrace-migration.jar) (link to the latest version) instead.

If **dtcmd** is called without command line parameters, it displays the following applicability:
```

------------------------------------------------------------
                 Dynatrace Commandline Tool                 
             Copyright (C) 2004-2017 Dynatrace              
------------------------------------------------------------
                        Version 7.0.0                       

No command specified

Usage: dtcmd [options] [command] [command options]
  Options:
    -dthome
      Dynatrace home installation directory
  Commands:
    -startup      starts up local server
      Usage: -startup [options]
        Options:
          -verbose
            provide a more detailed output (in case of errors)

    -restart      restarts the Dynatrace Server
      Usage: -restart [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -shutdown      shuts down the Dynatrace Server
      Usage: -shutdown [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -listprofiles      display system profiles
      Usage: -listprofiles [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -enable      enables the system profile
      Usage: -enable [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
        * -profile
            specifies the system profile to work on
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -disable      disables the system profile
      Usage: -disable [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
        * -profile
            specifies the system profile to work on
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -activeconf      activates specified configuration
      Usage: -activeconf [options] [<hostname>[:<port>]]
        Options:
        * -c, -config
            configuration to activate
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
        * -profile
            specifies the system profile to work on
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -startsession      record a new session, using optional sessionname (max. 
            80 chars)
      Usage: -startsession [options] [<hostname>[:<port>]]
        Options:
          -f
            session name
          -lock
            prevents the session from being deleted on cleanups triggered by 
            low disk space
          -nossl
            disables communication via HTTPS
          -notimestamp
            specifies to not append timestamp to sessionname
          -pass
            password to log in with
        * -profile
            specifies the system profile to work on
          -recordingoption
            recording option of the session file
            Default: all
            Possible Values: [all, violations, timeseries]
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)

    -stopsession      stop recording session
      Usage: -stopsession [options] [<hostname>[:<port>]]
        Options:
          -nossl
            disables communication via HTTPS
          -pass
            password to log in with
        * -profile
            specifies the system profile to work on
          -user
            username to log in with
          -verbose
            provide a more detailed output (in case of errors)
```

#### <a name="anchor_general-options"></a> General Options
Use the following options in combination with the [Server](#anchor_server-options) or [System Profile Options](#anchor_system-profile-options):
* **-user**: Specifies the username.
* **-pass**: Specifies the password.
* **-nossl**: Disables SSL for communication with the AppMon Server.
* **-verbose**: Turns on the generation of detailed data output for the issued command.
* **\<hostname\>**: Specifies the host name of the AppMon Server for which the command is issued. If you don't specify a host name, AppMon uses localhost.
* **\<port\>**: Specifies the port to which the [Built-in Webserver](http://www.dynatrace.com/support/doc/appmon/shortlink/id_set_up_communication_connections#anchor_builtin) at the AppMon Server is listening. If no port is specified, AppMon uses the default port *8020* (or *8021* for SSL).

#### <a name="anchor_custom-properties-file"></a> Custom properties file
Some options are set by the custom properties file *cmd.properties*, which is placed in the same directory as **dtcmd**. The following options can be used in *cmd.properties*:
```
server.host=<hostname of dynaTrace Server>
server.port.http=<http port of dynaTrace Server>
server.port.https=<https port of dynaTrace Server>
server.ssl=<true|false>
javax.net.ssl.trustStore=<relative path of trust store>
javax.net.ssl.trustStorePassword=<password of trust store>
user=<username to log in dynaTrace Server>
pass=<password to log in dynaTrace Server>
```
If an option in your custom *cmd.properties* is empty or missing, AppMon uses the corresponding option of default *cmd.properties*:
```
server.host=localhost
server.port.http=8020
server.port.https=8021
server.ssl=true
javax.net.ssl.trustStore=keystore.jks
javax.net.ssl.trustStorePassword=<password of trust store>
user=
pass=
```
If you use the corresponding command line parameters (`-ssl`, `-user`, `-pass`, `<hostname>[:<port>]`), these parameters are applied. If the command line parameter `<hostname>` is used, AppMon ignores the custom *cmd.properties* `server.*` options.

#### <a name="anchor_server-options"></a> Server options
* **-startup**: Starts an instance of the AppMon Server as a Windows service. You must run `dtcmd -startup` with administrator rights.
* **-restart**: Restarts the AppMon Server that is specified by the host name, port, and **`DT_HOME`**. Generates an error message if any running AppMon Server has no specified parameters.
* **-shutdown**: Stops the specified server instance and generates an error message, if an appropriate running AppMon Server is not found.
* **-listprofiles**: Lists the currently AppMon Server [System Profile](http://www.dynatrace.com/support/doc/appmon/shortlink/id_system_profile)s that are available.

#### <a name="anchor_system-profile-options"></a> System Profile options
* **-enable**: Enables the specified System Profile.
* **-disable**: Disables the specified System Profile.
* **-activeconf -c \<configuration\>**: Changes the configuration of the selected [System Profile](http://www.dynatrace.com/support/doc/appmon/shortlink/id_system_profile) to the provided configuration. Prints an error message if <configuration> does not exist.
* **-startsession [-f \<sessionname\>] [-recordingoption \<recoption\>] [-lock]**: Starts recording of incoming PurePaths to a session file, using the provided session name as file name. A default name is generated if the parameter sessionname is omitted.
* **-stopsession**: Stops recording of incoming PurePaths. Prints an error message if recording is currently not active.

#### <a name="anchor_building"></a> Building
In order to build the library, one must execute `mvn clean install` or `mvn clean package`.

##### <a name="anchor_tests"></a> Running tests
This tool comes with some unit tests. To run them, execute the following command: `mvn clean test`.
