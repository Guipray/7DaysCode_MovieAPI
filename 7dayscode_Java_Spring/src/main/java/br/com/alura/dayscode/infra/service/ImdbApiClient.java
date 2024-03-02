package br.com.alura.dayscode.infra.service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alura.dayscode.model.ListOfMovies;

@Service
public class ImdbApiClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${imdb.apiurl}")
	private String apiUrl;

	public ListOfMovies getBody() {
		return this.restTemplate.getForObject(apiUrl, ListOfMovies.class);
	}
	
	public PrintWriter getHTML() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("src/main/resources/content.html");
		return writer;
	}
	
	public ListOfMovies getMoviesByTitle(ListOfMovies movies, String title) {
		ListOfMovies filteredMovies = new ListOfMovies(movies.items().stream()
											.filter(movie -> movie.title().toLowerCase().startsWith(title.toLowerCase()))
											.collect(Collectors.toList()));
		return filteredMovies;
	}
	
}
