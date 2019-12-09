import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class A2 {

	public static void main(String[] args) throws Exception{
		//String file = "src/tmdb_5000_credits.csv";
		String file = args[0];
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		BufferedReader br = null;
		try{
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String s = br.readLine();	
			//System.out.println(s);
			while((s = br.readLine()) != null){
				int pos1 = s.indexOf("[{");
				if(pos1 == -1)
					continue;
				
				s = s.substring(pos1);
				//System.out.println(s);
				
				List<String> list = new ArrayList<String>();
				
				int pos3 = s.indexOf("}]");
				if(s.indexOf("cast_id") != -1) {
					String cast = s.substring(0, pos3 + 2);
					cast = cast.replace("\"\"", "\"");
					//System.out.println("cast: " + cast);
					
					JSONArray data1 = (JSONArray) new JSONParser().parse(cast);
					for(int i = 0; i < data1.size(); i++) {
						list.add(((JSONObject) data1.get(i)).get("name").toString());
					}
					
					String temp = s.substring(pos3 + 2);
					if(temp.indexOf("credit_id") != -1) {
						String credit = s.substring(pos3 + 5, s.length() - 1);
						credit = credit.replace("\"\"", "\"");
						//System.out.println("credit: " + credit);

						JSONArray data2 = (JSONArray) new JSONParser().parse(credit);
						for(int i = 0; i < data2.size(); i++) {
							list.add(((JSONObject) data2.get(i)).get("name").toString());
						}
					}
				}else {
					if(s.indexOf("credit_id") != -1) {
						String credit = s.substring(0, pos3 + 2);
						credit = credit.replace("\"\"", "\"");

						JSONArray data1 = (JSONArray) new JSONParser().parse(credit);
						for(int i = 0; i < data1.size(); i++) {
							list.add(((JSONObject) data1.get(i)).get("name").toString());
						}
					}
				}
				
				
				
				
				
				
				for(int i = 0; i < list.size(); i++) {
					String str1 = list.get(i);
					if(!map.containsKey(str1)) {
						map.put(str1, new HashMap<String, String>());
					}
					
					Map<String, String> temp = map.get(str1);
					for(int j = 0; j < list.size(); j++) {
						String str2 = list.get(j);
						if(i != j) {
							if(!temp.containsKey(str2)) {
								temp.put(str2, str1);
							}
						}
					}
				}
			}
			
			String actor1;
			String actor2;
			Scanner sc = new Scanner(System.in);
			while(true) {
				while(true) {
					System.out.print("Actor 1 name: ");
					actor1 = sc.nextLine();
					if(map.containsKey(actor1)) {
						break;
					}else {
						System.out.println("No such actor.");
					}
				}
				
				while(true) {
					System.out.print("Actor 2 name: ");
					actor2 = sc.nextLine();
					if(map.containsKey(actor2)) {
						break;
					}else {
						System.out.println("No such actor.");
					}
				}
				
				
				// bfs
				Map<String, String> parent = new HashMap<String, String>();
				
				List<String> queue = new ArrayList<String>();
				queue.add(actor1);
				Map<String, Boolean> seen = new HashMap<String, Boolean>();
				seen.put(actor1, true);
				
				while(queue.size() > 0) {
					String cur = queue.remove(0);
					
					Map<String, String> hm = map.get(cur);
					Iterator<Entry<String, String>> iter = hm.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, String> entry = (Entry<String, String>) iter.next();
						String key = entry.getKey();
						String val = entry.getValue();
						
						if(!seen.containsKey(key)) {
							parent.put(key, cur);
							
							queue.add(key);
							seen.put(key, true);
						}
					}
				}
				
				List<String> dd = new ArrayList<String>();
				dd.add(actor2);
				String res = parent.get(actor2);
				while(!res.equals(actor1)) {
					dd.add(res);
					res = parent.get(res);
				}
				dd.add(actor1);
				
				
				for(int i = dd.size() - 1; i >= 0; i--) {
					if(i == 0) {
						System.out.println(dd.get(i));
					}else {
						System.out.print(dd.get(i) + " --> ");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				br.close();
			}catch(Exception e1){
				
			}
		}
	}
}
