package planto_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private String zip;
    private String houseNumber;
    private String apartmentNumber;

}
