package classe;

import java.io.*;

public class JavaControllerGenerator {
    private static final String TEMPLATE_FILE = "/home/thekraken9/Desktop/template_generator/src/ClassController.txt";

    public static void generate(String tableName) throws Exception {
        String className = capitalize(tableName);
        String endPoint = addS(tableName);
        String template = "";
        template = JavaControllerGenerator.getTemplate();
        template = template.replace("%ENDPOINT%", endPoint);
        template = template.replace("%CLASS_NAME%", className + "Controller");
        template = template.replace("%CLASS_REPOSITORY%", className + "Repository" + " " + tableName + "Repository");
        template = template.replace("%CLASS_CONSTRUCTOR%", constructor(tableName));
        template = template.replace("%GET%", getAll(tableName));
        template = template.replace("%GET_BY_ID%", getById(tableName));
        template = template.replace("%POST%", create(tableName));
        template = template.replace("%PUT%", update(tableName));
        template = template.replace("%DELETE%", delete(tableName));
        BufferedWriter bw = new BufferedWriter(new FileWriter(className + "Controller.java"));
        bw.write(template);
        bw.close();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String addS(String str) {
        return str + "s";
    }

    public static String constructor(String tableName) {
        String constructor = "";
        String repositoryClass = capitalize(tableName) + "Repository";
        String repositoryName = tableName + "Repository";
        constructor += "@Autowired\n";
        constructor += "\tpublic " + capitalize(tableName) + "Controller("+ repositoryClass + " " + repositoryName +") {\n";
        constructor += "\t\tthis." + repositoryName + " = " + repositoryName + ";\n";
        constructor += "\t}\n";
        return constructor;
    }

    public static String getAll(String tableName) {
        String getAll = "";
        String repositoryName = tableName + "Repository";
        getAll += "@GetMapping\n";
        getAll += "\tpublic List<" + capitalize(tableName) + "> getAll" + capitalize(addS(tableName)) + "() {\n";
        getAll += "\t\treturn " + repositoryName + ".findAll();\n";
        getAll += "\t}\n";
        return getAll;
    }

    public static String getById(String tableName) {
        String getById = "";
        String repositoryName = tableName + "Repository";
        getById += "@GetMapping(\"/{id}\")\n";
        getById += "\tpublic ResponseEntity<" + capitalize(tableName) + "> get" + capitalize(tableName) + "ById(@PathVariable(value = \"id\") Long id) throws ResourceNotFoundException {\n";
        getById += "\t\t" + capitalize(tableName) + " " + tableName + " = " + repositoryName + ".findById(id)\n";
        getById += "\t\t\t.orElseThrow(() -> new ResourceNotFoundException(\"" + capitalize(tableName) + " not found for this id :: \" + id));\n";
        getById += "\t\treturn ResponseEntity.ok().body(" + tableName + ");\n";
        getById += "\t}\n";
        return getById;
    }

    public static String create(String tableName) {
        String create = "";
        String repositoryName = tableName + "Repository";
        create += "@PostMapping\n";
        create += "\tpublic " + capitalize(tableName) + " create" + capitalize(tableName) + "(@Valid @RequestBody " + capitalize(tableName) + " " + tableName + ") {\n";
        create += "\t\treturn " + repositoryName + ".save(" + tableName + ");\n";
        create += "\t}\n";
        return create;
    }

    public static String update(String tableName) {
        String update = "";
        String repositoryName = tableName + "Repository";
        update += "@PutMapping(\"/{id}\")\n";
        update += "\tpublic ResponseEntity<" + capitalize(tableName) + "> update" + capitalize(tableName) + "(@PathVariable(value = \"id\") Long id,\n";
        update += "\t\t@Valid @RequestBody " + capitalize(tableName) + " " + tableName + ") throws ResourceNotFoundException {\n";
        update += "\t\t" + tableName + ".setId(id);\n";
        update += "\t\treturn ResponseEntity.ok(" + repositoryName + ".save(" + tableName + "));\n";
        update += "\t}\n";
        return update;
    }

    public static String delete(String tableName) {
        String delete = "";
        String repositoryName = tableName + "Repository";
        delete += "@DeleteMapping(\"/{id}\")\n";
        delete += "\tpublic Map<String, Boolean> delete" + capitalize(tableName) + "(@PathVariable(value = \"id\") Long id)\n";
        delete += "\t\tthrows ResourceNotFoundException {\n";
        delete += "\t\t" + capitalize(tableName) + " " + tableName + " = " + repositoryName + ".findById(id)\n";
        delete += "\t\t\t.orElseThrow(() -> new ResourceNotFoundException(\"" + capitalize(tableName) + " not found for this id :: \" + id));\n";
        delete += "\t\t" + repositoryName + ".deleteById(id);\n";
        delete += "\t\tMap<String, Boolean> response = new HashMap<>();\n";
        delete += "\t\tresponse.put(\"deleted\", Boolean.TRUE);\n";
        delete += "\t\treturn response;\n";
        delete += "\t}\n";
        return delete;
    }

    public static String getTemplate() throws IOException {
        StringBuilder template = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(JavaControllerGenerator.TEMPLATE_FILE));
        String line = br.readLine();
        while (line != null) {
            template.append(line).append("\n");
            line = br.readLine();
        }
        br.close();
        return template.toString();
    }

    public static void main(String[] args) throws Exception {
        JavaControllerGenerator.generate("salle");
    }
}
