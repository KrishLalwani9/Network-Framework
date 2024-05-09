# Documentation to use this framework
# To use this framework you have to designe an class and apply path annotation on the class and its methods and register your class at server side by using nframeworkServer object
# Example over hear
# Create you required class using Path annotation and Register your class
import com.krish.lalwani.nframework.server.*;
import com.krish.lalwani.nframework.server.annotations.*;
@Path("/banking")
public class Bank 
{
@Path("/branchName")
public String getBranchName(String city) throws Exception
{
if(city.equals("Ujjain"))
{
return "Freeganj";
}
if(city.equals("INDORE"))
{
return "Chapan";
}
if(city.equals("Mumbai"))
{
return "Colaba";
}
throw new Exception("No branch in that city");
}
public static void main(String krish[])
{
NFrameworkServer server=new NFrameworkServer();
server.registerClass(Bank.class);
server.start();
}
}


# For Executing this framework first you have to start the server

java -classpath ..\Network-Framework\server\build\libs\nframework-server.jar;..\Network-Framework\common\build\libs\nframework-common.jar;c:\gson\*;. Bank
 
# For using this class at client side give the value of setup path annotation value applied on class and methods during designing of class

import com.krish.lalwani.nframework.client.*;
class BankUI
{
public static void main(String ...krish)
{
try
{
NFrameworkClient client=new NFrameworkClient();
String branchName=(String)client.execute("/banking/branchName",krish[0]);
System.out.println(branchName);
}catch(Throwable t)
{
System.out.println(t.getMessage());
}
}
}

# For Execution at client side

java -classpath ..\Network-Framework\client\build\libs\nframework-client.jar;..\Network-Framework\common\build\libs\nframework-common.jar;c:\gson\*;. BankUI Mumbai
