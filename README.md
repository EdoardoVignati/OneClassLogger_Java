# OneClassLogger
An easy, complete and ready-to-use Java logger

## How to use

- Copy the ```OneClassLogger.java``` file in your Java source folder
- Create a ```log.options``` file (optional)
- Checkout  ```Main.java``` for some examples (optional)

## Features

- It's a self-contained one-class logger
- No third party dependencies required
- Fully customizable
- It works with jar packages
- Different levels of logging
- Logs on stdout or file
- Easy to use...very easy

## The options file

The file should be named ```log.options``` and it's not strictly required.

If you don't specify the path to this file, the logger will print on the standard output.

The options are compiled the first time when the OneClassLogger is built and are used till the end of the process.

You can specify the options file path in two ways:

- in the source code during the declaration of the logger
- as a JVM option with the ```-Dlog=/path/to/log.options``` flag 

The file has a very simple format. Each row determines a new logging rule.

A row is composed of the following three elements: 

- a Java regex that matches the fully qualified class/es name or package/s name
- the list of logging levels that you want to log separated by comma
- an appender

Each element is separated by a space/tab or combinations of them.

You can choose from the following levels: 

- INFO
- DEBUG
- WARN
- ERROR

You can choose from the following appenders:

- _CONSOLE
- _FILE:/path/to/my/app.log
- _DEVNULL

Examples:

```
my.package.MyClass		DEBUG,WARN		_CONSOLE
MyClass				WARN			_CONSOLE
mypackage.*			WARN,DEBUG		_FILE:/tmp/logs.txt
my.package.*			DEBUG			_FILE:/tmp/logs
```

The _FILE appender is designed to write in append mode (does not flush your old log file).

Please note that there is no order between levels unlike the log4j approach.

## Enjoy

OneClassLogger aspires to be easy to use, lightweight and powerful.

Feel free to contribute.

Many thanks if you keep the signature in the OneClassLogger header.
