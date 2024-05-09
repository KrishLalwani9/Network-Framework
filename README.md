# Documentation to use this framework
# To use this framework you have to design an class and apply path annotation on the class and its methods and register your class at server side by using nframeworkServer object
# Example over hear
# Create you required class using Path annotation and Register your class
import com.krish.lalwani.nframework.server.*;
<br>
import com.krish.lalwani.nframework.server.annotations.*;
<br>
@Path("/banking")
<br>
public class Bank 
<br>
{
<br>
@Path("/branchName")
<br>
public String getBranchName(String city) throws Exception
<br>
{
<br>
if(city.equals("Ujjain"))
<br>
{
<br>
return "Freeganj";
<br>
}
<br>
if(city.equals("INDORE"))
<br>
{
<br>
return "Chapan";
<br>
}
<br>
if(city.equals("Mumbai"))
<br>
{
<br>
return "Colaba";
<br>
}
<br>
throw new Exception("No branch in that city");
<br>
}
<br>
public static void main(String krish[])
<br>
{
<br>
NFrameworkServer server=new NFrameworkServer();
<br>
server.registerClass(Bank.class);
<br>
server.start();
<br>
}
<br>
}
<br>


# For Executing this framework first you have to start the server

java -classpath ..\Network-Framework\server\build\libs\nframework-server.jar;..\Network-Framework\common\build\libs\nframework-common.jar;c:\gson\*;. Bank
 
# For using this class at client side give the value of setup path annotation value applied on class and methods during designing of class

import com.krish.lalwani.nframework.client.*;
<br>
class BankUI
<br>
{
<br>
public static void main(String ...krish)
<br>
{
<br>
try
<br>
{
<br>
NFrameworkClient client=new NFrameworkClient();
<br>
String branchName=(String)client.execute("/banking/branchName",krish[0]);
<br>
System.out.println(branchName);
<br>
}catch(Throwable t)
<br>
{
<br>
System.out.println(t.getMessage());
<br>
}
<br>
}
<br>
}
<br>

# For Execution at client side

java -classpath ..\Network-Framework\client\build\libs\nframework-client.jar;..\Network-Framework\common\build\libs\nframework-common.jar;c:\gson\*;. BankUI Mumbai
