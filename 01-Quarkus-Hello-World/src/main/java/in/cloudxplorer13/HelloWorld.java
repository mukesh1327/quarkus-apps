package in.cloudxplorer13;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

@Path("/login")
public class HelloWorld {

    // Simple hardcoded username/password
    private static final String VALID_USER = "admin";
    private static final String VALID_PASS = "password123";

    // STEP 1: Show login page (GET)
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String showLoginPage() {
        return "<html>" +
                "<head><title>Login</title></head>" +
                "<body>" +
                "<h1>Login Page</h1>" +
                "<form method='POST' action='/login/submit'>" +
                "Username: <input type='text' name='username'/><br><br>" +
                "Password: <input type='password' name='password'/><br><br>" +
                "<input type='submit' value='Login'/>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    // STEP 2: Handle login submission (POST)
    @POST
    @Path("/submit")
    @Produces(MediaType.TEXT_HTML)
    public String handleLogin(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        if (username == null || password == null) {
            return "<html><body><h2>Invalid input!</h2></body></html>";
        }

        if (username.equals(VALID_USER) && password.equals(VALID_PASS)) {
            return "<html><body>" +
                    "<h1>Login Successful!</h1>" +
                    "<p>Welcome, " + username + ".</p>" +
                    "</body></html>";
        } else {
            return "<html><body>" +
                    "<h1>Login Failed!</h1>" +
                    "<p>Incorrect username or password.</p>" +
                    "<a href='/login'>Try Again</a>" +
                    "</body></html>";
        }
    }
}
