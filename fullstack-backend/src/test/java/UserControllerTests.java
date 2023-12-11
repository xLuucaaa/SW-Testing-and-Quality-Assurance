import com.codewitharjun.fullstackbackend.model.User;
import com.codewitharjun.fullstackbackend.service.UserService;
import com.codewitharjun.fullstackbackend.controller.UserController;
import com.codewitharjun.fullstackbackend.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;

public class UserControllerTests {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        users = List.of(
                new User() {{
                    setId(1L);
                    setUsername("user1");
                    setName("User One");
                    setEmail("user1@example.com");
                    setDepartment("IT");
                }},
                new User() {{
                    setId(2L);
                    setUsername("user2");
                    setName("User Two");
                    setEmail("user2@example.com");
                    setDepartment("HR");
                }},
                new User() {{
                    setId(3L);
                    setUsername("user3");
                    setName("User Three");
                    setEmail("user3@example.com");
                    setDepartment("PR");
                }}
        );
    }

    @Test
    public void getAllUsersAPI_Success() throws Exception {
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[2].username").value("user3"));

        verify(userService, times(1)).getAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserByIdAPI_Success() throws Exception {
        when(userService.get(1L)).thenReturn(users.get(0));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("User One"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.department").value("IT"));

        verify(userService, times(1)).get(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserByIdAPI_UserNotFound() {
        when(userService.get(1L)).thenThrow(new UserNotFoundException(1L));

        Exception thrownException = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(get("/user/1")));

        assertTrue(thrownException.getCause() instanceof UserNotFoundException);
        assertEquals("Could not find the user with id 1", thrownException.getCause().getMessage());

        verify(userService, times(1)).get(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void addUserAPI_Success() throws Exception {
        User newUser = new User() {{
            setId(4L);
            setUsername("user4");
            setName("User Four");
            setEmail("user4@example.com");
            setDepartment("Finance");
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(newUser);

        when(userService.add(any())).thenReturn(newUser);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.name").value("User Four"))
                .andExpect(jsonPath("$.username").value("user4"))
                .andExpect(jsonPath("$.email").value("user4@example.com"))
                .andExpect(jsonPath("$.department").value("Finance"));

        verify(userService, times(1)).add(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUserAPI_Success() throws Exception {
        User updatedUser = new User() {{
            setId(1L);
            setUsername("user1updated");
            setName("User One Updated");
            setEmail("user1updated@example.com");
            setDepartment("HR");
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedUserJson = objectMapper.writeValueAsString(updatedUser);

        when(userService.update(any(), eq(1L))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("User One Updated"))
                .andExpect(jsonPath("$.username").value("user1updated"))
                .andExpect(jsonPath("$.email").value("user1updated@example.com"))
                .andExpect(jsonPath("$.department").value("HR"));

        verify(userService, times(1)).update(any(), eq(1L));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUserAPI_UserNotFound() throws Exception {
        when(userService.update(any(), eq(1L))).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(put("/user/1"))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserAPI_Success() throws Exception {
        String successMessage = "User with id 1 has been deleted successfully.";

        when(userService.delete(1L)).thenReturn(successMessage);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(userService, times(1)).delete(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserAPI_UserNotFound() {
        when(userService.delete(1L)).thenThrow(new UserNotFoundException(1L));

        Exception thrownException = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(delete("/user/1")));

        assertTrue(thrownException.getCause() instanceof UserNotFoundException);
        assertEquals("Could not find the user with id 1", thrownException.getCause().getMessage());

        verify(userService, times(1)).delete(1L);
        verifyNoMoreInteractions(userService);
    }
}
