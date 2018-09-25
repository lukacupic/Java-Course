# Java Course
This repository contains solved homework problems from the **Java Course** at the Faculty of Electrical Engineering and Computing, held by lecturer doc. dr. sc. Marko Čupić.

## Homework 1 - Introduction to Java
* wrote a few simple programs in Java; no object-oriented paradigm is used here

## Homework 2 - Custom Collections
* implemented a custom array-backed and linked-list-backed collections
* created a ComplexNumber class, which supports various features, including parsing a string into a ComplexNumber

## Homework 3 - Lexer and Parser
* implemented a lexer and a parser for a custom script language - SmartScript

## Homework 4 - Hash Table and Simple Database
* implemented a custom hash table collection, which uses a linked list for collision resolution
* wrote a simple database emulator (which relies on the previously created collection) which allows query commands for fetching various items from the database

## Homework 5 - Collections
* implemented a simple notification system for numerical changes which uses the **Observer design pattern**
* created a collection for generating prime numbers
* created the ObjectMultistack collection which behaves in a map-like way, but instead of mapping a single key to a single value, it maps a key to a stack of values. Talk about abstraction...
* used the Java Stream API for manipulating various collections

## Homework 6 - Files
* created a program which allows encryption and decryption of files using the AES algorithm and offers calculation and verification of the SHA-256 file digest.
* implemented a custom shell interface, MyShell, with which the user can interact with. This is a list of available commands:
  * cat - writes the contents of the specified file onto the console
  * copy - copies the specified file to another location
  * exit - terminates the shell
  * hexdump - dumps a hex representation of the contents of the specified file onto the console
  * charsets - lists all available system charsets onto the console
  * ls - lists all files and directories in the specified directory
  * mkdir - creates a new directory on the specified location
  * symbol - prints out or replaces the shell prompt symbol
  * tree - writes a tree-like structure of the specified directory to the standard output, recursively listing all files and subdirectories under the given directory
  * help - provides help for the specified command (```help help``` is also allowed — how meta is that?!)

## Homework 7 - Boolean Expression Parser
* implemented a lexer which accepts the following logical operators: AND, OR, NOT, XOR
* implemented a recursive descent parser which supports the following grammar:
```S  -> E1
E1 -> E2 (OR E2)*
E2 -> E3 (XOR E3)*
E3 -> E4 (AND E4)*
E4 -> NOT E4 | E5
E5 -> VAR | CONST | '(' E1 ')'
```
* implemented a truth table generator
* implemented an expression evaluator for evaluating boolean expressions
* implemented a few other boolean expression details

## Homework 8 - Quine McCluskey Minimizer
* implemented the Quine McCluskey algorithm for minimizing boolean functions
* the minimizer uses the services of the Boolean Expression Parser implemented in the previous homework, for evaluating boolean expressions during the minimization
* implemented a logger which displays the current progress of the algorithm (to the console by default, but this can be changed by modifying the ```logging.properties``` file

## Homework 9 - Multithreading
* implemented a simple library for working with 3D vectors, complex numbers and polynomials
* using the previously written complex number and polynomial libraries, implemented an algorithm for calculating Newton-Raphson fractals and displaying the obtained result visually
* using the 3D vector library, implemented a ray-caster for rendering 3D scenes and visually displaying the obtained result

## Homework 10 - Introduction to Swing
* implemented a custom layout manager - CalcLayout - for managing the elements of a custom calculator
* implemented a custom calculator which supports default calculating operations (addition, subtraction, multiplication, division) as well as some scientific operations (trigonometric and inverse trigonometric functions, exponents, logarithms etc.)
* implemented a bar chart for dynamically displaying a collection of (x, y) pairs
* implemented a simple generator of prime numbers in Swing

## Homework 11 - Simple Text Editor
* created JNotepad++ - a simple Notepad++ clone with the following funcionality:
  * creating a new blank document,
  * opening existing document,
  * saving document,
  * saving-as document,
  * closing currently open document,
  * cut/copy/paste text,
  * statistical info,
  * exiting application.
  * various tools such as changing case, sorting the lines, removing duplicate lines etc.
* upgraded the basic version of the program to support internationalization (i18n): the program supports Croatian, English and German languages

## Homework 12 - Web Server
* created SmartScriptHttpServer - a multithreaded HTTP server
* the server supports executing SmartScript script files, by using the SmartScript parser implemented in the Homework #03
* server properties are configured in the file ```server.properties``` and it's supported MIME types are configured in the ```mime.properties``` file
* implemented several "workers" - simple programs whose job it is to produce some result and deliver it to the user. The implemented workers are:
  * SumWorker - obtains URL parameters from the user, performs an addition operation among them and displays the result in a table on an HTML document which is delivered to the user
  * HelloWorker - displays a simple "Hello World" message when a user navigates to a certain URL of the server
  * CircleWorker - obtains URL parameters from the user representing several circle parameters, creates the circle and sends it back to the user in the image/png MIME type format (a .png image)
* implemented Cookie handling - the user can access a certain resource on the server, and the server will remember this user in a session map for a certain amount of time (defined in ```server.properties```)

## Homework 13 - Java Server Pages

## Homework 14 - SQL Database

## Homework 15 - Simple Blog with Java Persistence API

## Homework 16 - Search engine and Paint

## Homework 17 - Android

## Homework 18 - Web front-end
