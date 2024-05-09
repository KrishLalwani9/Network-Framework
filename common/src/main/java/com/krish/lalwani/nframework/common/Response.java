package com.krish.lalwani.nframework.common;
public class Response implements java.io.Serializable
{
private boolean success;
private Object result; //stores what reurns the funcution (action) which is called
private Object exception;
public void setSuccess(boolean success)
{
this.success=success;
}
public boolean getSuccess()
{
return this.success;
}
public void setResult(Object result)
{
this.result=result;
}
public Object getResult()
{
return this.result;
}
public void setException(Object exception)
{
this.exception=exception;
}
public Object getException()
{
return this.exception;
}
public boolean hasException()
{
return (success==false);
}
}