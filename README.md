Simple TODO list REST API Example
=================================

This repository contains implementation of simple API for TODO list app.

Intended to be used as an example in VIA course (https://sites.google.com/a/via.felk.cvut.cz/via/).

# Eclipse Setup & Usage

## Prerequisites
* Installed Git (command line)
* Installed Maven (command line)
* Installed [Eclipse IDE for J2EE Developers](http://www.eclipse.org/downloads/)
* Installed [m2eclise plugin](http://eclipse.org/m2e/)
* Installed [EGit Plugin](http://www.eclipse.org/egit/)

## Project Checkout

1. Eclipse Menu: File->New->Other->Maven->Checkout Maven Projects from SCM
2.   Click on m2e Marketplace; install m2e-egit connector
3.   Setup SCM URL: git@github.com:filip26/VIATodoListApiExample.git and Finish

## Collaboration Support
1. Eclipse Package Explorer: right click on via-todo-api-example root directory
2.   Team->Share->Git
3.   Select 'Use or create repository in parent ...'
4.   Select project via-todo-api-example and Finish
   
## Run Configuration
1. Eclipse Package Explorer: right click on via-todo-api-example root directory
2.   Run As->Maven Build...
3.   Goals: jetty:run
4.   Apply and Run

# Command Line Setup & Usage

## Prerequisites
* Installed Git (command line)
* Installed Maven (command line)

## Project Checkout

```bash
$git clone git@github.com:filip26/VIATodoListApiExample.git
```

## Run
```bash
$cd VIATodoListApiExample
$mvn jetty:run
```
