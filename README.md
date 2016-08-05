# Resource comparator for Apache CXF v2.x

## Apache CXF framework

Apache CXF is an open source services framework. CXF helps you build and develop services using frontend programming APIs, like JAX-WS and JAX-RS. These services can speak a variety of protocols such as SOAP, XML/HTTP, RESTful HTTP, or CORBA and work over a variety of transports such as HTTP, JMS or JBI.

## Context

Using version 2 of Apache CXF, there is an issue when multiple resource has the same value for the global `@Path` annotation. The discriminant path is the same so CXF don't know which resource to select. With the default behaviour of Apache CXF v2.x, for an incoming request, a resource will be “randomly” selected.

### Possible issue using Apache CXF v2.x

```java
@Path("/") 
public interface First {
   @GET 
   @Path("foo") 
   Response foo();
}

@Path("/")
public interface Second {
   @GET
   @Path("bar")
   Response bar();
}
```

The main problem is when requesting `GET /bar`, the `First` resource could be selected. It will cause an error because there is no corresponding method in the `First` resource.

See the declared issue: https://issues.apache.org/jira/browse/CXF-5360

## Solutions

There is 3 different solution to solve this issue:
- If you can, migrate to Apache CXF v3.x. This new major version correct this issue.
- Be careful and do not use two identical value for the global `@Path` annotation.
- Set a custom `ResourceComparator`to your project.

### Set a custom resource comparator

To solve the explained issue in Apache CXF 2.x, a solution is to use a custom `ResourceComparator`.
See the official documentation about this: http://cxf.apache.org/docs/jax-rs-basics.html#JAX-RSBasics-Customselectionbetweenmultipleresourcesoroperations

The custom comparator must be declared in the JAX-RS configuration file:
```xml
<!-- Custom ResourceComparator -->
<jaxrs:resourceComparator>
   <bean class="path-to-the-class.CXFInterfaceComparator"/>
</jaxrs:resourceComparator>
```

This custom comparator help CXF to decide which resource class/interface should be used to match the incoming request. This comparator analyse all the resources and try to find in which of them the incoming request is declared, looking at the methods `@Path` and verb annotations. 

## Extra information

All `OPTIONS` HTTP requests are automatically accepted if the request path of the request is matching an existing method path declaration.
