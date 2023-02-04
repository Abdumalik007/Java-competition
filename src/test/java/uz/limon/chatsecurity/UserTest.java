package uz.limon.chatsecurity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.limon.chatsecurity.dto.ResponseDTO;
import uz.limon.chatsecurity.dto.UserDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("Ab3kmems");
        userDTO.setUsername("malik1210");
        userDTO.setFirstName("abdumalik");
        userDTO.setLastName("sobirov");
        userDTO.setPhoneNumber("+998-94-923-32-28");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        String responseBody = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ObjectReader reader = objectMapper.readerFor(new TypeReference<ResponseDTO<Integer>>() {});
        ResponseDTO<Integer> responseDTO = reader.readValue(responseBody);

        Assertions.assertEquals(0, responseDTO.getCode());
        Assertions.assertNotNull( responseDTO.getData());
        Assertions.assertEquals("Successfully saved!", responseDTO.getMessage());
        Assertions.assertTrue(responseDTO.isSuccess());

    }

}
