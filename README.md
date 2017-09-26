# Java Course
This repository contains solved homework problems from the **Java Course** at the Faculty of Electrical Engineering and Computing, held by Marko Čupić.

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

## Homework 7 - Boolean Expressions Parser
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

## Homework 9 - Multithreading

## Homework 10 - Introduction to Swing

## Homework 11 - Simple Text Editor

## Homework 12 - Web Server

## Homework 13 - Java Server Pages

## Homework 14 - SQL Database

## Homework 15 - Simple Blog with Java Persistence API

## Homework 16 - Search engine and Paint

## Homework 17 - Android

## Homework 18 - Web front-end
