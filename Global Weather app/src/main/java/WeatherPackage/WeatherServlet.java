package WeatherPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet("/WeatherServlet")
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputData = request.getParameter("userInput");

		//API Setup
		String apiKey = "bed8b8892b34443b27bd8ceb370cd12b\r\n"
				+ "" ;
		//Get the City from the input
		String city = request.getParameter("city");
		
		//Create the URL for the Global Weather report app API request
		String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=Patna&appid=bed8b8892b34443b27bd8ceb370cd12b";
		
		//API Integration
		URL url = new URL (apiURL);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		//want to store in String
		StringBuilder responseContent = new StringBuilder();
		
		// create a scanner object to get input from user.
		Scanner scanner = new Scanner(reader);
		
		while (scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		
		//TypeCasting : Passing the data into JSon
		Gson gson= new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.println(jsonObject);
		
		//Date and Time
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date (dateTimestamp).toString();
		
		//Temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind speed
		double windspeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Condition
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//Set the data as set request attribute (for sending the jsp file)
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperaturee", temperatureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windspeed", windspeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		
		//forward the request to the weather.jsp page for rendering
		request.getRequestDispatcher("index.jsp").forward(request,response);
		
//	} catch (IOException e) {
//		e.printStackTrace();
	}

}
