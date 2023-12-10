package classe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import database_details.DataTypeCsGenerator;
import database_details.DatabaseDetails;;

public class CsClassGenerator {
    private static final String TEMPLATE_FILE = "/home/thekraken9/Desktop/template_generator/src/ClassTemplate.txt";

    public static HashMap<String, ArrayList<Attribut>> data() throws Exception {
        ArrayList<DatabaseDetails> details = DatabaseDetails.getDatabaseDetailsFromDatabase();
        HashMap<String, ArrayList<Attribut>> hs = new HashMap<String, ArrayList<Attribut>>();
        for (int i = 0; i < details.size(); i++) {
            DatabaseDetails databaseDetails = details.get(i);
            if (hs.containsKey(databaseDetails.getTableName())) {
                ArrayList<Attribut> arr = hs.get(databaseDetails.getTableName());
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        new DataTypeCsGenerator(databaseDetails.getColumnType()).getCsTypeOfColumn(),
                        databaseDetails.getColumnNullable(), 1));
                hs.put(databaseDetails.getTableName(), arr);
            } else {
                ArrayList<Attribut> arr = new ArrayList<Attribut>();
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        new DataTypeCsGenerator(databaseDetails.getColumnType()).getCsTypeOfColumn(),
                        databaseDetails.getColumnNullable(), 1));
                hs.put(databaseDetails.getTableName(), arr);
            }
        }
        return hs;
    }

    public static String attribut(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String attributes = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            attributes += "\tprivate " + attribut.get(i).getType();
            if (attribut.get(i).getNullable().equals("1")) {
                attributes += "?";
            }
            attributes += " " + attribut.get(i).getNom() + " { get; set; }\n";
        }
        return attributes;
    }

    public static void generate() {
        try {
            HashMap<String, ArrayList<Attribut>> hs = CsClassGenerator.data();
            Set<Entry<String, ArrayList<Attribut>>> entrees = hs.entrySet();
            for (Entry<String, ArrayList<Attribut>> table : entrees) {
                String tableName = table.getKey();
                String attributs = CsClassGenerator.attribut(hs, tableName);
                String className = capitalizeFirstLetter(tableName);
                String template = loadTemplateFromFile();
                String constructor = CsClassGenerator.constructeur(hs, tableName);
                String emptyconstructor = CsClassGenerator.constructeurvide(hs, tableName);
                template = template.replace("%CLASS_NAME%", className);
                template = template.replace("%ATTRIBUTS%", attributs);
                template = template.replace("%IMPORTS%", "");
                template = template.replace("%SETTERS%", "");
                template = template.replace("%GETTERS%", "");
                template = template.replace("%CONSTRUCTOR%", constructor);
                template = template.replace("%EMPTYCONSTRUCTOR%", emptyconstructor);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(className + ".cs"))) {
                    writer.write(template);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generate(String tableName) {
        try {
            HashMap<String, ArrayList<Attribut>> hs = CsClassGenerator.data();
            String attributs = CsClassGenerator.attribut(hs, tableName);
            String className = capitalizeFirstLetter(tableName);
            String template = loadTemplateFromFile();
            String setter = CsClassGenerator.setter(hs, tableName);
            String getter = CsClassGenerator.getter(hs, tableName);
            String constructor = CsClassGenerator.constructeur(hs, tableName);
            String emptyconstructor = CsClassGenerator.constructeurvide(hs, tableName);
            template = template.replace("%CLASS_NAME%", className);
            template = template.replace("%ATTRIBUTS%", attributs);
            template = template.replace("%IMPORTS%", "");
            template = template.replace("%SETTERS%", setter);
            template = template.replace("%GETTERS%", getter);
            template = template.replace("%CONSTRUCTOR%", constructor);
            template = template.replace("%EMPTYCONSTRUCTOR%", emptyconstructor);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(className + ".cs"))) {
                writer.write(template);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String loadTemplateFromFile() throws IOException {
        StringBuilder template = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    public static String constructeur(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String constructor = "\tpublic " + capitalizeFirstLetter(tableName) + "(";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            constructor += attribut.get(i).getType();
            if (attribut.get(i).getNullable().equals("1")) {
                constructor += "?";
            }
            constructor += " " + attribut.get(i).getNom();
            if (i != attribut.size() - 1) {
                constructor += ", ";
            }
        }
        constructor += " )\n\t{";
        for (int i = 0; i < attribut.size(); i++) {
            constructor += "\n\t\t";
            constructor += "this.set" + capitalizeFirstLetter(attribut.get(i).getNom()) + "(" + attribut.get(i).getNom()
                    + "); ";
        }
        constructor += "\n\t}";
        return constructor;
    }

    public static String constructeurvide(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String constructor = "\tpublic " + capitalizeFirstLetter(tableName) + "()\n\t{\n\n\t}";
        return constructor;
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String setter(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String setter = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            setter += "\tpublic void Set" + capitalizeFirstLetter(attribut.get(i).getNom()) + "( "
                    + attribut.get(i).getType();
            if (attribut.get(i).getNullable().equals("1")) {
                setter += "?";
            }
            setter += " " + attribut.get(i).getNom() + " )\n\t{\n\t\tthis." + attribut.get(i).getNom() + " = "
                    + attribut.get(i).getNom() + "; \n\t}\n\n";
        }
        return setter;
    }

    public static String getter(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String getter = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            getter += "\tpublic " + attribut.get(i).getType();
            if (attribut.get(i).getNullable().equals("1")) {
                getter += "?";
            }
            getter += " Get" + capitalizeFirstLetter(attribut.get(i).getNom()) + "()\n\t{\n\t\treturn this."
                    + attribut.get(i).getNom() + "; \n\t}\n\n";
        }
        return getter;
    }
}
