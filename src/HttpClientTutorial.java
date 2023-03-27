import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class HttpClientTutorial {

    static Scanner s = new Scanner(System.in);
    static String sessionId = "";

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("I'm a powerful AI. I am going to take over the world tomorrow");
        System.out.println("But first, let's play a numbers game!");
        getSessionId();
        System.out.println("Would you like to play? (type 'start') ");

        while (true) {
            String userInput = s.nextLine().trim();

            switch (userInput) {
                case "stats" -> getStats();
                case "status" -> getStatus();
                case "start" -> startGame();
                case "exit" -> endGame();
                case "shutdown" -> shutDownServer();
                default -> doGuess(userInput);
            }
        }
    }

    private static void getSessionId() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/login", "", "POST");
        sessionId = response.headers().allValues("sessionId").get(0);
    }

    public static void getStats() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/stats", "", "GET");
        System.out.println(response.body());
    }

    public static void getStatus() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/status", "", "GET");
        System.out.println(response.body());
    }

    public static void startGame() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/start-game", "", "POST");
        if (response.statusCode() == 200) {
            System.out.println("Game started! Guess a number from 1 to 100!");
        } else {
            System.out.println("The game has already been started!");
        }
    }

    public static void endGame() throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/end-game", "", "POST");
        if (response.statusCode() == 200) {
            System.out.println("Game ended!");
        } else {
            System.out.println("There is no active game!  Type 'start' for a new game");
        }
    }

    public static void doGuess(String userInput) throws IOException, InterruptedException {
        HttpResponse<String> response = doRequest("/guess", userInput, "POST");
        if (response.statusCode() == 200) {
            if (response.body().contains("EQUAL")) {
                System.out.println("You guessed the number! The game is now over. \n Type start to play again.");
            } else {
                System.out.println("Your number is " + response.body() + " than my number!");
            }
        } else {
            System.out.println(response.body());
        }
    }

    private static void shutDownServer() {
        System.out.println("Thank you for travelling with lux express!");
        System.exit(0);
    }

    private static HttpResponse<String> doRequest(String endPoint, String body, String requestMethod) throws IOException, InterruptedException {
        URI HTTP_SERVER_URI = URI.create("http://10.10.10.25:5555" + endPoint);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method(requestMethod, HttpRequest.BodyPublishers.ofString(body))
                .uri(HTTP_SERVER_URI)
                .header("sessionId", sessionId)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}