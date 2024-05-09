package com.krish.lalwani.nframework.server;
import java.net.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.io.*;
import com.krish.lalwani.nframework.common.*;
class RequestProcessor extends Thread
{
private NFrameworkServer server;
private Socket socket;
RequestProcessor(NFrameworkServer server,Socket socket)
{
this.socket=socket;
this.server=server;
start();
}
public void run()
{
try
{
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
byte header[]=new byte[1024];
byte tmp[]=new byte[1024];
int chunkSize=1024;
int i,j,k;
j=0;
i=0;
int bytesReadCount;
int bytesToReceive=1024;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
int requestLength=0;
j=1;
i=1023;
while(i>=0)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i--;
}
System.out.println("Header received");
byte ack[]=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("Acknowlegment sent");
byte request[]=new byte[requestLength];
bytesToReceive=requestLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
request[i]=tmp[i];
i++;
}
j=j+bytesReadCount;
}
System.out.println("Request arrived of length : "+requestLength);
String requestJSONString=new String(request,StandardCharsets.UTF_8);
Request requestObject=JSONUtil.fromJSON(requestJSONString,Request.class);
//the request Object contains servicePath and arguments
//We want the reference of the TCPService that contain Class ref & method ref
String servicePath=requestObject.getServicePath();
TCPService tcpService=this.server.getTCPService(servicePath);
Response responseObject=new Response();
if(tcpService==null)
{
responseObject.setSuccess(false);
responseObject.setResult(null);
responseObject.setException(new RuntimeException("Invalid Path : "+servicePath));
}
else
{
Class c=tcpService.c;
Method method=tcpService.method;
try
{
Object serviceObject=c.newInstance();
Object result=method.invoke(serviceObject,requestObject.getArguments());
responseObject.setSuccess(true);
responseObject.setResult(result);
responseObject.setException(null);
}catch(InstantiationException instantiationException)
{
responseObject.setSuccess(false);
responseObject.setResult(null);
responseObject.setException(new RuntimeException("Unable to create Object to service class associated with the path : "+servicePath));
System.out.println(instantiationException);
}
catch(IllegalAccessException illegalAccessException)
{
responseObject.setSuccess(false);
responseObject.setResult(null);
responseObject.setException(new RuntimeException("Unable to create Object to service class associated with the path : "+servicePath));
System.out.println(illegalAccessException);
}
catch(InvocationTargetException invocationException)
{
responseObject.setSuccess(false);
responseObject.setResult(null);
responseObject.setException(invocationException.getCause().getMessage());
System.out.println(invocationException.getCause());
}
}
String responseJSONString=JSONUtil.toJSON(responseObject);
byte bytes[]=responseJSONString.getBytes(StandardCharsets.UTF_8);
int responseLength=bytes.length;
int x=responseLength;
i=1023;
header=new byte[1024];
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
os.write(header,0,1024);
os.flush();
System.out.println("Header Sent of length : "+responseLength);
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("Acknowledgement received");
int bytesToSend=responseLength;
j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize) chunkSize=bytesToSend-j;
os.write(bytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
System.out.println("Response sent");
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("Acknowledgment received");
socket.close();
}catch(IOException e)
{
System.out.println(e);
}
}
}