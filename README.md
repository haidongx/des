# About this package

This package is an implementation of Discrete Event Simulation (DES) Framework that was the basis of many projects of Haidong Xue's PhD work at Georgia State University.

It is different from [DEVS](https://en.wikipedia.org/wiki/DEVS) majorly in the following aspects:
* it does not require atomic models' having states or state transition functions on all states predefined
* external transition has no advanced time as an input

Transition functions only requires
* internal transition function on time int_transition(time)
* external transition function on messages ext_transition(message)

It then largely simplies the modeling on complex continuous atomic models compared with DEVS.

# Usage and installation

## With Maven
1. Clone it to local
2. Run [Maven install](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) to install it to local Maven repo. If you are using IntelliJ, just run the buildin Maven install tool.
3. In dependent projects' POM, add a dependency
```xml
<dependency>
    <groupId>edu.gsu.hxue</groupId>
    <artifactId>des</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
## With jar
1. Package this project to a .jar file.
2. Add the jar file as a dependency libirary of your project.

## Other methods
Any other methods to add third party lib will work, e.g. methods mentioned [here](http://blog.javafortesters.com/2017/10/maven-local-dependencies.html)

