package com.gdgistanbul.com;

import org.neo4j.graphdb.RelationshipType;


public class Constant {

	public static final String DB_PATH="D:\\Developer\\neo4jdb\\";
	
	public static enum Relations implements RelationshipType {
		FOLLOW,LIKES,ROOT_REL_USER,ROOT_REL_MOVIE,USER,MOVIE
	}
	
}
