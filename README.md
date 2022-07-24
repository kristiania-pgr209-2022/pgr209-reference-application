# Library Server

Example library management system where administrative users can register books with authors and add loans.

## Technology to be used

* Jetty 11
* Action Controller
* fluent-jdbc
* Flyway
* SLF4J with implementation Logevents

## Problem domain

* Books with authors
  * Multiple books
* Users
* Rentals where users rent books

### Simple model: Each book has one or more authors

![](./doc/domain-model-Library-v1_a.png)

### Simple model: Each book is in one or more libraries

![](./doc/domain-model-Library_v1_b.png)

### Books, authors and libraries

![](./doc/domain-model-Library_v2_a.png)

### Books, libraries and loans

![](./doc/domain-model-Library_v2_b.png)

### Books, authors, libraries and loans

![](./doc/domain-model-Library_v3.png)
