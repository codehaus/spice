Welcome to Excalibur CLI!

As always, the if you have any questions :

1) Make sure you followed any directions
2) Review documentation included in this package, or online at
      http://jakarta.apache.org/avalon/excalibur
3) Ask on the avalon-dev list.  This is a great source of support
   information. To join, read http://jakarta.apache.org/site/mail.html
   and then follow the link at the bottom to join the lists.

noalias
-------
This simple example shows how to set it up so that only the long form
or only short form of an option is capable of being used.

Compile it by running

javac -classpath ../../build/lib/@dist.name@.jar *.java (Unix)

or

javac -classpath ..\..\build\lib\@dist.name@.jar *.java (Windows)

Run it using the scripts provided:

  java -classpath ../../build/lib/@dist.name@.jar:. NoAlias (Unix)

or

  java -classpath ..\..\build\lib\@dist.name@.jar;. NoAlias (Windows)


Try passing it the "--help" command line option to see which other
command line options are available. The experiment by passing in
different options.

Some combinations to try out include

--help
-h
h
--version
