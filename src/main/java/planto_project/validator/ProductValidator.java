package planto_project.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import planto_project.dao.ProductRepository;
import planto_project.dto.ProductDto;
import planto_project.dto.UpdateUserDto;
import planto_project.model.Product;
import planto_project.model.UserAccount;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    final ProductRepository productRepository;

    private <T> void updateField(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }

    public void updateFields(Product product, ProductDto productDto) {
        updateField(productDto.getName(), product.getName(), product::setName);
        updateField(productDto.getCategory(), product.getCategory(), product::setCategory);
        updateField(productDto.getDescription(), product.getDescription(), product::setDescription);
        updateField(productDto.getImageUrl(), product.getImageUrl(), product::setImageUrl);
        updateField(productDto.getPrice(), product.getPrice(), product::setPrice);


    }

}
