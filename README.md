Filesystem code assessment for Proofpoint. Written in Java. Compiles with Java 11.

Use the simple console application to see it work.

~~~
$ cd filesystem
$ mvn spring-boot:run
~~~

Note, when adding drives, don't use a *path*. For example ...

~~~
touch drive c
touch folder public c
touch documents c/public
~~~
