import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] arguments) throws IOException {
        Path fileName = Path.of("/home/maedeh/Desktop/project-group-30/untitled/src/main/java/Thamina");
        String input = Files.readString(fileName);
        input = input.replaceAll("//", "\n//");
        String regex = "((private)|(public)) (?<returnType>.+) (?<methodName>[a-zA-Z0-9]+)\\s*[(](?<args>[^)]*)[)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int index = 0;
        while (matcher.find()) {
            boolean flag = false;
            for (int i = 0; i < index; i++) {
                if (!matcher.find()) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                break;
            index++;
            String methodName = matcher.group("methodName");
            String returnType = matcher.group("returnType");
            String args = matcher.group("args");

            args = args.replaceAll("ArrayList<.*?>", "int");
            args = args.replaceAll("Hashmap<.*?>", "int");

            int startIndex = matcher.start();
            while (true) {
                if (input.charAt(startIndex) == '\n') {
                    startIndex++;
                    break;
                }
                startIndex++;
            }
            int startIndexCopy = startIndex;
            int count = 1;
            int endIndex;
            while (true) {
                if (input.charAt(startIndex) == '{')
                    count++;
                else if (input.charAt(startIndex) == '}')
                    count--;
                if (count == 0) {
                    endIndex = startIndex;
                    break;
                }
                startIndex++;
            }
            String body;

            String[] elements = args.split(",");
            for (int i=0; i<elements.length; i++) {
                elements[i] = elements[i].trim();
                String[] subElement = elements[i].split(" ");
                if (subElement.length < 2) {
                    continue;
                }
                elements[i] = subElement[1];
            }

            String finalArgs = "";
            for (String element : elements) {
                finalArgs += String.format(", MyGson.toJson(%s)", element);
            }
            if (!returnType.equals("void"))
                body = String.format("Request request = new Request(\"%s\"%s);\n" + "return (%s) NetworkController.getNetworkController().transferData(request);\n", methodName,finalArgs, returnType);
            else
                body = String.format("Request request = new Request(\"%s\"%s);\n", methodName, finalArgs);
            input = input.substring(0, startIndexCopy) + body + input.substring(endIndex);
            matcher = pattern.matcher(input);
        }
        Files.writeString(Path.of("GameController.java"), input);
        System.out.println(input);
    }
}
