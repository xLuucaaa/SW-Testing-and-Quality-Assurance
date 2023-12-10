import com.codewitharjun.fullstackbackend.model.User;
import com.codewitharjun.fullstackbackend.repository.UserRepository;
import com.codewitharjun.fullstackbackend.exception.UserNotFoundException;
import com.codewitharjun.fullstackbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
                }}
        );
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertNotNull(result);
        assertEquals(users.size(), result.size(),
                "Size of the returned user list should match the size of the sample data list");
        assertEquals(users.get(0), result.get(0),
                "First user in the returned list should match the first user of the sample data list");
        assertEquals(users.get(1), result.get(1),
                "Second user in the returned list should match the second user of the sample data list");
        assertEquals(result.get(0).getName(), "User One");
        assertEquals(result.get(0).getDepartment(), "IT");
        assertEquals(result.get(1).getName(), "User Two");
        assertEquals(result.get(1).getDepartment(), "HR");

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getExistingUserById() {
        Long userId = 1L;
        User expectedUser = users.get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.get(userId);

        assertNotNull(result);
        assertEquals(expectedUser, result, "Returned user should match the expected user");
        assertEquals(expectedUser.getName(), "User One");
        assertEquals(expectedUser.getDepartment(), "IT");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getNonExistingUserById() {
        Long userId = 4L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void addUser() {
        User user = new User() {{
            setId(3L);
            setUsername("user3");
            setName("User Three");
            setEmail("user3@example.com");
            setDepartment("PR");
        }};

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.add(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.getId(), "Saved user should have a non-null ID");
        assertEquals(user, result, "Returned user should match the expected user");
        assertEquals(user, savedUser, "Saved user should match the expected user");
    }

    @Test
    void updateExistingUser() {
        Long userId = 1L;
        User existingUser = users.get(0);

        User updatedUser = new User() {{
            setUsername("updatedUser1");
            setName("Updated User One");
            setEmail("updatedUser1@example.com");
            setDepartment("UpdatedDepartment1");
        }};

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(updatedUser, userId);

        assertNotNull(result);
        assertEquals(updatedUser, result, "Returned user should match the updated user");
        assertEquals(updatedUser.getUsername(), result.getUsername(), "Username should be updated");
        assertEquals(updatedUser.getName(), result.getName(), "Name should be updated");
        assertEquals(updatedUser.getEmail(), result.getEmail(), "Email should be updated");
        assertEquals(updatedUser.getDepartment(), result.getDepartment(), "Department should be updated");

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateNonExistingUser() {
        Long userId = 4L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(new User(), userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteExistingUser() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        String result = userService.delete(userId);

        assertEquals("User with id " + userId + " has been deleted successfully.", result);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void deleteNonExistingUser() {
        Long userId = 4L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
