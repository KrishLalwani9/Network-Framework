package com.krish.lalwani.nframework.common;
public class Request implements java.io.Serializable
{
private String servicePath; // represent a path to a method(/Designation/add)
private Object [] arguments;
public void setServicePath(String servicePath)
{
this.servicePath=servicePath;
}
public String getServicePath()
{
return this.servicePath;
}
public void setArguments(Object ...arguments)
{
this.arguments=arguments;
}
public Object [] getArguments()
{
return this.arguments;
}
}