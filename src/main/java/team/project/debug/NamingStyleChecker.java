package team.project.debug;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

//@Component /* <- 开启命名风格检测 */
final class NamingStyleChecker {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("team.project")
    private String rootPackage;

    @Value("team.project.module._template")
    private String templatePackageName;

    @Value("Tmpl")
    private String tmplPrefix;

    private final Pattern packageNamePattern  = Pattern.compile("^[a-z.]*$");
    private final Pattern classNamePattern    = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
    private final Pattern variableNamePattern = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
    private final Pattern constantNamePattern = Pattern.compile("^[A-Z][_A-Z0-9]*$");
    private final Pattern methodNamePattern   = Pattern.compile("^[a-z][a-zA-Z0-9]*$");

    private HashSet<String>     invalidPackageNames;
    private ArrayList<String[]> invalidClassName;
    private ArrayList<String[]> invalidTmplPrefix;
    private ArrayList<String[]> invalidFieldName;
    private ArrayList<String[]> invalidMethodName;
    private ArrayList<String[]> invalidParamName;

    @PostConstruct
    private void checkAndReport() {
        invalidPackageNames = new HashSet<>();
        invalidClassName    = new ArrayList<>();
        invalidTmplPrefix   = new ArrayList<>();
        invalidFieldName    = new ArrayList<>();
        invalidMethodName   = new ArrayList<>();
        invalidParamName    = new ArrayList<>();

        for (String className : getAllClassesInThisProject(rootPackage)) {
            try {
                checkClass(Class.forName(className));
            } catch (ClassNotFoundException ignored) {}
        }

        if ( ! invalidPackageNames.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String packageName : invalidPackageNames) {
                sb.append(" - ").append(packageName).append("\n");
            }
            logger.error("""
                包名使用全小写形式，不使用下划线分隔单词
                请修改如下包名：
                {}""", sb);
        }

        if ( ! invalidClassName.isEmpty() || ! invalidTmplPrefix.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] className : invalidClassName) {
                sb.append(" - ").append(className[0]).append(" （").append(className[1]).append("）\n");
            }
            for (String[] className : invalidTmplPrefix) {
                sb.append(" - ").append(className[0]).append(" （").append(className[1]).append("）\n");
            }
            logger.error("""
                类名使用大驼峰形式，每个单词的首字母都大写，不使用下划线分隔单词。不要使用模板包的 Tmpl 前缀
                请修改如下类名：
                {}""", sb);
        }

        if ( ! invalidMethodName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] methodAndClass : invalidMethodName) {
                sb.append(" - ").append(methodAndClass[0]).append(" （位于：").append(methodAndClass[1]).append("）\n");
            }
            logger.error("""
                函数名采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                请修改如下函数名：
                {}""", sb);
        }

        if ( ! invalidParamName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] paramAndMethod : invalidParamName) {
                sb.append(" - ").append(paramAndMethod[0]).append(" （位于：").append(paramAndMethod[1]).append("）\n");
            }
            logger.error("""
                函数入参采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                请修改如下入参名：
                {}""", sb);
        }

        if ( ! invalidFieldName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] fieldAndClass : invalidFieldName) {
                sb.append(" - ").append(fieldAndClass[0]).append(" （位于：").append(fieldAndClass[1]).append("）\n");
            }
            logger.error("""
                字段名采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                常量可以采用所有单词的字母全大写，使用下划线分隔单词
                请修改如下字段名：
                {}""", sb);
        }
    }

    private void checkClass(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        if ( ! packageName.startsWith(rootPackage) || packageName.startsWith(templatePackageName)) {
            return;
        }

        if ( ! packageNamePattern.matcher(packageName).matches()) {
            invalidPackageNames.add(packageName);
        }

        String className = clazz.getName();
        String classSimpleName = clazz.getSimpleName();
        if ( ! classSimpleName.contains("$$") && ! classNamePattern.matcher(classSimpleName).matches()) {
            invalidClassName.add(new String[]{classSimpleName, className});
        }
        else if (classSimpleName.startsWith(tmplPrefix)) {
            invalidTmplPrefix.add(new String[]{classSimpleName, className});
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (   ! fieldName.contains("$")
                && ! variableNamePattern.matcher(fieldName).matches()
                && ! constantNamePattern.matcher(fieldName).matches())
            {
                invalidFieldName.add(new String[]{fieldName, className});
            }
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (   ! methodName.contains("$")
                && ! methodNamePattern.matcher(methodName).matches())
            {
                invalidMethodName.add(new String[]{methodName + "()", className});
            }

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                if (! variableNamePattern.matcher(paramName).matches()) {
                    invalidParamName.add(new String[]{paramName, className + "." + methodName + "()"});
                }
            }
        }
    }

    private List<String> getAllClassesInThisProject(String packageName) {
        List<String> classNames = new ArrayList<>();
        URL basePathURL = Thread.currentThread().getContextClassLoader().getResource(packageName.replace('.', '/'));
        if (basePathURL != null) {
            File baseDirectory = new File(basePathURL.getPath());
            if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                File[] files = baseDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            classNames.addAll(
                                getAllClassesInThisProject(packageName + "." + file.getName())
                            );
                        } else if (file.getName().endsWith(".class")) {
                            classNames.add(
                                packageName + '.' + file.getName().substring(0, file.getName().length() - 6)
                            );
                        }
                    }
                }
            }
        }
        return classNames;
    }
}
