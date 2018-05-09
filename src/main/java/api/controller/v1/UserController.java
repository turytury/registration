package api.controller.v1;

import api.model.User;
import api.model.UserForm;
import api.repository.UserRepository;
import api.transport.ResponseTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUsers() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody UserForm user) {
        String errMsg = checkEmpty(user);
        if(!StringUtils.isEmpty(errMsg)){
            return badRequestResponse(errMsg);
        }

        if(!validateEmail(user.getEmail())) return badRequestResponse("Invalid e-mail.");

        errMsg = checkPassword(user.getPassword());
        if(!StringUtils.isEmpty(errMsg)){
            return badRequestResponse(errMsg);
        }

        if(!validateSalary(user.getSalary())) return badRequestResponse("Salary must be numeric.");

        String type = getUserType(user.getSalary());
        if(StringUtils.isEmpty(type)) {
            return badRequestResponse("Must have based salary more than 15,000 THB/month to register.");
        }

        User result = repository.save(new User(user.getEmail(), user.getPassword(), user.getSalary(), type));

        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    private String checkEmpty(UserForm user){
        if(StringUtils.isEmpty(user.getEmail())) return "E-mail is required.";
        if(StringUtils.isEmpty(user.getPassword())) return "Password is required.";
        if(StringUtils.isEmpty(user.getSalary())) return "Salary is required.";
        return "";
    }

    private String checkPassword(String password){
        if(password.length() < 8) return "Password must be at least 8 characters.";
        return "";
    }

    private boolean validateSalary(String Salary){
        try {
            Long.parseLong(Salary.replace(",", ""));
            return true;
        }catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean validateEmail(String email){
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE).matcher(email);
        if(matcher.find()) return true;
        return false;
    }

    private String getUserType(String value){
        Long salary = Long.parseLong(value.replace(",", ""));
        if (salary > 50000) return "Platinum";
        if (salary > 30000) return "Gold";
        if (salary >= 15000) return "Silver";
        return "";
    }

    private ResponseEntity badRequestResponse(String message){
        return new ResponseEntity<>(errorResponse("400", message), HttpStatus.BAD_REQUEST);
    }

    private ResponseTransport errorResponse(String status, String message){
        ResponseTransport response = new ResponseTransport();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}