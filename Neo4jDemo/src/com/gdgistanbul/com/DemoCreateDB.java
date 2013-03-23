package com.gdgistanbul.com;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.gdgistanbul.com.Constant.Relations;

public class DemoCreateDB {

	public static void main(String[] args) {
		/*
		 * graph db
		 */
		GraphDatabaseService graphDB = new EmbeddedGraphDatabase(
				Constant.DB_PATH);
		/*
		 * trans begin
		 */
		Transaction tx = graphDB.beginTx();
		try {
			/*
			 * Indexes
			 */
			Index<Node> userIndex = graphDB.index().forNodes("USER_INDEX");
			Index<Node> movieIndex = graphDB.index().forNodes("MOVIE_INDEX");

			/*
			 * create root_nodes
			 */
			Node userRoot = createUserRoot(graphDB);
			Node movieRoot = createMovieRoot(graphDB);

			/*
			 * creates users
			 */
			Node hasan = createUser(1, "hasan", graphDB, userRoot, userIndex);
			Node salim = createUser(2, "salim", graphDB, userRoot, userIndex);
			Node arman = createUser(3, "arman", graphDB, userRoot, userIndex);
			Node ali = createUser(4, "ali", graphDB, userRoot, userIndex);

			/*
			 * creates movies
			 */
			Node matrix = createMovie(1, "Matrix", graphDB, movieRoot,
					movieIndex);
			Node bttf = createMovie(2, "Back to the future", graphDB,
					movieRoot, movieIndex);
			Node lotr = createMovie(3, "Lord of the rings", graphDB, movieRoot,
					movieIndex);
			Node fandf = createMovie(4, "Fast and furious", graphDB, movieRoot,
					movieIndex);

			/*
			 * follow relations
			 */
			hasan.createRelationshipTo(salim, Relations.FOLLOW);
			arman.createRelationshipTo(salim, Relations.FOLLOW);
			arman.createRelationshipTo(ali, Relations.FOLLOW);
			ali.createRelationshipTo(salim, Relations.FOLLOW);

			/*
			 * like relations
			 */
			hasan.createRelationshipTo(matrix, Relations.LIKES);
			hasan.createRelationshipTo(fandf, Relations.LIKES);
			salim.createRelationshipTo(lotr, Relations.LIKES);
			salim.createRelationshipTo(bttf, Relations.LIKES);
			arman.createRelationshipTo(bttf, Relations.LIKES);
			ali.createRelationshipTo(matrix, Relations.LIKES);
			ali.createRelationshipTo(bttf, Relations.LIKES);

			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
			tx.failure();
		} finally {
			tx.finish();
		}
	}

	public static Node createUserRoot(GraphDatabaseService graphDB) {
		Node refNode = graphDB.getReferenceNode();
		Node userRoot = graphDB.createNode();
		userRoot.setProperty("root_id", "USER_ROOT");
		refNode.createRelationshipTo(userRoot, Relations.ROOT_REL_USER);
		return userRoot;
	}

	public static Node createMovieRoot(GraphDatabaseService graphDB) {
		Node refNode = graphDB.getReferenceNode();
		Node movieRoot = graphDB.createNode();
		movieRoot.setProperty("root_id", "MOVIE_ROOT");
		refNode.createRelationshipTo(movieRoot, Relations.ROOT_REL_MOVIE);
		return movieRoot;
	}

	public static Node createUser(int id, String userName,
			GraphDatabaseService graphDB, Node userRoot, Index<Node> userIndex) {
		Node user = graphDB.createNode();
		user.setProperty("user_id", id);
		user.setProperty("user_name", userName);
		userRoot.createRelationshipTo(user, Relations.USER);
		userIndex.add(user, "user_id", id);
		return user;
	}

	public static Node createMovie(int id, String movieName,
			GraphDatabaseService graphDB, Node movieRoot, Index<Node> movieIndex) {
		Node movie = graphDB.createNode();
		movie.setProperty("movie_id", id);
		movie.setProperty("movie_name", movieName);
		movieRoot.createRelationshipTo(movie, Relations.MOVIE);
		movieIndex.add(movie, "movie_id", id);
		return movie;
	}
}
