package api.controller.v1;

import api.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    public static UserController userController;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private final String URL = "/api/v1/users";
    private final String EMAIL = "test@test.test";
    private final String PASSWORD = "12345678";
    private final String SALARY = "15,000";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getUserJson())
                .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getUserJson("test", "", ""))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid e-mail.")));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getUserJson("", "1234567", ""))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Password must be at least 8 characters.")));
    }

    @Test
    public void testInvalidSalary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getUserJson("", "", "15,000฿"))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Salary must be numeric.")));
    }

    @Test
    public void testSalaryLessThan15000() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getUserJson("", "", "14,999"))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Must have based salary more than 15,000 THB/month to register.")));
    }

    private String getUserJson(){
        return getUserJson("", "", "");
    }

    private String getUserJson(String email, String password, String salary){
        if(StringUtils.isEmpty(email)) email = EMAIL;
        if(StringUtils.isEmpty(password)) password = PASSWORD;
        if(StringUtils.isEmpty(salary)) salary = SALARY;

        return "{\"email\" : \"" + email + "\"," +
                "\"password\" : \"" + password + "\"," +
                "\"salary\" : \"" + salary + "\"}";
    }
}