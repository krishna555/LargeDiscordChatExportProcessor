import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import ResponseStruct.*;
public class DiscordChatAggregator {

    public static List<String> readKeywords() {
        BufferedReader reader;
        List<String> keywords =  new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/keywords.txt"));
            String line = reader.readLine();

            while (line != null) {
                keywords.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Found keywords : " + keywords.toString());
        return keywords;
    }
    // private static String[] keywords = {"ukraine", "russia", "putin", "soviet", "kremlin", "minsk", "ukrainian", "NATO", "luhansk", "donetsk", "kyiv", "kiev", "moscow", "zelensky", "fsb", "KGB", "Україна", "Киев", "ФСБ", "Россия", "КГБ", "Київ", "україни", "Росія", "кгб", "фсб", "SlavaUkraini", "ukrainian", "\\U0001F1FA\\U0001F1E6", "Украина", "украины", "Donbas", "donbas", "Донбасс", "Донбасс", "своихнебросаем"};

    public static boolean isMessageNonEmpty(MessageStruct messageObj) {
        if (messageObj.getContent() == "" || messageObj.getContent() == null) {
            return false;
        }
        return true;
    }

    public static boolean isKeywordInMessage(MessageStruct messageObj, String keyword) {
        // System.out.println(messageObj.getContent().toLowerCase() + ", " +  keyword + ", " + messageObj.getContent().toLowerCase().contains(keyword));
        if (messageObj.getContent() != "" &&
                messageObj.getContent() != null &&
                (messageObj.getContent().toLowerCase().contains(keyword) == true)) {
                return true;
        }
        return false;
    }
    public static void main(String[] args) throws Exception{
        if (args.length != 3) {
            System.out.println("Usage: java DiscordChatAggregator <DISCORD_CHAT_PATH> <OUTPUT_PATH_1> <OUTPUT_PATH_2>");
            return;
        }
//        String path = "/Users/krishnaravishankar/usc/discord/outputs/ChatExport/604650578320293928.json";
//        String outputPath = "src/main/output/jsonData2.txt";
//        String outputPath2 = "src/main/output/lookupData2.json";

        String path = args[0];
        String outputPath = args[1];
        String outputPath2 = args[2];
        List<String> keywords = readKeywords();
        int count = 0;
        try {
            InputStream inputStream = Files.newInputStream(Path.of(path));
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

            PrintWriter out1 = new PrintWriter(new FileWriter(outputPath));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("messages")) {
                    break;
                }
                else {
                    reader.skipValue();
                }
            }
            reader.beginArray();
            // ExportStruct exportobj = new Gson().fromJson(reader, ExportStruct.class);
            Map<String, List<MessageStruct>> result = new HashMap<String, List<MessageStruct>>();
            Set<String> messageIds = new HashSet<String>();
            while (reader.hasNext()) {
                MessageStruct messageObj = new Gson().fromJson(reader, MessageStruct.class);

                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(messageObj);
                // System.out.println(jsonString);

                count += 1;

                if (isMessageNonEmpty(messageObj)) {
                    for (String keyword: keywords) {
                        if (isKeywordInMessage(messageObj, keyword)) {
                            if (!messageIds.contains(messageObj.getId())) {
                                // New Message Write to File.
                                // System.out.println(jsonString + "\n");
                                out1.write(jsonString + "\n");
                                messageIds.add(messageObj.getId());
                            }

                            if (!result.containsKey(keyword)) {
                                result.put(keyword, new ArrayList<MessageStruct>());
                            }
                            result.get(keyword).add(messageObj);
                        }
                    }
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            String lookupMapJson = mapper.writeValueAsString(result);
            // System.out.println(lookupMapJson);
            try {
                PrintWriter lookupMapWriter = new PrintWriter(new FileWriter(outputPath2));
                lookupMapWriter.write(lookupMapJson);
                lookupMapWriter.close();
            } catch(Exception ex) {
                throw ex;
            }
            reader.endArray();
            String name = reader.nextName();
            long totalMessages = reader.nextLong();
            reader.endObject();
                System.out.println(totalMessages);
                System.out.println(count);
        } catch(Exception ex) {
            throw new Exception(ex);
        }
    }
}
