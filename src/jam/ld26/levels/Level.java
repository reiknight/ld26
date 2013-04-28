package jam.ld26.levels;

import infinitedog.frisky.textures.TextureManager;
import jam.ld26.game.C;
import jam.ld26.tiles.TileSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class Level {
    private String fileName;
    private ArrayList<ArrayList<Integer>> map;
    private int tileSize;
    private String tileSetName, tileSetFileName;
    private TileSet tileSet;
    
    public Level() {
        tileSize = 32;
        tileSet = new TileSet(C.Textures.DEFAULT_TILE_SET.name, tileSize);
        map = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < (C.SCREEN_HEIGHT / tileSize); i += 1) {            
            map.add(new ArrayList<Integer>());
            for(int j = 0; j < (C.SCREEN_WIDTH / tileSize); j += 1) {
                map.get(i).add(0);
            }
        }    
    }
    
    public Level(String fileName) {
        this.fileName = fileName;
    }
    
    public void load() throws FileNotFoundException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(new Scanner(new File(fileName)).useDelimiter("\\Z").next());
        
        // Parse tileSize
        tileSize = Integer.parseInt(obj.get("tileSize").toString());
        
        // Parse tileSet
        JSONObject tileSetObj = (JSONObject) obj.get("tileSet");
        tileSetName = tileSetObj.get("name").toString();
        tileSetFileName = tileSetObj.get("fileName").toString();
        TextureManager tm = TextureManager.getInstance();
        if(tm.getTexture(tileSetName) == null) {
            TextureManager.getInstance().addTexture(tileSetName, tileSetFileName);
        }
        tileSet = new TileSet(tileSetName, tileSize);
        
        // Parse map element of level
        map = new ArrayList<ArrayList<Integer>>();
        JSONArray jsonArray = (JSONArray) obj.get("map"); 
        if (jsonArray != null) { 
            for (int i = 0; i < jsonArray.size(); i++){ 
                map.add(new ArrayList<Integer>());
                for (int j = 0; j < ((JSONArray) jsonArray.get(i)).size(); j++){ 
                    map.get(i).add(Integer.parseInt(((JSONArray) jsonArray.get(i)).get(j).toString()));
                } 
            }
        }
    }
    
    public void save() throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject tileSetObj = new JSONObject();
        JSONArray mapObj = new JSONArray();
        
        obj.put("tileSize", tileSize);
        tileSetObj.put("name", tileSetName);
        tileSetObj.put("fileName", tileSetFileName);
        obj.put("tileSet", tileSetObj);
        
        for (int i = 0; i < getRows(); i += 1) {
            JSONArray rowObj = new JSONArray();
            for (int j = 0; j < getCols(); j += 1) {    
                rowObj.add(map.get(i).get(j));
            }
            mapObj.add(rowObj);
        }
        obj.put("map", mapObj);
        
        backupFile();
        
        FileWriter fileWriter = null;
        File newTextFile = new File(fileName);
        fileWriter = new FileWriter(newTextFile);
        fileWriter.write(obj.toString());
        fileWriter.close();
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
     public TileSet getTileSet() {
        return tileSet;
    }
     
    public ArrayList<ArrayList<Integer>> getMap() {
        return map;
    }

    public int getRows() {
        return map.size();
    }

    public int getCols() {
        // All rows have the same columns
        return map.get(0).size();
    }

    public void render(GameContainer gc, Graphics g) {
        for (int i = 0; i < getRows(); i += 1) {
            for (int j = 0; j < getCols(); j += 1) {
                tileSet.render(map.get(i).get(j), j, i);
            }
        }
    }

    public void update(GameContainer gc, int delta) {
        
    }    
    
    public int[] getTilePosition(Vector2f v) {
        int[] tilePosition = {0, 0};
        tilePosition[0] = (int) (v.x / tileSize);
        tilePosition[1] = (int) (v.y / tileSize);
        return tilePosition;
    }
    
    public void addTileIdAtPosition(int[] position) {
        int id = map.get(position[1]).get(position[0]);
        map.get(position[1]).set(position[0], (id + 1) % tileSet.size());
    }
    
    public void subTileIdAtPosition(int[] position) {
        int id = map.get(position[1]).get(position[0]);
        map.get(position[1]).set(position[0], (id - 1) % tileSet.size());
    }
    
    private void backupFile() {
        InputStream inStream = null;
        OutputStream outStream = null;
        
        try{
 
            File file1 = new File(fileName);
            File file2 = new File(fileName + ".bak");
 
            inStream = new FileInputStream(file1);
            outStream = new FileOutputStream(file2); // for override file content
            //outStream = new FileOutputStream(file2,<strong>true</strong>); // for append file content
 
            byte[] buffer = new byte[1024];
 
            int length;
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);
            }
 
            if (inStream != null)inStream.close();
            if (outStream != null)outStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}