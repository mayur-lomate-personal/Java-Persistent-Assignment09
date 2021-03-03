package movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Category {
	ACTION, ADVENTURE, ANIMATED, COMEDY, DRAMA, FANTASY, HISTORICAL, HORROR, SCIENCE, THRILLER, ROMANCE;
}

enum Language {
	ENGLISH, HINDI, MARATHI, TELUGU, KANNADA, TAMIL;
}

public class Movie implements Serializable {
	private int movieId;
	private String movieName;
	private Category movieType;
	private Language language;
	private LocalDate releaseDate;
	private List<String> casting;
	private Double rating;
	private Double totalBusinessDone;
	public Movie(int movieId, String movieName, Category movieType, Language language, LocalDate releaseDate,
			List<String> casting, Double rating, Double totalBusinessDone) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.movieType = movieType;
		this.language = language;
		this.releaseDate = releaseDate;
		this.casting = casting;
		this.rating = rating;
		this.totalBusinessDone = totalBusinessDone;
	}
	public Movie() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the Movie ID : ");
		this.movieId = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter the Movie Name : ");
		this.movieName = sc.nextLine();
		System.out.print("Enter the Movie Type(ACTION, ADVENTURE, ANIMATED, COMEDY, DRAMA, FANTASY, HISTORICAL, HORROR, SCIENCE, THRILLER) : ");
		this.movieType = Category.valueOf(sc.nextLine().toUpperCase());
		System.out.print("Enter the Movie Language(ENGLISH, HINDI, MARATHI, TELUGU, KANNADA, TAMIL) : ");
		this.language = Language.valueOf(sc.nextLine().toUpperCase());
		System.out.print("Enter the Movie Release Date : ");
		this.releaseDate = LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/yyyy").parse(sc.nextLine()));
		System.out.print("Enter the no of members in Movie Casting : ");
		int n = sc.nextInt();
		sc.nextLine();
		this.casting = new ArrayList<String>();
		for(int i=1; i<=n; i++) {
			System.out.print("Enter the Cast Member " + i + " : ");
			this.casting.add(sc.nextLine());
		}
		System.out.print("Enter the Movie Rating : ");
		this.rating = sc.nextDouble();
		System.out.print("Enter the Movie's Total Business Done : ");
		this.totalBusinessDone = sc.nextDouble();
		sc.nextLine();
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public Category getMovieType() {
		return movieType;
	}
	public void setMovieType(Category movieType) {
		this.movieType = movieType;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<String> getCasting() {
		return casting;
	}
	public void setCasting(List<String> casting) {
		this.casting = casting;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Double getTotalBusinessDone() {
		return totalBusinessDone;
	}
	public void setTotalBusinessDone(Double totalBusinessDone) {
		this.totalBusinessDone = totalBusinessDone;
	}
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", movieName=" + movieName + ", movieType=" + movieType + ", language="
				+ language + ", releaseDate=" + releaseDate + ", casting=" + casting + ", rating=" + rating
				+ ", totalBusinessDone=" + totalBusinessDone + "]";
	}
	
}
