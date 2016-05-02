# CXF2-resource-comparator
Resource interface comparator for Apache CXF 2

Using version 2 of Apache CXF, there is a problem when multiple resource (interface) has the same value for the global @Path annotation. With the default behaviour of CXF2, for an incoming request, a resource will be “randomly” selected. The discriminant path is the same (here '/') so CXF can't decide anything.
See the declared bug: https://issues.apache.org/jira/browse/CXF-5360

To solve this problem, we can add a custom 'ResourceComparator'. See: http://cxf.apache.org/docs/jax-rs-basics.html#JAX-RSBasics-Customselectionbetweenmultipleresourcesoroperations

The custom comparator must be declared in the JAX-RS configuration file:
```xml
<!-- Custom ResourceComparator -->
<jaxrs:resourceComparator>
   <bean class="org.custom.CustomResourceComparator"/>
</jaxrs:resourceComparator>
</jaxrs:server>
```

This custom comparator help CXF to decide which resource class should be used to match the incoming request.
