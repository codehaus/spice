Welcome to Excalibur CLI!

As always, the if you have any questions :

1) Make sure you followed any directions
2) Review documentation included in this package, or online at
      http://jakarta.apache.org/avalon/excalibur
3) Ask on the avalon-dev list.  This is a great source of support
   information. To join, read http://jakarta.apache.org/site/mail.html
   and then follow the link at the bottom to join the lists.

incompat_options
----------------
This simple example shows how to make options incompatible with one another.
In the application it demonstrates how to make two command line options
(quiet and verbose) incompatible with each other.

Compile it by running

javac -classpath ../../build/lib/@dist.name@.jar *.java (Unix)

or

javac -classpath ..\..\build\lib\@dist.name@.jar *.java (Windows)

Run it using the scripts provided:

  java -classpath ../../build/lib/@dist.name@.jar:. IncompatOptions (Unix)

or

  java -classpath ..\..\build\lib\@dist.name@.jar;. IncompatOptions (Windows)

Try passing both quiet and verbose options to the command.
