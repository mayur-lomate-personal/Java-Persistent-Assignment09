package movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MainExecution {
	
	Connection con;
	
	public MainExecution() {
		// TODO Auto-generated constructor stub
		try {
			this.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "pers_practice", "0101");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			System.out.println("Connection Failed");
			System.exit(0);
		}
	}
	
	List<Movie> populatedMovies(File file) throws FileNotFoundException {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		Scanner sc = new Scanner(file);
		while(sc.hasNext()) {
			String movieInfo = sc.nextLine();
			String movieAttributes[] = movieInfo.split(",");
			ArrayList<String> casting = new ArrayList();
			Collections.addAll(casting, movieAttributes[5].substring(1, movieAttributes[5].length()-1).split("-"));
			movies.add(new Movie(Integer.parseInt(movieAttributes[0]), 
					movieAttributes[1], Category.valueOf(movieAttributes[2].toUpperCase()), 
					Language.valueOf(movieAttributes[3]), 
					LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/yyyy").parse(movieAttributes[4])),
					casting,
					Double.parseDouble(movieAttributes[6]),
					Double.parseDouble(movieAttributes[7])
					));
		}
		return movies;
	}
	
	int getCategoryValue(Category a) {
		switch(a) {
		case ACTION:
			return 0;
		case ADVENTURE:
			return 1;
		case ANIMATED:
			return 2;
		case COMEDY:
			return 3;
		case DRAMA:
			return 4;
		case FANTASY:
			return 5;
		case HISTORICAL:
			return 6;
		case HORROR:
			return 7;
		case SCIENCE:
			return 8;
		case THRILLER:
			return 9;
		case ROMANCE:
			return 10;
		}
		return 0;
	}
	
	int getLanguageCategory(Language l) {
		switch(l) {
		case ENGLISH:
			return 0;
		case HINDI:
			return 1;
		case MARATHI:
			return 2;
		case TELUGU:
			return 3;
		case KANNADA:
			return 4;
		case TAMIL:
			return 5;
		}
		return 0;
	}
	
	Boolean allMoviesInDb(List<Movie> movies) {
		for(Movie movie : movies) {
			try {
				PreparedStatement ps = con.prepareStatement("insert into java_ass09_movies_info values(?, ?, ?, ?, ?, ?, ?)");
				ps.setInt(1, movie.getMovieId());
				ps.setString(2, movie.getMovieName());
				ps.setInt(3, getCategoryValue(movie.getMovieType()));
				ps.setInt(4, getLanguageCategory(movie.getLanguage()));
				ps.setDate(5, Date.valueOf(movie.getReleaseDate()));
				ps.setDouble(6, movie.getRating());
				ps.setDouble(7, movie.getTotalBusinessDone());
				ps.execute();
				for(String cast : movie.getCasting()) {
					ps = con.prepareStatement("insert into java_ass09_movies_cast values(?, ?)");
					ps.setInt(1, movie.getMovieId());
					ps.setString(2, cast);
					ps.execute();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
		}
		return true;
	}
	
	void addMovie(Movie movie, List<Movie> movies) {
		movies.add(movie);
	}
	
	void serializeMovies(List<Movie> movies, String fileName) throws IOException {
		FileOutputStream file = new FileOutputStream(fileName); 
        ObjectOutputStream out = new ObjectOutputStream(file); 

        for(Movie movie : movies) {
        	out.writeObject(movie);
        }
          
        out.close(); 
        file.close();
	}
	
	List<Movie> deserializeMovies(String fileName) throws ClassNotFoundException, IOException {
		FileInputStream file = new FileInputStream(fileName); 
        ObjectInputStream in = new ObjectInputStream(file); 
        
        ArrayList<Movie> movies = new ArrayList<Movie>();
          
        // Method for deserialization of object 
        while(in.available() != 0) {
        	movies.add((Movie)in.readObject());
        }
          
        in.close(); 
        file.close();
      
        return movies;
	}
	
	List<Movie> getMoviesReleasedInYear(int year) throws SQLException {
		PreparedStatement st = con.prepareStatement("select * from java_ass09_movies_info where to_char(release_date, 'yyyy')=?");
		st.setString(1, new Integer(year).toString());
		ResultSet rs = st.executeQuery();
		ArrayList<Movie> movies = new ArrayList<Movie>();
		while(rs.next()) {
			st = con.prepareStatement("select cast_name from java_ass09_movies_cast where movie_id=?");
			st.setInt(1, rs.getInt(1));
			ResultSet rs1 = st.executeQuery();
			ArrayList<String> cast = new ArrayList<String>();
			while(rs1.next()) {
				cast.add(rs1.getString(1));
			}
			movies.add(new Movie(
					rs.getInt(1),
					rs.getString(2),
					Category.values()[rs.getInt(3)],
					Language.values()[rs.getInt(4)],
					rs.getDate(5).toLocalDate(),
					cast,
					rs.getDouble(6),
					rs.getDouble(7)
					));
		}
		return movies;
	}
	
	List<Movie> getMoviesByActor(String actors[]) throws SQLException {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		for(String actor : actors) {
			PreparedStatement st = con.prepareStatement("select * from java_ass09_movies_info where movie_id in(select movie_id from java_ass09_movies_cast where cast_name=?)");
			st.setString(1, actor);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				st = con.prepareStatement("select cast_name from java_ass09_movies_cast where movie_id=?");
				st.setInt(1, rs.getInt(1));
				ResultSet rs1 = st.executeQuery();
				ArrayList<String> cast = new ArrayList<String>();
				while(rs1.next()) {
					cast.add(rs1.getString(1));
				}
				movies.add(new Movie(
						rs.getInt(1),
						rs.getString(2),
						Category.values()[rs.getInt(3)],
						Language.values()[rs.getInt(4)],
						rs.getDate(5).toLocalDate(),
						cast,
						rs.getDouble(6),
						rs.getDouble(7)
						));
			}
		}
		return movies;
	}
	
	void updateRatings(int movieId, double rating, List<Movie> movies) {
		for(int i=0; i<movies.size(); i++) {
			if(movies.get(i).getMovieId() == movieId) {
				movies.get(i).setMovieId(movieId);
				return;
			}
		}
	}
	
	void updateBusiness(int movieId, double amount, List<Movie> movies) {
		for(int i=0; i<movies.size(); i++) {
			if(movies.get(i).getMovieId() == movieId) {
				movies.get(i).setTotalBusinessDone(amount);
				return;
			}
		}
	}
	
	Map<Language, Set<Movie>> businessDone(double amount) throws SQLException {
		Map<Language, Set<Movie>> mp=new HashMap<Language,Set<Movie>>();
		PreparedStatement st = con.prepareStatement("select * from java_ass09_movies_info where total_business_done>? order by total_business_done");
		st.setDouble(1, amount);
		ResultSet rs = st.executeQuery();
		for(Language lang : Language.values()) {
			mp.put(lang, new HashSet<Movie>());
		}
		while(rs.next()) {
			st = con.prepareStatement("select cast_name from java_ass09_movies_cast where movie_id=?");
			st.setInt(1, rs.getInt(1));
			ResultSet rs1 = st.executeQuery();
			ArrayList<String> cast = new ArrayList<String>();
			while(rs1.next()) {
				cast.add(rs1.getString(1));
			}
			mp.get(Language.values()[rs.getInt(4)]).add(new Movie(
					rs.getInt(1),
					rs.getString(2),
					Category.values()[rs.getInt(3)],
					Language.values()[rs.getInt(4)],
					rs.getDate(5).toLocalDate(),
					cast,
					rs.getDouble(6),
					rs.getDouble(7)
					));
		}
		return mp;
	}
	
	@Override
	public void finalize() {
		try {
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection unable to close");
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		MainExecution obj = new MainExecution();
		List<Movie> movies = obj.populatedMovies(Paths.get("moviesdataforread.txt").toFile());
		System.out.println(movies);
		System.out.println("Movie in db : " + obj.allMoviesInDb(movies));
		obj.addMovie(new Movie(), movies);
		obj.serializeMovies(movies, "serializedMovies.bat");
		movies = obj.deserializeMovies("serializedMovies.bat");
		System.out.println(obj.getMoviesReleasedInYear(2019));
		System.out.println(obj.getMoviesByActor(new String[] {"Scarlett Johansson", "Jeremy Renner"}));
		obj.updateRatings(1, 4.5, movies);
		obj.updateBusiness(1, 1200, movies);
		System.out.println(obj.businessDone(500));
		
		System.out.println();
		System.out.println(obj.allMoviesInDb(movies));
		System.out.println(obj.populatedMovies(Paths.get("moviesdataforread.txt").toFile()));
	}
}
