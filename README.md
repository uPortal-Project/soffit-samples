# Soffit Samples

[![Build Status](https://travis-ci.org/uPortal-Project/soffit-samples.svg?branch=master)](https://travis-ci.org/uPortal-Project/soffit-samples)

This project contains several example Soffits for uPortal.  More information
about Soffit can be found in the [uPortal manual](https://jasig.github.io/uPortal/developer/soffits/).

## Building This Project

This project builds with [Gradle](https://gradle.org/).  You can install Gradle
on your machine or use the included [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

### Building with Gradle

```bash
  $gradle assemble
```

### Building with the Gradle Wrapper

```bash
  $./gradlew assemble
```

## Running This Project

```bash
  $java -jar build/libs/soffit-samples-0.0.1-SNAPSHOT.war
```
