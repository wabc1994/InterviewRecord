package src;
import java.lang.Throwable;
//处理多层异常处理的时候要考虑是遍历的
public class ExceptionHandling {
    public static void main(String[] args)
    {
        try
        {
            //将字符串转换为数字形式 parseInt(静态方法)
            //throw 自动抛出一个异常 new throw 创建一个一异常对象
            int x =Integer.parseInt("9");
            int i = Integer.parseInt("abc");   //This statement throws NumberFormatException
        }
//异常处理从特殊到一般，从最开始时候的处理到最后面的处理
        catch(NumberFormatException ex)
        {
            System.out.println("This block handles NumberFormatException");
        }

        catch(Exception ex)
        {
            System.out.println("This block handles all exception types");
        }

        catch (Throwable ex)
        {
            System.out.println("Throwable is super class of Exception");
        }
    }
}
