package javaconfigftw.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String args[]) throws Throwable {
        String path =   packageToFolders(Main.class) + "/context.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(path);
     }

    private static String packageToFolders(Class<?> clazzWhosePackageWeWant) {
        Package aPackage = clazzWhosePackageWeWant.getPackage();
        String packageName = aPackage.getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('/');

        for (char c : packageName.toCharArray())
            stringBuilder.append(c == '.' ? '/' : c);

        return stringBuilder.toString().trim();
    }
}
