package planto_project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import planto_project.dao.ProductRepository;
import planto_project.dto.*;
import planto_project.dto.filters_dto.*;
import planto_project.model.CategoryCount;
import planto_project.model.Filter;
import planto_project.model.Product;
import planto_project.validator.ProductValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, DataServices, DataForFilters<DataForProductsFiltersDto> {
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final ProductValidator productValidator;


    @Override
    public ProductDto createProduct(NewProductDto newProductDto) {
        Product product = modelMapper.map(newProductDto, Product.class);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String id) {
        Product product = productRepository.findProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product product = productRepository.findProductById(id);
        productValidator.updateFields(product, productDto);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto deleteProduct(String id) {
        Product product = productRepository.removeProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto changeQuantityOfProduct(String id, int amount, boolean flag) {
        Product product = productRepository.findProductById(id);
        product.changeQuantity(amount, flag);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public Page<ProductDto> findAllProducts(SortingDto sortingDto) {
        return productRepository.findAll(getPageable(sortingDto)).map(p -> modelMapper.map(p, ProductDto.class));
    }

    @Override
    public Page<ProductDto> findAllProductsWithCriteria(SortingDto sortingDto) {
        Sort.Direction direction = sortingDto.getDirection() > 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(sortingDto.getPage(), sortingDto.getSize(), Sort.by(direction, sortingDto.getField().toLowerCase()));

        Map<String, Filter<?>> filterMap = Map.ofEntries(
                Map.entry("name", new Filter<String>()),
                Map.entry("category", new Filter<String>()),
                Map.entry("price", new Filter<Double>()),
                Map.entry("quantity", new Filter<Integer>())
        );

        Query query = getQueryWithCriteria(filterMap, sortingDto);
//        Filter<String> filterName = null;
//        Filter<String> filterCategory = null;
//        Filter<Double> filterPrice = null;
//
//        List<FilterDto> filtersDto = sortingDto.getCriteria();
//        List<Criteria> criteries = new ArrayList<>();
//        for (FilterDto filterDto : filtersDto) {
//            String field = filterDto.getField();
//            if (field.equalsIgnoreCase("name")) {
//                filterName = new Filter<String>(filterDto.getField(),
//                        filterDto.getType(),
//                        (String) filterDto.getValue());
//                Criteria criteria = filterName.getCriteria();
//                if (criteria != null) {
//                    criteries.add(criteria);
//                }
//            }
//
//            else if (field.equalsIgnoreCase("price")
//                    && filterDto instanceof FilterDoubleDto filterDtoTmp) {
//
//                filterPrice = new Filter<Double>(filterDtoTmp.getField(),
//                        filterDtoTmp.getType(),
//                        filterDtoTmp.getValueFrom(),
//                        filterDtoTmp.getValueTo());
//
//                Criteria criteria = filterPrice.getCriteria();
//                if (criteria != null) {
//                    criteries.add(criteria);
//                }
//
//            }
//
//            else if (field.equalsIgnoreCase("category")
//                        && filterDto instanceof FilterStringDto filterDtoTmp) {
//
//                filterCategory = new Filter<String>(filterDtoTmp.getField(),
//                        filterDtoTmp.getType(),
//                        filterDtoTmp.getValueList());
//                Criteria criteria = filterCategory.getCriteria();
//                if (criteria != null) {
//                    criteries.add(criteria);
//                }
//
//            }
//        }
//
//        Query query = new Query();
//        if (!criteries.isEmpty()) {
//            query.addCriteria(new Criteria().andOperator(criteries));
//        }

        return productRepository.findProductsByQuery(query, pageable);
    }

    @Override
    public DataForProductsFiltersDto getDataForFilters() {

        BigDecimal maxPrice = productRepository.getTop1ByOrderByPriceDesc().getPrice();
        Integer inStock = productRepository.getDistinctByQuantityGreaterThan(0).size();
        Integer outStock = productRepository.getDistinctByQuantity(0).size();
        List<CategoryCount> result = productRepository.groupByCategory(Sort.by("_id"));

        List<CategoryDataDto> resultList = result.stream().map((categoryCount -> new CategoryDataDto(categoryCount.get_id(), categoryCount.getCount()))).toList();

        return new DataForProductsFiltersDto(maxPrice, resultList, inStock, outStock);
    }



}
