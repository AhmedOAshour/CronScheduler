package main.cron;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

public class ClassReader {

    public Callable<Object> readFunction(String filePath, String fileName, String methodName) {
        compile(filePath + fileName);
        Class<?> clazz = loadClass(filePath, fileName);
        if (clazz != null) {
            return getMethod(clazz, methodName);
        }
        return null;
    }

    private void compile(String file) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler.run(null, null, null, file) != 0) {
            System.err.println("Compilation Failed: " + file);
        }
    }

    private Class<?> loadClass(String filePath, String fileName) {
        try {
            File fileDirectory = new File(filePath);
            URL url = fileDirectory.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            if (fileName.length()<6) {
                System.err.println("Invalid file name: " + fileName);
                return null;
            }
            Class<?> clazz = classLoader.loadClass(fileName.substring(0, fileName.length() - 5));

            classLoader.close();
            return clazz;
        } catch (ClassNotFoundException | MalformedURLException e) {
            System.err.println("Error loading class: " + filePath + fileName);
        } catch (IOException e) {
            System.err.println("Cannot close classLoader manually");
        }
        return null;
    }

    private Callable<Object> getMethod(Class<?> clazz, String methodName) {
        try {
            Method methods = clazz.getMethod(methodName);
            return new Callable<>() {
                public Object call() throws Exception {
                    return methods.invoke(null);
                }
            };
        } catch (NoSuchMethodException e) {
            System.err.println("No such method found: " + methodName);
            return null;
        }
    }
}
