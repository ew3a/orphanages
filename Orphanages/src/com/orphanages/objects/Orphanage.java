package com.orphanages.objects;

import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Orphanage
{

	private String name;
	private List<String> problems;
	private List<String> solutions;
	private LatLng location;
	private HashMap<String, String> chat;

	public Orphanage(String name , List<String> problems ,
			List<String> solutions , LatLng location ,
			HashMap<String, String> chat)
	{
		super();
		this.name = name;
		this.problems = problems;
		this.solutions = solutions;
		this.location = location;
		this.chat = chat;
	}

	public Orphanage()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<String> getProblems()
	{
		return problems;
	}

	public void setProblems(List<String> problems)
	{
		this.problems = problems;
	}

	public List<String> getSolutions()
	{
		return solutions;
	}

	public void setSolutions(List<String> solutions)
	{
		this.solutions = solutions;
	}

	public LatLng getLocation()
	{
		return location;
	}

	public void setLocation(LatLng location)
	{
		this.location = location;
	}

	public HashMap<String, String> getChat()
	{
		return chat;
	}

	public void setChat(HashMap<String, String> chat)
	{
		this.chat = chat;
	}

	

}
