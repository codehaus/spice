Welcome to Excalibur CLI!

As always, the if you have any questions :

1) Make sure you followed any directions
2) Review documentation included in this package, or online at
      http://jakarta.apache.org/avalon/excalibur
3) Ask on the avalon-dev list.  This is a great source of support
   information. To join, read http://jakarta.apache.org/site/mail.html
   and then follow the link at the bottom to join the lists.

option_arguments
----------------
This simple example shows how to have options, requiring an argument,
optionally supporting an argument or requiring 2 arguments.

Compile it by running

javac -classpath ../../build/lib/@dist.name@.jar *.java (Unix)

or

javac -classpath ..\..\build\lib\@dist.name@.jar *.java (Windows)

Run it using the scripts provided:

  java -classpath ../../build/lib/@dist.name@.jar:. OptionArguments (Unix)

or

  java -classpath ..\..\build\lib\@dist.name@.jar;. OptionArguments (Windows)

Arguments to try

-f
-f x
-f x -S
-S
-Sx
-Ds
-Ds=x
-D s x
-D