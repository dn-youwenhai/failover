package com.dongnao.jack.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Service
public class MongoUserDao implements IMongoUserDao {

	@Autowired
	@Qualifier("mongoTemplate1")
	MongoTemplate mt;
	
	@Autowired
	@Qualifier("mongoTemplate2")
	MongoTemplate mt2;

	public String insert(String param) {
		for (int i = 0; i < 10; i++) {

			JSONObject jo = new JSONObject();
			jo.put("name", "jack" + i);
			jo.put("age", 19);
			mt.insert(jo, "mycon");
		}

		for (int i = 0; i < 10; i++) {

			JSONObject jo = new JSONObject();
			jo.put("name", "sam" + i);
			jo.put("age", 12);
			mt.insert(jo, "mycon");
		}

		for (int i = 0; i < 10; i++) {

			JSONObject jo = new JSONObject();
			jo.put("name", "senvon" + i);
			jo.put("age", 43);
			mt.insert(jo, "mycon");
		}

		return "OK";
	}

	public String sum(String param) {
		DBCollection col = mt.getCollection("mycon");

		String mapStr = "function(){if(this.age>10) emit(this.age,this.name)}";

		String reduceStr = "function(key,values){var count=0;values.forEach(function(){count+=1;});var result={names:values,sum:count};return result;}";

		MapReduceOutput output = col.mapReduce(mapStr, reduceStr, "result",
				null);

		DBCollection outcol = output.getOutputCollection();

		DBCursor dbc = outcol.find();

		StringBuffer sb = new StringBuffer();

		while (dbc.hasNext()) {
			DBObject dbo = dbc.next();
			sb.append(dbo.toString());
			System.out.println(dbo.toString());
		}

		return sb.toString();
	}

	public String upload(String param) {
		
		GridFS gridfs = new GridFS(mt2.getDb());
		
		try {
			GridFSInputFile gfsif = gridfs.createFile(new File("F:\\hallv2\\WebRoot\\html\\images\\ios_ewm.png"));
			gfsif.put("filename", "ios_ewm");
			gfsif.save();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}

	public String download(String param) {
		
		GridFS gridfs = new GridFS(mt2.getDb());
		
		GridFSDBFile sfsf = gridfs.findOne("ios_ewm");
		
		InputStream is = sfsf.getInputStream();
		
		try {
			FileOutputStream fos = new FileOutputStream(new File("F:\\hallv2\\WebRoot\\html\\images\\download.png"));
			
			byte[] bytearr = new byte[1024];
			
			while(is.read(bytearr) != -1) {
				fos.write(bytearr);
			}
			
			fos.close();
			is.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "OK";
	}
}
