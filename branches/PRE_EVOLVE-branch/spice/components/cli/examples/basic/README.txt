Welcome to Excalibur CLI!

As always, the if you have any questions :

1) Make sure you followed any directions
2) Review documentation included in this package, or online at
      http://jakarta.apache.org/avalon/excalibur
3) Ask on the avalon-dev list.  This is a great source of support
   information. To join, read http://jakarta.apache.org/site/mail.html
   and then follow the link at the bottom to join the lists.

basic
-----
This simple example shows how to use the CLI Component for basic command
line parsing. It also demonstrates generating a usage message from list
of options.

Compile it by running

javac -classpath ../../build/lib/@dist.name@.jar *.java (Unix)

or

javac -classpath ..\..\build\lib\@dist.name@.jar *.java (Windows)

Run it using the scripts provided:

  java -classpath ../../build/lib/@dist.name@.jar:. BasicCLI (Unix)

or

  java -classpath ..\..\build\lib\@dist.name@.jar;. BasicCLI (Windows)


Try passing it the "--help" command line option to see which other
command line options are available. The experiment by passing in
different options.

Some combinations to try out include

--help
-h
h
--version
