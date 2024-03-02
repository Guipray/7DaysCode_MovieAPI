package br.com.alura.dayscode.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.dayscode.infra.service.ImdbApiClient;
import br.com.alura.dayscode.model.ListOfMovies;
import br.com.alura.dayscode.model.Movie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("filmes")
public class MovieController {
	
	public static final String POST_SUCCESS = "Filme inserido aos favoritos com sucesso!";
	public static final String POST_FAIL = "Filme não foi encontrado na lista";
	
	private ListOfMovies listOfMovies = new ListOfMovies(new ArrayList<>());
	private ListOfMovies favorites = new ListOfMovies(new ArrayList<>());
	
	@Autowired
	private ImdbApiClient imdbApiClient;

	@GetMapping("/top250/json")
	public ResponseEntity<ListOfMovies> getTop250Movies(@RequestParam (required = false) String title) {
		ListOfMovies movies = this.imdbApiClient.getBody();
		if (title != null) {
			ListOfMovies filteredMovies = this.imdbApiClient.getMoviesByTitle(movies, title);
			this.addMovieToList(filteredMovies);
		} else {
			this.addMovieToList(movies);
		}
		return ResponseEntity.ok(listOfMovies);
	}
	
	@GetMapping("/top250")
    public void getMoviesHtml(HttpServletResponse response, @RequestParam(required = false) String title) throws IOException {
        ListOfMovies movies = this.imdbApiClient.getBody();
        response.setContentType("text/html");
        PrintWriter writer = this.imdbApiClient.getHTML();
        writer = response.getWriter();
        
        if (title != null) {
        	ListOfMovies filteredMovies = this.imdbApiClient.getMoviesByTitle(movies, title);
        	this.addMovieToList(filteredMovies);
        	new HTMLGenerator(writer).generate(filteredMovies);
        } else {
        	this.addMovieToList(movies);
        	new HTMLGenerator(writer).generate(movies);
        }
        
        writer.close();
    }
	
	@PostMapping("/favorito/{movieId}")
	public String addFavoriteMovie(@PathVariable String movieId) {
		if (this.listOfMovies.items().isEmpty()) {
			getTop250Movies(null);
		}
		
		Optional<Movie> movieOp =
				this.listOfMovies.items().stream()
						.filter(movie -> movie.id().equalsIgnoreCase(movieId))
						.findFirst();
		
		if (movieOp.isPresent()) {
			this.favorites.items().add(movieOp.get());
			return POST_SUCCESS;
		} else {
			return POST_FAIL;
		}
		
	}
	
	@GetMapping("/favoritos/json")
	public ResponseEntity<ListOfMovies> getFavoriteMovies() {
		return ResponseEntity.ok(favorites);
	}
	
	@GetMapping("/favoritos")
	public void getFavoriteMoviesHTML(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter writer = new PrintWriter("src/main/resources/favoritos.html");
		writer = response.getWriter();
		new HTMLGenerator(writer).generate(favorites);
		
		writer.close();
	}
	
	public void addMovieToList(ListOfMovies movies) {
		this.listOfMovies.items().clear();
		this.listOfMovies.items().addAll(movies.items());
	}
	
}
