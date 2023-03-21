import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpClientTutorial {

    static Scanner s = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("I'm a powerful AI. I am going to take over the world tomorrow");
        System.out.println("But first, let's play a numbers game!");
        System.out.println("Would you like to play? (type 'start') ");

        while (true) {
            String userInput = s.nextLine().trim();

            switch (userInput) {
                case "start" -> startGame();
                case "end" -> endGame();
                case "exit" -> exitGame();
                default -> doGuess(userInput);
            }
        }
    }

    public static void startGame() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/start-game", "");
        if (response.statusCode() == 200) {
            System.out.println("Game started! Guess a number from 1 to 100!");
        } else {
            System.out.println("The game has already been started!");
        }
    }

    public static void endGame() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/end-game", "");
        if (response.statusCode() == 200) {
            System.out.println("Game ended!");
        } else {
            System.out.println("There is no active game!  Type 'start' for a new game");
        }
    }

    public static void doGuess(String userInput) throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/guess", userInput);
        if (response.statusCode() == 200) {
            if (response.body().contains("EQUAL")) {
                System.out.println("You guessed the number! The game is now over. You guessed " + response.body().split(" ")[1] + " times.");
            } else {
                System.out.println("Your number is " + response.body().split(" ")[0] + " than my number!");
            }
        } else {
            System.out.println(response.body());
        }
    }

    private static void exitGame() {
        System.out.println("Thank you for travelling with lux express!");
        System.exit(0);
    }

    private static HttpResponse<String> doRequest(String endPoint, String body) throws IOException, InterruptedException {
        URI HTTP_SERVER_URI = URI.create("http://localhost:5555" + endPoint);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(HTTP_SERVER_URI)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}