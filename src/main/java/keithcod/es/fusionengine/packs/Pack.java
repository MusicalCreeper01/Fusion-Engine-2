package keithcod.es.fusionengine.packs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import keithcod.es.fusionengine.register.Domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pack {

    public String name = "New Pack!";
    public String domain = "misc";
    public String desc = "This is a pack without a description.";

    public class Author {
        public String name = "Anonymous";
        public String email = "";
        public String site = "";
    }
    public Author[] authors;

    public class Credit {
        public String name = "Anonymous ";
        public String site = "";
        public String desc = "";
    }

    public Credit[] credits;

    public String path;

    public List<PackBlock> blocks = new ArrayList<>();

    public void build(){
        Domain d = Domain.createDomain(domain);

        for(PackBlock block : blocks)
            block.build(d);


    }

    public static Pack load(String path) throws FileNotFoundException, JsonSyntaxException{

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        Pack data = gson.fromJson(reader, Pack.class);
        data.path = Paths.get(path).getParent().toString();

        String blocksPath = Paths.get(data.path, "blocks/").toString();
        System.out.println("    Attempting to load blocks at \"" + blocksPath + "\"");
        File folder = new File(blocksPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String s =listOfFiles[i].getPath();
                if(s.endsWith("json")){
                    System.out.println("        Attempting to load block \"" + s + "\"");
                    try {
                        PackBlock block= PackBlock.load(s);
                        block.generateSlug();
                        data.blocks.add(block);
                        System.out.println("        Loaded block \"" + block.name + ":" + block.slug + "\"");
                    }catch(FileNotFoundException ex){

                    }catch(JsonSyntaxException ex){
                        System.out.println("Failed to load block \"" + s + "\"! " + ex.getMessage());
                    }
                }
            }
        }

        return data;
    }
}
