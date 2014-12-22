package itc.gic.model;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

public class Spot implements Serializable{
	
	private String img, name, address;
	private int id, numberReport;
	private double lat, lng, distance, degree;
	
	public Spot(int id, String img, String name, String address, double degree, double distance, double lat, double lng, int numberReport) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.img = img;
		this.name = name;
		this.address = address;
		this.degree = degree;
		this.distance = distance;
		this.lat = lat;
		this.lng = lng;
		this.numberReport = numberReport;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public double getDegree() {
		return degree;
	}
	
	public void setDegree(double degree) {
		this.degree = degree;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public int getNumberReport() {
		return numberReport;
	}
	
	public void setNumberReport(int numberReport) {
		this.numberReport = numberReport;
	}
}
