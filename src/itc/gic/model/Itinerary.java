package itc.gic.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.entity.SerializableEntity;

public class Itinerary implements Serializable{
	
	private int id;
	ArrayList<Integer> spots;
	public String name, description;
	
	public Itinerary(int id, String name, String description) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	
}
