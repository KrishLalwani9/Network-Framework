package com.krish.lalwani.nframework.client;
import com.krish.lalwani.nframework.common.*;
import com.krish.lalwani.nframework.common.exceptions.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
public class NFrameworkClient
{
public Object execute(String servicePath,Object ...arguments) throws Throwable
{
try
{
Request request=new Request();
request.setServicePath(servicePath);
request.setArguments(arguments);
String requestJSONString=JSONUtil.toJSON(request);
Socket socket=new Socket("localhost",5500);
byte bytes[]=requestJSONString.getBytes(StandardCharsets.UTF_8);
int requestLength=bytes.length; //look at this
byte header[]=new byte[1024];
int chunkSize=1024;
int i=1023;
int x=requestLength;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
OutputStream os=socket.getOutputStream();
os.write(header,0,1024);
os.flush();
System.out.println("Header sent");
int bytesReadCount;
byte ack[]=new byte[1];
InputStream is=socket.getInputStream();
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("Acknowledgement received");
int bytesToSend=requestLength;
int j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize) chunkSize=bytesToSend-j;
os.write(bytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
System.out.println("request Sent of length : "+requestLength);
byte tmp[]=new byte[1024];
bytesReadCount=1024;
j=0;
int k;
int bytesToReceive=1024;
i=0;
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
int responseLength=0;
j=1;
i=1023;
while(i>=0)
{
responseLength=responseLength+(header[i]*j);
j=j*10;
i--;
}
System.out.println("header received of length : "+responseLength);
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("acknowledgement sent");
byte response[]=new byte[responseLength];
j=0;
i=0;
bytesToReceive=responseLength;
tmp=new byte[1024];
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
response[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
System.out.println("response recevied");
ack[0]=1;
os.write(ack,0,1);
os.flush();
socket.close();
String responseJSONString=new String(response,StandardCharsets.UTF_8);
System.out.println(responseJSONString);
Response responseObject=JSONUtil.fromJSON(responseJSONString,Response.class);
if(responseObject.getSuccess())
{
return responseObject.getResult();
}
else
{
throw new Throwable(responseObject.getException().toString());
}
}catch(Exception e)
{
System.out.println(e);
}
return null;
}
}