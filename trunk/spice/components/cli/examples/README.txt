Welcome to Spice CLI!

This directory contains a number of examples for the component.
They are;

basic
-----
This simple example shows how to use the CLI Component for basic command
line parsing. It also demonstrates generating a usage message from list
of options.

option_arguments
----------------
This simple example shows how to have options, requiring an argument,
optionally supporting an argument or requiring 2 arguments.

noalias
-------
This simple example shows how to set it up so that only the long form
or only short form of an option is capable of being used.

incompat_options
----------------
This simple example shows how to make options incompatible with one another.
In the application it demonstrates how to make two command line options
(quiet and verbose) incompatible with each other.

multiple_parsers
----------------
This example shows you how to use multiple different parsers to parse
a command line. This is useful in cases where the command line contains
a directive that may need different parameters. An example of such a case
being the cvs command. The cvs command has "global" parameters like
"verbose" then a command (such as "commit" or "update") and then a set of
command specific parameters. This shows how CLI handles a similar case.
