package com.krish.lalwani.nframework.server;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import com.krish.lalwani.nframework.server.annotations.*;
public class NFrameworkServer
{
private ServerSocket serverSocket;
private Set<Class> tcpNetworkServiceClasses; // we put here information of registered classes
private Map<String,TCPService> services;
public NFrameworkServer()
{
tcpNetworkServiceClasses=new HashSet<>();
services=new HashMap<>();
}

public void registerClass(Class c)
{
Path pathOnType;
Path pathOnMethod;
Method methods[];
String fullPath;
TCPService tcpService=null;
pathOnType=(Path)c.getAnnotation(Path.class);
if(pathOnType==null) return; //if no Path annotation applied on class so there is no sense to register it
methods=c.getMethods();
int methodWithPathAnnotationCount=0;
for(Method method:methods)
{
pathOnMethod=(Path)method.getAnnotation(Path.class);
if(pathOnMethod==null) continue;
methodWithPathAnnotationCount++;
fullPath=pathOnType.value()+pathOnMethod.value(); //there i have a doubt that is String which is given to us as argument has ex /calculator/add and our full path /calculator/add
tcpService=new TCPService();
tcpService.c=c;
tcpService.method=method;
tcpService.path=fullPath;
this.services.put(fullPath,tcpService);
}
if(methodWithPathAnnotationCount>0)
{
tcpNetworkServiceClasses.add(c);
}
}


public TCPService getTCPService(String path) //to get information of given class from our registered class if not then return null by RequestProcessor 
{
return this.services.get(path);
}

public void start()
{
try
{
serverSocket=new ServerSocket(5500);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Krish's server is ready to accept your request on port no : 5500");
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(this,socket); //here this because details of registered classes is in this class
}
}catch(Exception e)
{
System.out.println("This is bad "+e.getMessage());
}
}

}