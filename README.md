[![Build Status](https://travis-ci.org/mpobjects/spring-mpo-util.svg?branch=master)](https://travis-ci.org/mpobjects/spring-mpo-util)
[![Maven Central](https://img.shields.io/maven-central/v/com.mpobjects.spring/spring-mpo-util.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.mpobjects.spring%22%20AND%20a:%22spring-mpo-util%22)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.mpobjects.spring/spring-mpo-util.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/mpobjects/spring/spring-mpo-util/)
[![License](https://img.shields.io/github/license/mpobjects/spring-mpo-util.svg)](https://github.com/mpobjects/spring-mpo-util/blob/master/LICENSE)

# spring-mpo-util
Spring Framework Utilities


# XML Namespace: MPO Util

```
xmlns:mpo="http://system.mp-objects.com/schemas/spring/util"
xsi:xsi:schemaLocation="
	http://system.mp-objects.com/schemas/spring/util 
	http://system.mp-objects.com/schemas/spring/util/mpo-util.xsd
"
```

## mpo:resources

```xml
<mpo:resources groups="groups.list" pattern="(.*)\.foo\.xml">
	<value>classpath*:*.foo.xml</value>
</mpo:resources>
```

This creates a `List<Resource>` bean with the resources sorted and filtered according to the group specification. Resources not in the group list are filtered out.

The content of the element is just like the `util:list` element, and is expected to contain `String` values which are Spring resource locations.

### Attributes

#### groups

A Spring resource location to a file containing the sorted list of accepted groups, which each group on a separate line.

If no groups are defined all resources are accepted and the list is unsorted.

#### groups-ref

A reference to a `List<String>` bean containing the groups. Either `groups` or `groups-ref` can be specified. Not both.

#### pattern

A regular expression executed on the resource's name in order to find the group. By default the first matching group is used as the value of the group.

#### group-index

Defined which group in the pattern expression contains the group value.

#### resource-name

Defines what value of the resource to use as name in the pattern matching. The default is `filename` which is just the filename without any path element. Alternatively `uri` can be used which uses the full resource URI.

#### scope

Spring bean scope.

