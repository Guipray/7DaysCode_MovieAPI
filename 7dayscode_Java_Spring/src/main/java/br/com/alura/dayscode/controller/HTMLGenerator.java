package br.com.alura.dayscode.controller;

import java.io.PrintWriter;

import br.com.alura.dayscode.model.ListOfMovies;

public class HTMLGenerator {
	
	private PrintWriter writer;

	public HTMLGenerator(PrintWriter writer) {
		this.writer = writer;
	}
	
    String head =
            """
            <head>
                <meta charset=\"utf-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">
                <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\" 
                    + "integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">					
            </head>
            """;
	
    String divTemplate =
            """
            <div class=\"card text-white bg-dark mb-3\" style=\"max-width: 18rem;\">
                <h4 class=\"card-header\">%s</h4>
                <div class=\"card-body\">
                    <img class=\"card-img\" src=\"%s\" alt=\"%s\">
                    <p class=\"card-text mt-2\">Nota: %s - Ano: %s</p>
                </div>
            </div>
            """;
	
	public void generate(ListOfMovies movies){
		
		writer.println(head);
		
		writer.println("<body>");
		
		movies.items()
			  .forEach(movie -> {
				  writer.println(divTemplate.formatted(movie.title(), movie.image(), movie.title(), movie.imDbRating(), movie.year()));
			  });
		writer.println(
				"""
					</body>
				</html>
				""");
	}
	
}
